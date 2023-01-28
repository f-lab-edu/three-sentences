package com.sh.threesentences.users.repository;

import com.sh.threesentences.users.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 해당 이메일로 등록된 이메일을 조회합니다.
     *
     * @param email 조회할 이메일 주소
     * @return 조회된 이메일
     */
    @Query("select u.email from User u where u.email= :email")
    public Optional<String> findByEmail(@Param("email") String email);

}
