package org.example.grad_gateway2.DAO;

import org.example.grad_gateway2.Entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    List<Company> findAllByNameLikeIgnoreCase(String name);

    List<Company> findAllByLocationLikeIgnoreCase(String location);

    List<Company> findAllBySizeLessThanEqual(int size);

    boolean existsByName(String name);


}
