package com.sh.threesentences.readingspace.service;

import static com.sh.threesentences.readingspace.exception.ReadingSpaceErrorCode.DELETE_ADMIN_ONLY;
import static com.sh.threesentences.readingspace.exception.ReadingSpaceErrorCode.MEMBER_IS_STILL_IN_SPACE;
import static com.sh.threesentences.readingspace.exception.ReadingSpaceErrorCode.READING_SPACE_NOT_FOUND;

import com.sh.threesentences.common.enums.OpenType;
import com.sh.threesentences.readingspace.dto.ReadingSpaceRequestDto;
import com.sh.threesentences.readingspace.dto.ReadingSpaceResponseDto;
import com.sh.threesentences.readingspace.entity.ReadingSpace;
import com.sh.threesentences.readingspace.entity.ReadingSpaceMemberRole;
import com.sh.threesentences.readingspace.entity.fcc.ReadingSpaceMembers;
import com.sh.threesentences.readingspace.enums.UserRole;
import com.sh.threesentences.readingspace.repository.ReadingSpaceRepository;
import com.sh.threesentences.readingspace.repository.UserReadingSpaceRepository;
import com.sh.threesentences.users.entity.User;
import com.sh.threesentences.users.exception.UserNotFoundException;
import com.sh.threesentences.users.repository.UserRepository;
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

    private final UserRepository userRepository;

    public ReadingSpaceResponseDto create(ReadingSpaceRequestDto readingSpaceRequestDto, String email) {

        User user = userRepository.findByEmail(email)
            .orElseThrow(UserNotFoundException::new);

        ReadingSpace savedReadingSpace = createReadingSpace(
            readingSpaceRequestDto, user);

        return ReadingSpaceResponseDto.fromEntity(savedReadingSpace);
    }

    private ReadingSpace createReadingSpace(ReadingSpaceRequestDto readingSpaceRequestDto, User user) {
        ReadingSpace savedReadingSpace = readingSpaceRepository.save(readingSpaceRequestDto.toEntity());

        ReadingSpaceMemberRole readingSpaceMemberRole = new ReadingSpaceMemberRole(user,
            savedReadingSpace, UserRole.ADMIN);
        userReadingSpaceRepository.save(readingSpaceMemberRole);
        return savedReadingSpace;
    }

    /**
     * 회원 등록시에 기본적으로 함께 만들어지는 Space를 생성한다.
     *
     * @param readingSpaceRequestDto 생성 요청 DTO
     * @param user                   사용자
     * @return 생성된 Space 응답값
     */
    public ReadingSpaceResponseDto createInitialSpace(ReadingSpaceRequestDto readingSpaceRequestDto, User user) {

        ReadingSpace savedReadingSpace = createReadingSpace(
            readingSpaceRequestDto, user);

        return ReadingSpaceResponseDto.fromEntity(savedReadingSpace);
    }

    public List<ReadingSpaceResponseDto> getPublicReadingSpaces() {
        List<ReadingSpace> allPublicReadingSpaces = readingSpaceRepository.findAllByOpenType(OpenType.PUBLIC);

        return allPublicReadingSpaces
            .stream()
            .map(ReadingSpaceResponseDto::fromEntity)
            .collect(Collectors.toList());
    }

    public ReadingSpaceResponseDto update(ReadingSpaceRequestDto readingSpaceRequestDto, Long id) {
        ReadingSpace readingSpace = readingSpaceRepository.findById(id).orElseThrow(()
            -> new IllegalStateException(READING_SPACE_NOT_FOUND.getMessage()));
        readingSpace.update(readingSpaceRequestDto);
        return ReadingSpaceResponseDto.fromEntity(readingSpace);
    }

    public List<ReadingSpaceResponseDto> getMyReadingSpaces(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(UserNotFoundException::new);
        return userReadingSpaceRepository.findByUserId(user.getId())
            .stream()
            .map(ReadingSpaceMemberRole::getReadingSpace)
            .map(ReadingSpaceResponseDto::fromEntity)
            .collect(Collectors.toList());
    }

    public void delete(Long id, String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(UserNotFoundException::new);

        ReadingSpaceMemberRole spaceMemberRole = userReadingSpaceRepository.findByUserAndReadingSpaceId(user, id)
            .orElseThrow(() -> new IllegalStateException(READING_SPACE_NOT_FOUND.getMessage()));

        if (!spaceMemberRole.isAdmin()) {
            throw new IllegalStateException(DELETE_ADMIN_ONLY.getMessage());
        }

        int memberCount = userReadingSpaceRepository.countByReadingSpaceId(id);
        if (hasMembersGreaterThanOne(memberCount)) {
            throw new IllegalStateException(MEMBER_IS_STILL_IN_SPACE.getMessage());
        }

        List<ReadingSpaceMemberRole> membersOfReadingSpace = userReadingSpaceRepository.findByReadingSpaceId(id);
        ReadingSpaceMembers readingSpaceMembers = ReadingSpaceMembers.fromEntity(membersOfReadingSpace);
        readingSpaceMembers.checkSpaceDeleteCondition();

        ReadingSpaceMemberRole readingSpaceMemberRole = membersOfReadingSpace.get(0);
        readingSpaceMemberRole.delete();
        readingSpaceMemberRole.getReadingSpace().delete();

    }

    private boolean hasMembersGreaterThanOne(int memberCount) {
        return memberCount >= 2;
    }
}
