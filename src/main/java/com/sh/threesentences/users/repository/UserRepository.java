package com.sh.threesentences.users.repository;

import com.sh.threesentences.users.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {

}
