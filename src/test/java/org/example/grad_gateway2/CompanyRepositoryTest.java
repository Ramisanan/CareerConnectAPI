package org.example.grad_gateway2;

import org.example.grad_gateway2.DAO.CompanyRepository;
import org.example.grad_gateway2.Entity.Company;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class CompanyRepositoryTest {

    @Autowired
    private CompanyRepository repo;

    @Test
    void customQueries_andExistsWork() {
        Company c1 = Company.builder().name("Alpha").location("NY").size(5).build();
        Company c2 = Company.builder().name("Beta").location("LA").size(20).build();
        repo.saveAll(List.of(c1, c2));

        List<Company> byName = repo.findAllByNameLikeIgnoreCase("%aLpha%");
        assertThat(byName).hasSize(1).first().extracting("name").isEqualTo("Alpha");

        List<Company> byLocation = repo.findAllByLocationLikeIgnoreCase("la");
        assertThat(byLocation).hasSize(1).first().extracting("location").isEqualTo("LA");

        List<Company> bySize = repo.findAllBySizeLessThanEqual(10);
        assertThat(bySize).hasSize(1).first().extracting("size").isEqualTo(5);

        assertThat(repo.existsByName("Alpha")).isTrue();
        assertThat(repo.existsByName("Gamma")).isFalse();
    }
}