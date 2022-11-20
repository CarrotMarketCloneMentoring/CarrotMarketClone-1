package com.daangn.clone.chatting.controller;

import com.daangn.clone.chatting.dto.ChattingDto;
import com.daangn.clone.chatting.dto.ChattingListDto;
import com.daangn.clone.chatting.dto.bind.BindChattingRequest;
import com.daangn.clone.chatting.dto.initInfo.InitInfoDto;
import com.daangn.clone.chatting.dto.last_read.LastReadDto;
import com.daangn.clone.chatting.dto.last_read.LastReadRequest;
import com.daangn.clone.chatting.dto.new_content.NewContentDto;
import com.daangn.clone.chatting.dto.polling.*;
import com.daangn.clone.chatting.dto.receive.ReceiveDto;
import com.daangn.clone.chatting.dto.receive.ReceiveRequest;
import com.daangn.clone.chatting.dto.ChattingContentDto;
import com.daangn.clone.chatting.dto.send.SendDto;
import com.daangn.clone.chatting.dto.send.SendRequest;
import com.daangn.clone.chatting.chattingroom.service.ChattingRoomService;
import com.daangn.clone.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ChattingController {

    private final ChattingRoomService chattingRoomService;


    /**
     * [API 13] : 구매를 위한 채팅 요청 -> 어디까지나 해당 요청에 의한 채팅방 정보만 넘겨주는 기능
     *
     * case 1. 해당 아이템에 해당 사용자가 채팅을 요청한적이 없는 경우 (사실 이 여부만 check해주면 됨 )
     *          -> 새로운 ChattingRoom을 새로 만들어서 그 정보를 반환
     *
     * case 2. 해당 아이템에 대해 , 요청을 보내는 사용자가 과거에 채팅을 시도했었어서 ~> 이전에 생성된 ChattingRoom이 존재하는 경우
     *          -> 그 ChattingRoom을 조회하여 해당 정보를 반환
     * */
   @PostMapping("/chat")
    public ApiResponse<ChattingDto> startChatting(@RequestAttribute Long memberId,
                                                  @Validated @RequestBody BindChattingRequest bindChattingRequest){
        return ApiResponse.success(chattingRoomService.bindChatting(memberId, bindChattingRequest.getItemId()));
    }

    /**
     * [API 14] : 내가 참여한 모든 채팅방 조회 -> 어디까지나 내가 참여한 채팅방들 정보만 넘겨주는 기능
     * -> 하단의 채팅 탭을 눌렀을 때 , 자신이 Seller로든 + EXPECTED_BUYER로든 참여한 모든 채팅방 정보를 리스트로 반환
     * -> 여기서 넘어가는 ChattingRoom의 정렬 기준은 - 생성일자를 기준으로 내림차순 (즉 생성된 최신순)
     *
     * <리팩터링 결과>
     * => 다 가져와서 스트림으로 필터링 하지 않고
     * => 특정 ChattingRoom들만 쿼리 한방으로 가져와서 , dto로 converting 하도록 수정함
     * ( ChattingRoom에 sellerMemberId와 buyerMemberId를 둔 역정규화의 결과 )
     * */
    @GetMapping("/chatList")
    public ApiResponse<ChattingListDto> getChattingList(@RequestAttribute Long memberId){
        return ApiResponse.success(chattingRoomService.getChattingList(memberId));
    }

    /**
     * [API 15] : 특정 ChattingRoom의 특정 targetMember에게 메세지를 보내는 기능
     * */
    @PostMapping("/chat/content")
    public ApiResponse<ChattingContentDto> postChattingContent(@RequestAttribute Long memberId,
                                                               @Validated @RequestBody SendRequest sendRequest){
        return ApiResponse.success(chattingRoomService.sendMessage(memberId,
                SendDto.builder()
                        .chattingRoomId(sendRequest.getChattingRoomId())
                        .targetMemberId(sendRequest.getTargetMemberId())
                        .content(sendRequest.getContent())
                        .build()));
    }

    /**
     * [API 16] : 특정 ChattingRoom에 참여중인 특정 targetMember에게 온 메세지를 넘겨주는 기능 (즉 메세지 수신)
     * => 이때 넘겨주는 메세지는 , 해당 targetMember가 마지막으로 읽은 메세지 이후로 온 메세지를 전부 넘겨준다
     * */

    @PatchMapping("/chat/content")
    public ApiResponse<List<ChattingContentDto>> getChattingContent(@RequestAttribute Long memberId,
                                                            @Validated @RequestBody ReceiveRequest receiveRequest){
        return ApiResponse.success(chattingRoomService.receiveMessage(memberId,
                ReceiveDto.builder()
                        .chattingRoomId(receiveRequest.getChattingRoomId())
                        .lastReadContentId(receiveRequest.getLastReadContentId())
                        .page(receiveRequest.getPage())
                        .limit(receiveRequest.getLimit())
                        .build()));
    }

    /** [API 17] : 특정 ChattingRoom에 새로운 메세지 도착 여부 확인 기능 (Polling) */

    @GetMapping("/chat/content/new")
    public ApiResponse<NewContentDto> isNewMessage(@RequestAttribute Long memberId,
                                                   @ModelAttribute NewContentRequest newContentRequest){
        return ApiResponse.success(chattingRoomService.isNewMessage(memberId, newContentRequest.getChattingRoomId()));
    }


    /** [API 18] : 상대방이 어디까지 메세지를 읽었는지를 확인하는 기능 */
    @GetMapping("/chat/content/last")
    public ApiResponse<LastReadDto> getLastReadContentId(@ModelAttribute LastReadRequest lastReadRequest){
        return ApiResponse.success(chattingRoomService
                .getLastReadContentId(lastReadRequest.getChattingRoomId(), lastReadRequest.getTargetMemberId()));
    }


    /** [API 19] : 로그인 직후 , 해당 사용자가 참여하고 있는 모든 ChattingRoom의 정보를 조회하고 , 각 Room에 새로운 메세지가 왔는지의 여부를 확인*/
    @GetMapping("/chat/initinfo")
    public ApiResponse<InitInfoDto> getAllChatInfo(@RequestAttribute Long memberId){
        return ApiResponse.success(chattingRoomService.getChatInitInfo(memberId));
    }





}
