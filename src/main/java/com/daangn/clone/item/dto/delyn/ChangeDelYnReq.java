package com.daangn.clone.item.dto.delyn;

import com.daangn.clone.common.enums.DelYn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
@NoArgsConstructor
public class ChangeDelYnReq {

    @NotNull(message = "DelYn값을 수정하고자 하는 Item의 id값은 필수로 넘겨주셔야 합니다.")
    private Long itemId;

    @NotNull(message = "어떤 DelYn 상태로 수정할지에 대한 값은 필수로 넘겨주셔야 합니다.")
    private DelYn delYn;
}
