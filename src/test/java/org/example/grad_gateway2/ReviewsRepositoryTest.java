package org.example.grad_gateway2;

import org.example.grad_gateway2.DAO.ReviewsRepository;
import org.example.grad_gateway2.Entity.Company;
import org.example.grad_gateway2.Entity.Reviews;
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
class ReviewsRepositoryTest {

    @Autowired
    private ReviewsRepository repo;

    @Test
    void queryMethodsReturn() {
        Company c = Company.builder().id(1L).name("C").build();
        User u = User.builder().id(2L).build();
        Reviews r = Reviews.builder().company(c).user(u).rating(3).build();
        repo.save(r);

        assertThat(repo.findAllByCompanyId(1L)).hasSize(1);
        assertThat(repo.findAllByCompanyIdOOrderByBest(1L)).hasSize(1);
        assertThat(repo.findAllByCompanyIdOrderByWorst(1L)).hasSize(1);
    }
}