package com.whatsapp.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GroupChatRequest {
    private Set<Integer> userId;
    private String groupName;
    private String groupImage;
}
