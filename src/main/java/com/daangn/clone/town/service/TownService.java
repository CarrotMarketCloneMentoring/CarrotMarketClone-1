package com.daangn.clone.town.service;

import com.daangn.clone.town.dto.TownDto;
import com.daangn.clone.town.repository.TownRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TownService {

    private final TownRepository townRepository;


    /** 모든 Town을 조회하여 반환 */
    public List<TownDto> getAllTown(){
        return townRepository.findAll().stream().map(t ->
                TownDto.builder().townId(t.getId()).townName(t.getName()).build())
                .collect(Collectors.toList());
    }


}
