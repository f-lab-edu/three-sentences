package com.sh.threesentences.users.entity;

import com.sh.threesentences.users.dto.UserResponseDto;
import com.sh.threesentences.users.enums.MembershipType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 사용자 엔티티
 */
@Entity
@Getter
@NoArgsConstructor
public class Users {

    /**
     * 엔티티 식별자
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 사용자 이메일
     */
    @Column(unique = true)
    private String email;

    /**
     * 사용자 이름
     */
    @Column
    private String name;

    /**
     * 사용자 패스워드
     */
    @Column
    private String password;

    /**
     * 사용자 멤버십 타입
     * FREE, BASIC, PREMIUM 3가지 타입이 존재
     * 초기 디폴드값은 FREE로 세팅
     */
    @Column
    @Enumerated(EnumType.STRING)
    private MembershipType membership = MembershipType.FREE;

    public Users(String email, String name, String password, MembershipType membership) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.membership = membership;
    }

    public UserResponseDto toUserResponseDto() {
        return new UserResponseDto(this.id, this.email, this.name, this.membership);
    }
}
