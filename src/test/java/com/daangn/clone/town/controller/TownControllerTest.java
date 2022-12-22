package com.daangn.clone.town.controller;

import com.daangn.clone.town.dto.TownDto;
import com.daangn.clone.town.service.TownService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Rollback(value = false)
class TownControllerTest {

    @Autowired
    private TownService townService;

    @Test
    void api5(){
        //given

        //when
        List<TownDto> townDtoList = townService.getAllTown();

        //then
        townDtoList.stream()
                .map(dto -> dto.getTownName())
                .forEach(n -> System.out.println(n));
    }
}