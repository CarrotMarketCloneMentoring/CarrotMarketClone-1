package com.daangn.clone.town.controller;

import com.daangn.clone.town.Town;
import com.daangn.clone.town.dto.TownDto;
import com.daangn.clone.town.repository.TownRepository;
import com.daangn.clone.town.service.TownService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


public class TownControllerTest {

    @InjectMocks
    private TownService townService;

    @Mock
    private TownRepository townRepository;

    @Test
    void api5() throws Exception{
        //given
        Town town1 = new Town("서울특별시_광진구_중곡제1동");
        Town town2 = new Town("서울특별시_광진구_중곡제2동");
        Town town3 = new Town("서울특별시_광진구_중곡제3동");
        Town town4 = new Town("서울특별시_광진구_중곡제4동");
        Town town5 = new Town("서울특별시_광진구_능동");
        Town town6 = new Town("서울특별시_광진구_구의제1동");
        Town town7 = new Town("서울특별시_광진구_구의제2동");
        Town town8 = new Town("서울특별시_광진구_구의제3동");
        Town town9 = new Town("서울특별시_광진구_광장동");
        Town town10 = new Town("서울특별시_광진구_자양제1동");
        Town town11 = new Town("서울특별시_광진구_자양제2동");
        Town town12 = new Town("서울특별시_광진구_자양제3동");
        Town town13 = new Town("서울특별시_광진구_자양제4동");
        Town town14 = new Town("서울특별시_광진구_화양동");
        Town town15 = new Town("서울특별시_광진구_군자동");
        Town town16 = new Town("서울특별시_동작구_노량진제1동");
        Town town17 = new Town("서울특별시_동작구_노량진제2동");
        Town town18 = new Town("서울특별시_동작구_상도제1동");
        Town town19 = new Town("서울특별시_동작구_상도제2동");
        Town town20 = new Town("서울특별시_동작구_상도제3동");
        Town town21 = new Town("서울특별시_동작구_상도제4동");
        Town town22 = new Town("서울특별시_동작구_흑석동");
        Town town23 = new Town("서울특별시_동작구_사당제1동");
        Town town24 = new Town("서울특별시_동작구_사당제2동");
        Town town25 = new Town("서울특별시_동작구_사당제3동");
        Town town26 = new Town("서울특별시_동작구_사당제4동");
        Town town27 = new Town("서울특별시_동작구_사당제5동");
        Town town28 = new Town("서울특별시_동작구_대방동");
        Town town29 = new Town("서울특별시_동작구_신대방제1동");
        Town town30 = new Town("서울특별시_동작구_신대방제2동");

        List<Town> townList = List.of(town1, town2, town3, town4, town5, town6, town7, town8, town9, town10,
                town11, town12, town13, town14, town15, town16, town17, town18, town19, town20,
                town21, town22, town23, town24, town25, town26, town27, town28, town29, town30);

        when(townRepository.findAll()).thenReturn(townList);

        //when
        List<TownDto> townDtoList = townService.getAllTown();

        //then
        assertThat(townDtoList).extracting("townName").containsExactly(
                "서울특별시_광진구_중곡제1동", "서울특별시_광진구_중곡제2동", "서울특별시_광진구_중곡제3동"  , "서울특별시_광진구_중곡제4동",
                "서울특별시_광진구_능동", "서울특별시_광진구_구의제1동", "서울특별시_광진구_구의제2동", "서울특별시_광진구_구의제3동",
                "서울특별시_광진구_광장동", "서울특별시_광진구_자양제1동", "서울특별시_광진구_자양제2동", "서울특별시_광진구_자양제3동",
                "서울특별시_광진구_자양제4동", "서울특별시_광진구_화양동", "서울특별시_광진구_군자동", "서울특별시_동작구_노량진제1동",
                "서울특별시_동작구_노량진제2동", "서울특별시_동작구_상도제1동", "서울특별시_동작구_상도제2동","서울특별시_동작구_상도제3동",
                "서울특별시_동작구_상도제4동", "서울특별시_동작구_흑석동", "서울특별시_동작구_사당제1동", "서울특별시_동작구_사당제2동",
                "서울특별시_동작구_사당제3동", "서울특별시_동작구_사당제4동", "서울특별시_동작구_사당제5동", "서울특별시_동작구_대방동",
                "서울특별시_동작구_신대방제1동", "서울특별시_동작구_신대방제2동"
        );

    }
}
