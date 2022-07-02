package com.haltebogen.gittalk.service;

import com.haltebogen.gittalk.dto.member.ChatMemberResponseDto;
import com.haltebogen.gittalk.dto.member.GitUserProfileDto;
import com.haltebogen.gittalk.dto.member.MemberDetailResponseDto;
import com.haltebogen.gittalk.dto.member.MemberResponseDto;
import com.haltebogen.gittalk.dto.oauth.GithubUserResponseDto;
import com.haltebogen.gittalk.entity.Member;
import com.haltebogen.gittalk.entity.ProviderType;
import com.haltebogen.gittalk.repository.MemberRepository;
import com.haltebogen.gittalk.trace.Trace;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final RestTemplate restTemplate;

    private final String PAGINATION_PAGE_SIZE = "1000000";
    private final String GITHUB_FOLLOWERS_API_URL_PATH = "https://api.github.com/users/%s/followers?per_page=%s";

    @Trace
    public Page<Member> findUserBySearch(Pageable pageable, String keyword) {
        return memberRepository.findBySearch(keyword, pageable);
    }

    public Member createMember(GithubUserResponseDto githubUserResponseDto) {
        if (!isExistMember(githubUserResponseDto)) {
            Member member = Member.builder()
                    .providerId(githubUserResponseDto.getId())
                    .providerType(ProviderType.GITHUB)
                    .email("null")
                    .nickName(githubUserResponseDto.getLogin())
                    .name(githubUserResponseDto.getName())
                    .bio(githubUserResponseDto.getBio())
                    .company(githubUserResponseDto.getCompany())
                    .profileImageUrl(githubUserResponseDto.getAvatar_url())
                    .followersUrl(githubUserResponseDto.getFollowers_url())
                    .followingUrl(githubUserResponseDto.getFollowings_url())
                    .followersNum(githubUserResponseDto.getFollowers())
                    .followingsNum(githubUserResponseDto.getFollowing()).build();
            memberRepository.save(member);
            return member;
        }

        return memberRepository.findByProviderId(githubUserResponseDto.getId()).get();
    }

    public List<GitUserProfileDto> getChatMembers(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        List<GitUserProfileDto> gitUserProfiles = getGitHubFollowers(member.getNickName());
        return gitUserProfiles;
    }

    public MemberDetailResponseDto getMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        return new MemberDetailResponseDto(member);
    }

    private List<GitUserProfileDto> getGitHubFollowers(String name) {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<GitUserProfileDto[]> responseGithubData = restTemplate.exchange(
                String.format(GITHUB_FOLLOWERS_API_URL_PATH, name, PAGINATION_PAGE_SIZE),
                HttpMethod.GET,
                httpEntity,
                GitUserProfileDto[].class
        );

        return Arrays.asList(responseGithubData.getBody());
    }

    private List<MemberResponseDto> getMembers() {
        List<Member> members = memberRepository.findAll();
        return members.stream().map(MemberResponseDto::new).collect(Collectors.toList());
    }
    private boolean isExistMember(GithubUserResponseDto githubUserResponseDto){
        Long githubUserId = githubUserResponseDto.getId();

        return memberRepository.findByProviderId(githubUserId).isPresent();
    }

}
