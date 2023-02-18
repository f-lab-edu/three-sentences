package com.sh.threesentences.common.config;

import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String userEmail = "";
        if (authentication != null && authentication.isAuthenticated()) {
            userEmail = authentication.getName(); // 로그인한 사용자의 정보를 조회하여 사용자의 이름을 등록자와 수정자로 지정
        }
        return Optional.of(userEmail);
    }
}
