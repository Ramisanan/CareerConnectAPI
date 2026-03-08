package org.example.grad_gateway2.DAO;

import org.example.grad_gateway2.Entity.Reviews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewsRepository extends JpaRepository<Reviews,Long> {

    @Query("select r from Reviews r where r.company.id = :companyId")
    List<Reviews> findAllByCompanyId(long companyId);

    @Query("select r from Reviews r where r.company.id = :companyId order by r.rating desc")
    List<Reviews> findAllByCompanyIdOOrderByBest(long companyId);

    @Query("select r from Reviews r where r.company.id =:companyId order by r.rating asc")
    List<Reviews> findAllByCompanyIdOrderByWorst(long companyId);
}
