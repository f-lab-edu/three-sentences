package com.sh.threesentences.users.service;

import com.sh.threesentences.common.enums.OpenType;
import com.sh.threesentences.readingspace.dto.ReadingSpaceRequestDto;
import com.sh.threesentences.readingspace.service.ReadingSpaceService;
import com.sh.threesentences.users.dto.UserRequestDto;
import com.sh.threesentences.users.dto.UserResponseDto;
import com.sh.threesentences.users.entity.User;
import com.sh.threesentences.users.exception.EmailDuplicateException;
import com.sh.threesentences.users.exception.UserNotFoundException;
import com.sh.threesentences.users.repository.UserRepository;
import com.sh.threesentences.utils.PasswordEncoder;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final ReadingSpaceService readingSpaceService;

    /**
     * 사용자를 등록합니다.
     *
     * @param userRequestDto 등록 요청 사용자 정보
     * @return 등록된 사용자 정보
     */
    public UserResponseDto save(UserRequestDto userRequestDto) {
        User user = userRequestDto.toEntity(PasswordEncoder.encrypt(userRequestDto.getPassword()));
        User savedUser = userRepository.save(user);

        readingSpaceService.create(new ReadingSpaceRequestDto("MyReadingSpace", "", OpenType.PUBLIC, null), savedUser);

        return UserResponseDto.fromEntity(savedUser);
    }

    /**
     * 이메일 중복 체크를 합니다. 중복이 발생한 경우 EmailDuplicateException 예외를 던집니다.
     *
     * @param email 중복 체크를 위한 이메일 주소
     */
    public void checkEmailDuplicated(String email) {
        boolean isExisted = userRepository.existsByEmail(email);
        if (isExisted) {
            throw new EmailDuplicateException();
        }
    }

    /**
     * 사용자를 삭제 합니다.
     * is_delete 컬럼을 True로 변경합니다.
     * @param id
     */
    public void delete(long id) {
        findById(id).delete();
    }

    /**
     * 사용자를 조회하고 리턴합니다.
     * @param id 사용자 ID
     * @return UserResponseDto
     */
    public UserResponseDto findUser(Long id) {
        return UserResponseDto.fromEntity(findById(id));
    }

    /**
     * 사용자를 조회합니다.
     * 사용자가 없으면 예외를 던집니다.
     * @param id 사용자 ID
     * @return 사용자 엔티티
     */
    private User findById(long id) {
        return userRepository.findById(id)
            .orElseThrow(UserNotFoundException::new);
    }

    // TODO: 일시적으로 사용자 정보를 얻기 위해 만들어 놓은 메소드. 로그인 기능 이후 삭제
    public User findByIdTemp(long id) {
        return userRepository.findById(id)
            .orElseThrow(UserNotFoundException::new);
    }
}
