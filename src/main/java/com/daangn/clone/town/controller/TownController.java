package com.daangn.clone.town.controller;

import com.daangn.clone.common.response.ApiResponse;
import com.daangn.clone.town.dto.TownDto;
import com.daangn.clone.town.service.TownService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TownController {
    private final TownService townService;


    /** [API.5] : 모든 town 조회 */
    @GetMapping("/town/all")
    public ApiResponse<List<TownDto>> getAllTown(){
        return ApiResponse.success(townService.getAllTown());
    }


}
