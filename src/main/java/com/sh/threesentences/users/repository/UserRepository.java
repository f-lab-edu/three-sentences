package com.sh.threesentences.users.repository;

import com.sh.threesentences.users.entity.Users;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<Users, Long> {

    @Query("select u.email from Users u where u.email= :email")
    public Optional<String> findByEmail(@Param("email") String email);

}
