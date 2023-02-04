package com.sh.threesentences.readingspace.service;

import com.sh.threesentences.common.enums.OpenType;
import com.sh.threesentences.readingspace.dto.ReadingSpaceRequestDto;
import com.sh.threesentences.readingspace.dto.ReadingSpaceResponseDto;
import com.sh.threesentences.readingspace.entity.ReadingSpace;
import com.sh.threesentences.readingspace.entity.UserReadingSpaceMapping;
import com.sh.threesentences.readingspace.enums.UserRole;
import com.sh.threesentences.readingspace.repository.ReadingSpaceRepository;
import com.sh.threesentences.readingspace.repository.UserReadingSpaceRepository;
import com.sh.threesentences.users.entity.User;
import java.util.List;
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
            -> new IllegalStateException("존재하지 않는 ReadingSpace 입니다."));
        readingSpace.update(readingSpaceRequestDto);
        return ReadingSpaceResponseDto.fromEntity(readingSpace);

    }
}
