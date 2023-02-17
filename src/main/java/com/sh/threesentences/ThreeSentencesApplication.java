package com.sh.threesentences;

import com.sh.threesentences.common.config.AuditorAwareImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableJpaAuditing
public class ThreeSentencesApplication {

    public static void main(String[] args) {
        SpringApplication.run(ThreeSentencesApplication.class, args);
    }

    /**
     * created_by, updated_by 에 대한 사용자 지정을 위한 Bean
     *
     * @return AuditorAware
     */
    @Bean
    public AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }


    /**
     * 사용자 패스워드 저장 및 인증에 사용되는 Bean
     *
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(4);
    }
}
