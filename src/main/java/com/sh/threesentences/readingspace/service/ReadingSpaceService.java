package com.sh.threesentences.readingspace.service;

import static com.sh.threesentences.readingspace.exception.ReadingSpaceErrorCode.READING_SPACE_NOT_FOUND;

import com.sh.threesentences.common.enums.OpenType;
import com.sh.threesentences.readingspace.dto.ReadingSpaceRequestDto;
import com.sh.threesentences.readingspace.dto.ReadingSpaceResponseDto;
import com.sh.threesentences.readingspace.entity.ReadingSpace;
import com.sh.threesentences.readingspace.entity.UserReadingSpaceMapping;
import com.sh.threesentences.readingspace.entity.fcc.ReadingSpaceMembers;
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
            -> new IllegalStateException(READING_SPACE_NOT_FOUND.getMessage()));
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
        ReadingSpaceMembers readingSpaceMembers = ReadingSpaceMembers.fromEntity(membersOfReadingSpace);
        readingSpaceMembers.checkSpaceDeleteCondition();

        ReadingSpace readingSpace = membersOfReadingSpace.get(0).getReadingSpace();
        readingSpace.delete();
    }
}
