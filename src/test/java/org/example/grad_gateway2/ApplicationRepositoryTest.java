package org.example.grad_gateway2;

import org.example.grad_gateway2.DAO.ApplicationRepository;
import org.example.grad_gateway2.Entity.Applications;
import org.example.grad_gateway2.Entity.JobPost;
import org.example.grad_gateway2.Entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class ApplicationRepositoryTest {

    @Autowired
    private ApplicationRepository repo;

    @Test
    void customMethodsWork() {
        User u = User.builder().id(1L).build();
        JobPost j = JobPost.builder().id(2L).build();
        Applications a = Applications.builder().user(u).jobPost(j).build();
        repo.save(a);

        List<Applications> byUser = repo.findAllByUserId(1L);
        assertThat(byUser).isNotEmpty();
        assertThat(repo.existsByUserIdAndJobPostId(1L, 2L)).isTrue();
    }
}