package org.example.grad_gateway2.DAO;


import org.example.grad_gateway2.Entity.Applications;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Applications, Long> {

    List<Applications> findAllByUserId(long userId);

    boolean existsByUserIdAndJobPostId(long jobPostId, long userId);

}
