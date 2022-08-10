package com.haltebogen.gittalk.entity.chat;

import com.haltebogen.gittalk.dto.member.ChatMemberResponseDto;
import com.haltebogen.gittalk.entity.user.Member;
import com.mongodb.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "rooms")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoom {

    @Id
    private String _id;

    private String roomName;

    @Nullable
    private List<ChatMessage> messages;

    private List<String> participantsId;

    @CreatedDate
    private LocalDateTime createdAt;
}
