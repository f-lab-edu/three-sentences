package com.sh.threesentences.readingspace.service;

import static com.sh.threesentences.common.exception.ErrorCode.MEMBER_IS_STILL_IN_SPACE;
import static com.sh.threesentences.common.exception.ErrorCode.NO_ADMIN_IN_SPACE;

import com.sh.threesentences.common.enums.OpenType;
import com.sh.threesentences.common.exception.ErrorCode;
import com.sh.threesentences.readingspace.dto.ReadingSpaceRequestDto;
import com.sh.threesentences.readingspace.dto.ReadingSpaceResponseDto;
import com.sh.threesentences.readingspace.entity.ReadingSpace;
import com.sh.threesentences.readingspace.entity.UserReadingSpaceMapping;
import com.sh.threesentences.readingspace.enums.UserRole;
import com.sh.threesentences.readingspace.repository.ReadingSpaceRepository;
import com.sh.threesentences.readingspace.repository.UserReadingSpaceRepository;
import com.sh.threesentences.users.entity.User;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ReadingSpaceService {

    private final ReadingSpaceRepository readingSpaceRepository;

    private final UserReadingSpaceRepository userReadingSpaceRepository;

    public ReadingSpaceResponseDto create(ReadingSpaceRequestDto readingSpaceRequestDto, User user) {
        ReadingSpace savedReadingSpace = readingSpaceRepository.save(readingSpaceRequestDto.toEntity());

        // TODO: 로그인한 사용자의 정보를 조회해서 User 엔티티를 UserReadingSpaceMapping에 추가해줘야함.
        UserReadingSpaceMapping userReadingSpaceMapping = UserReadingSpaceMapping.createUserReadingSpaceMapping(user,
            savedReadingSpace, UserRole.ADMIN);
        userReadingSpaceRepository.save(userReadingSpaceMapping);

        return ReadingSpaceResponseDto.fromEntity(savedReadingSpace);
    }

    public List<ReadingSpace> getPublicReadingSpaces() {
        return readingSpaceRepository.findAllByOpenType(OpenType.PUBLIC);
    }

    public ReadingSpaceResponseDto update(ReadingSpaceRequestDto readingSpaceRequestDto, Long id) {
        ReadingSpace readingSpace = readingSpaceRepository.findById(id).orElseThrow(()
            -> new IllegalStateException(ErrorCode.READING_SPACE_NOT_FOUND.getMessage()));
        readingSpace.update(readingSpaceRequestDto);
        return ReadingSpaceResponseDto.fromEntity(readingSpace);
    }

    public List<ReadingSpace> getMyReadingSpaces() {

        // TODO: 로그인한 사용자의 id를 조회할 수 있게 수정
        Long id = 1L;
        return userReadingSpaceRepository.findByUserId(id)
            .stream()
            .map(UserReadingSpaceMapping::getReadingSpace)
            .collect(Collectors.toList());
    }
    public void delete(Long id) {

        // TODO: 사용자 정보 조회 후, 어드민 권한인 경우에만 삭제 기능을 진행할 수 있게 변경

        List<UserReadingSpaceMapping> membersOfReadingSpace = userReadingSpaceRepository.findByReadingSpaceId(id);
        checkDeleteCondition(membersOfReadingSpace);

        ReadingSpace readingSpace = membersOfReadingSpace.get(0).getReadingSpace();
        readingSpace.delete();
    }

    private void checkDeleteCondition(List<UserReadingSpaceMapping> readingSpaceMembers) {
        if (readingSpaceMembers.size() >= 2 ) {
            // 스페이스가 가입된 사용자가 2명 이상인 경우, 삭제가 불가능
            throw new IllegalStateException(MEMBER_IS_STILL_IN_SPACE.getMessage());
        } else if (readingSpaceMembers.size() == 1) {
            // 스페이스가 가입된 사용자가 1명인데, 해당 계정이 어드민이 아니라면 삭제 불가능. 뭔가 문제가 발생
            if (readingSpaceMembers.get(0).getUserRole() != UserRole.ADMIN) {
                throw new IllegalStateException(NO_ADMIN_IN_SPACE.getMessage());
            }
        } else {
            // 0인 경우, 스페이스 존재하지 않음
            throw new IllegalStateException(ErrorCode.READING_SPACE_NOT_FOUND.getMessage());
        }
    }
}
