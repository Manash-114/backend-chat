package com.whatsapp.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String content;
    private LocalDateTime timestamp;

    @ManyToOne
    private User user;

    @ManyToOne
    private Chat chat;
}
