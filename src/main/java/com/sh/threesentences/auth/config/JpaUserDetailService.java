package com.sh.threesentences.auth.config;

import com.sh.threesentences.users.entity.User;
import com.sh.threesentences.users.exception.UserNotFoundException;
import com.sh.threesentences.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JpaUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
            .orElseThrow(UserNotFoundException::new);

        return new SecurityUser(user.getEmail(), user.getPassword(), user.getAuthorityType());
    }
}
