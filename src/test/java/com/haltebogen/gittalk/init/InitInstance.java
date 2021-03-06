package com.haltebogen.gittalk.init;

import com.haltebogen.gittalk.dto.oauth.TokenDto;
import com.haltebogen.gittalk.dto.oauth.GithubUserResponseDto;
import com.haltebogen.gittalk.entity.user.Member;
import com.haltebogen.gittalk.entity.user.ProviderType;

public class InitInstance {

    private final String NICKNAME = "git-talk-admin";
    private final String COMPANY = "git-talk";
    private final String FOLLOWERS_URL = "https://github.com/followers-url";
    private final String FOLLOWING_URL = "https://github.com/following-url";
    private final String NAME = "sehoon";
    private final String BIO = "안녕하세요!! 백엔드 개발자 입니다!";

    public Member createDefaultMember() {
        return Member.builder()
                .providerId(1L)
                .providerType(ProviderType.GITHUB)
                .email("test@gitnub.com")
                .nickName(NICKNAME)
                .name(NAME)
                .bio(BIO)
                .company(COMPANY)
                .followersUrl(FOLLOWERS_URL)
                .followingUrl(FOLLOWING_URL).build();
    }

    public Member createCustomMember(Long providerId, String nickName) {
        return Member.builder()
                .providerId(providerId)
                .providerType(ProviderType.GITHUB)
                .email("test@gitnub.com")
                .nickName(nickName)
                .name(NAME)
                .bio(BIO)
                .company(COMPANY)
                .followersUrl(FOLLOWERS_URL)
                .followingUrl(FOLLOWING_URL).build();
    }

    public GithubUserResponseDto createGithubUserResponseDto(Long id, String nickName) {
        GithubUserResponseDto githubUserResponseDto = GithubUserResponseDto.builder()
                .bio(BIO)
                .avatar_url("https://github.com/profile-image")
                .id(id)
                .login(nickName)
                .company(COMPANY)
                .followers_url(FOLLOWERS_URL)
                .followings_url(FOLLOWING_URL)
                .following(10L)
                .followers(20L)
                .name(NAME).build();
        return githubUserResponseDto;
    }

    public TokenDto createTokenDto() {
        TokenDto tokenDto = TokenDto.builder()
                .access_token("access_token")
                .token_type("bearer")
                .scope("").build();

        return tokenDto;
    }

    public String createPaginationResponse(int totalPage, boolean hasNext) {
        String result = String.format(
                "{\"data\":{\"totalPage\":%s,\"hasNext\":%s,\"data\":[{\"email\":\"null\",\"nickName\":\"git-talk-admin\",\"profileImageUrl\":\"https://github.com/profile-image\",\"statusMessage\":null}]},\"message\":\"ok\",\"status\":200}"
                , totalPage, hasNext
        );
        return result;
    }
}
