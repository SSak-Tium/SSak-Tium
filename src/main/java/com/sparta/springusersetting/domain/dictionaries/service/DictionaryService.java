package com.sparta.springusersetting.domain.dictionaries.service;

import com.sparta.springusersetting.domain.dictionaries.controller.DictionaryController;
import com.sparta.springusersetting.domain.dictionaries.dto.request.DictionaryRequestDto;
import com.sparta.springusersetting.domain.dictionaries.dto.request.DictionaryUpdateRequestDto;
import com.sparta.springusersetting.domain.dictionaries.dto.response.DictionaryListResponseDto;
import com.sparta.springusersetting.domain.dictionaries.dto.response.DictionaryResponseDto;
import com.sparta.springusersetting.domain.dictionaries.entitiy.Dictionaries;
import com.sparta.springusersetting.domain.dictionaries.exception.NotFoundDictionaryException;
import com.sparta.springusersetting.domain.dictionaries.repository.DictionaryRepository;
import com.sparta.springusersetting.domain.users.entity.User;
import com.sparta.springusersetting.domain.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DictionaryService {

    private final DictionaryRepository dictionaryRepository;
    private final UserService userService;

    // 식물사전 등록
    public DictionaryResponseDto createDictionary(long userId, DictionaryRequestDto dictionaryRequestDto) {
        // 유저 조회
        User user = userService.findUser(userId);

        // Entity 생성
        Dictionaries dictionaries = new Dictionaries(dictionaryRequestDto, user.getUserName());

        // DB 저장
        dictionaryRepository.save(dictionaries);

        // Dto 반환
        return new DictionaryResponseDto(dictionaries.getTitle(), dictionaries.getContent(), dictionaries.getUserName());
    }

    // 식물사전 단건 조회
    @Transactional(readOnly = true)
    public DictionaryResponseDto getDictionary(long userId, long dictionaryId) {
        // 유저 조회
        userService.findUser(userId);

        // 식물사전 조회
        Dictionaries dictionaries = findDictionary(dictionaryId);

        // Dto 반환
        return new DictionaryResponseDto(dictionaries.getTitle(), dictionaries.getContent(), dictionaries.getUserName());
    }

    // 식물사전 리스트 조회
    @Transactional(readOnly = true)
    public Page<DictionaryListResponseDto> getDictionaryList(int page, int size) {
        // Pageable 객체 생성
        Pageable pageable = PageRequest.of(page - 1, size);

        // 식물사전 전체 조회
        Page<Dictionaries> dictionariesPage = dictionaryRepository.findAll(pageable);

        // Dto 변환
        Page<DictionaryListResponseDto> dtoPage = dictionariesPage.map(dictionaries -> {
           DictionaryListResponseDto dictionaryListResponseDto = new DictionaryListResponseDto(dictionaries.getTitle(), dictionaries.getUserName());
           return dictionaryListResponseDto;
        });
        return dtoPage;
    }

    // 식물사전 수정
    public DictionaryResponseDto updateDictionary(long userId, DictionaryUpdateRequestDto dictionaryUpdateRequestDto, long dictionaryId) {
        // 유저 조회
        userService.findUser(userId);

        // 식물사전 조회
        Dictionaries dictionaries = findDictionary(dictionaryId);

        // Entity 수정
        dictionaries.update(dictionaryUpdateRequestDto);

        // DB 저장
        dictionaryRepository.save(dictionaries);

        // DTO 반환
        return new DictionaryResponseDto(dictionaries.getTitle(), dictionaries.getContent(), dictionaries.getUserName());
    }

    // 식물사전 삭제
    public String deleteDictionary(long userId, long dictionaryId) {
        // 유저 조회
        userService.findUser(userId);

        // 식물사전 조회
        Dictionaries dictionaries = findDictionary(dictionaryId);

        // DB 삭제
        dictionaryRepository.delete(dictionaries);

        return "정상적으로 삭제되었습니다.";
    }

    // 식물사전 조회 메서드
    public Dictionaries findDictionary(long dictionaryId) {
        return dictionaryRepository.findById(dictionaryId).orElseThrow(NotFoundDictionaryException::new);
    }



}