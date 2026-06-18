package com.example.lsms.inspection.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EmailRequestDTO {
    private String receiverEmail;
    private String subject;
    private String content;
}
