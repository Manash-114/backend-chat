package com.whatsapp.repository;

import com.whatsapp.model.Chat;
import com.whatsapp.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message,Integer> {

    public List<Message> findByChat(Chat chat);

    @Query("select m from Message m join Chat c where c.id = :chatId")
    public List<Message> findByChatId(@Param("chatId") Integer chatId);
}
