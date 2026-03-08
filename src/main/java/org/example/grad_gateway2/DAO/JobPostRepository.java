package org.example.grad_gateway2.DAO;

import org.example.grad_gateway2.Entity.JobPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface JobPostRepository extends JpaRepository<JobPost, Long> {

    @Query("select j from JobPost j where j.company.id = :companyId")
    List<JobPost> findAllByCompanyId(Long companyId);

    List<JobPost> findAllByTitleLikeIgnoreCase(String title);

    List<JobPost> findAllByLocationLikeIgnoreCase(String location);

    List<JobPost> findAllByTypeLikeIgnoreCase(String type);

    List<JobPost> findAllByExperienceLessThanEqual(int experience);

    List<JobPost> findAllBySalaryLessThanEqual(int salary);

    List<JobPost> findAllByVisaSponsorship(boolean visaSponsorship);

    List<JobPost> findAllByPostedAtAfter(Date postedAt);

}
