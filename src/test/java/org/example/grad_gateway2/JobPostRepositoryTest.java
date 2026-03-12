package org.example.grad_gateway2;

import org.example.grad_gateway2.DAO.JobPostRepository;
import org.example.grad_gateway2.Entity.Company;
import org.example.grad_gateway2.Entity.JobPost;
import org.example.grad_gateway2.Entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class JobPostRepositoryTest {

    @Autowired
    private JobPostRepository repo;

    @Test
    void customQueriesWork() {
        Company c = Company.builder().id(1L).name("C").build();
        User u = User.builder().id(2L).build();
        JobPost j = JobPost.builder().company(c).user(u).title("Hello").location("L").type("T").experience(1).salary(100).visaSponsorship(true).postedAt(new Date()).build();
        repo.save(j);

        assertThat(repo.findAllByTitleLikeIgnoreCase("%hello%%")).isNotEmpty();
        assertThat(repo.findAllByLocationLikeIgnoreCase("L")).isNotEmpty();
        assertThat(repo.findAllByTypeLikeIgnoreCase("T")).isNotEmpty();
        assertThat(repo.findAllByExperienceLessThanEqual(5)).isNotEmpty();
        assertThat(repo.findAllBySalaryLessThanEqual(200)).isNotEmpty();
        assertThat(repo.findAllByVisaSponsorship(true)).isNotEmpty();
        assertThat(repo.findAllByPostedAtAfter(new Date(0))).isNotEmpty();
    }
}