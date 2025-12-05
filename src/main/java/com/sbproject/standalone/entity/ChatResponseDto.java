package com.sbproject.standalone.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class ChatResponseDto {
    private String session_id;
    private String reply;
}
