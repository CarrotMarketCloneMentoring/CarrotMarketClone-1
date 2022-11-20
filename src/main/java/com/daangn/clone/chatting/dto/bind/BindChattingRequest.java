package com.daangn.clone.chatting.dto.bind;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class BindChattingRequest {

    @Positive(message = "상품 Id 값은 반드시 양수입니다.")
    @NotNull(message = "어떤 상품으로 부터 시작된 채팅인지, 그 상품 Id 값이 필수 입니다.")
    private Long itemId;
}
