package org.example.grad_gateway2;

import org.example.grad_gateway2.DAO.UserDataRepository;
import org.example.grad_gateway2.Entity.User;
import org.example.grad_gateway2.Entity.UserData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class UserDataRepositoryTest {

    @Autowired
    private UserDataRepository repo;

    @Test
    void findByUserIdWorks() {
        User u = User.builder().id(1L).build();
        UserData ud = UserData.builder().user(u).location("X").build();
        repo.save(ud);
        assertThat(repo.findByUserId(1L)).isNotNull();
    }
}