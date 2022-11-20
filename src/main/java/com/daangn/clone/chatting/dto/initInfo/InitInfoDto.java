package com.daangn.clone.chatting.dto.initInfo;

import com.daangn.clone.chatting.dto.ChattingListDto;
import com.daangn.clone.chatting.dto.new_content.NewContentDto;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InitInfoDto {

    private ChattingListDto chattingList;
    private List<NewContentDto> isNewMessageList = new ArrayList<>();

}
