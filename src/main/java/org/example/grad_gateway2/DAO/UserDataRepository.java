package org.example.grad_gateway2.DAO;

import org.example.grad_gateway2.Entity.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserDataRepository extends JpaRepository<UserData, Long> {

    @Query("select u from UserData u where u.user.id = :userId")

    UserData findByUserId(Long userId);
}
