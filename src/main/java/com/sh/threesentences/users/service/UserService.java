package com.sh.threesentences.users.service;

import com.sh.threesentences.users.dto.UserRequestDto;
import com.sh.threesentences.users.dto.UserResponseDto;
import com.sh.threesentences.users.entity.Users;
import com.sh.threesentences.users.repository.UserRepository;
import com.sh.threesentences.utils.PasswordEncoder;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponseDto save(UserRequestDto userRequestDto) {
        Users user = new Users(userRequestDto.getEmail(),
            userRequestDto.getName(),
            PasswordEncoder.encrypt(userRequestDto.getPassword()),
            userRequestDto.getMembership());
        Users savedUser = userRepository.save(user);

        return savedUser.toUserResponseDto();
    }

    public void checkEmailDuplicated(String email) {
        Optional<String> findEmail = userRepository.findByEmail(email);
        if (findEmail.isPresent()) {
            throw new RuntimeException();
        }

    }
}
