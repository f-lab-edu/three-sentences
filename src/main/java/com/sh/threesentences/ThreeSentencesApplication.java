package com.sh.threesentences;

import java.util.Optional;
import java.util.UUID;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ThreeSentencesApplication {

	public static void main(String[] args) {
		SpringApplication.run(ThreeSentencesApplication.class, args);
	}

    @Bean
    public AuditorAware<String> auditorProvider() {
        //TODO: 로그인 기능 구현시 UUID를 사용자 ID로 변경이 필요함
        return () -> Optional.of(UUID.randomUUID().toString());
    }
}
