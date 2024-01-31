package com.whatsapp.repository;

import com.whatsapp.model.Chat;
import com.whatsapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat,Integer> {


    @Query("select c from Chat c join c.users u where u.id=:userId")
    public List<Chat> findChatByUserId(@Param("userId") Integer userId);
    @Query("select c from Chat c where c.isGroup = false and :user1 member of c.users and :user2 member of c.users")
    Chat findSingleChatByUserIds(@Param("user1") User user1 , @Param("user2") User user2);
}
