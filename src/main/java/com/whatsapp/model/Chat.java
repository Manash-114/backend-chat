package com.whatsapp.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String chatName;
    private String chatImage;
    private boolean isGroup;

    //many chat can be created by one user
    @ManyToOne
    @JoinColumn(name = "creaedBy")
    private User createdBy;

    //multiple chat can be create by multiple user //for group
    @ManyToMany
    private Set<User> users =new HashSet<>();
    //many messages for one chat
    @OneToMany
    private List<Message> messageList = new ArrayList<>();

    @ManyToMany
    private Set<User> admin = new HashSet<>();

}
