package com.sh.threesentences.users.service;

import com.sh.threesentences.users.dto.UserRequestDto;
import com.sh.threesentences.users.dto.UserResponseDto;
import com.sh.threesentences.users.entity.Users;
import com.sh.threesentences.users.exception.EmailDuplicateException;
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

    /**
     * 사용자를 등록합니다.
     *
     * @param userRequestDto 등록 요청 사용자 정보
     * @return 등록된 사용자 정보
     */
    public UserResponseDto save(UserRequestDto userRequestDto) {
        Users user = userRequestDto.toEntity(PasswordEncoder.encrypt(userRequestDto.getPassword()));
        Users savedUser = userRepository.save(user);

        return savedUser.toUserResponseDto();
    }

    /**
     * 이메일 중복 체크를 합니다. 중복이 발생한 경우 EmailDuplicateException 예외를 던집니다.
     *
     * @param email 중복 체크를 위한 이메일 주소
     */
    public void checkEmailDuplicated(String email) {
        Optional<String> findEmail = userRepository.findByEmail(email);
        if (findEmail.isPresent()) {
            throw new EmailDuplicateException();
        }
    }
}
