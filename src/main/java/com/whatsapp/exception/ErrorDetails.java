package com.whatsapp.exception;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetails {
    private String error;
    private String message;
    private LocalDateTime timestamp;
}
