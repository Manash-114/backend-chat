package com.whatsapp.repository;

import com.whatsapp.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    private User user;
    private List<User> users = new ArrayList<>();
    @BeforeEach
    void setUp(){

        users = List.of(new User(101,"Manash jyoti","m@gmail.com","profileurl",
                "12345"),
                new User(102,"Minku jyoti","minku12@gmail.com","profileurl",
                        "12345"),
                new User(103,"Maina jyoti","maina@gmail.com","profileurl",
                        "12345")
        );

        userRepository.saveAll(users);

    }

    @AfterEach
    void tearDown(){
        userRepository.deleteAll();
        user = null;
    }

    @Test
    void testFindByEmail() {
        User byEmail = userRepository.findByEmail("minku12@gmail.com");
        assertThat(byEmail.getId()).isEqualTo(users.get(1).getId());
    }

    @Test
    void searchUser_Found() {

        List<User> m = userRepository.searchUser("maina");
        assertThat(!m.isEmpty()).isTrue();
    }

    @Test
    void searchUser_NotFound() {

        List<User> m = userRepository.searchUser("xe");
        assertThat(m.isEmpty()).isTrue();
    }
}