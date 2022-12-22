package com.daangn.clone.common.response;

import lombok.Getter;

/**
 * [ 1000단위 ] - 오류의 범위
 *  1000 : 요청 성공
 *  2 : Request 오류
 *  3 : Reponse 오류
 *  4 : DB, Server 오류
 *
 * [ 100단위 ] - 오류 도메인
 *  0 : 공통 오류
 *  1 : member 오류
 *  2 : item 오류
 *  3 : itemImage 오류
 *  4 : town 오류
 *  5 : category 오류
 *  6 : wish 오류
 *  7 : chattingRoom 오류
 *  8 : chattingContent 오류
 *  9 : ChattingMember 오류
 *
 *
 * [10단위] - 오류 HTTP Method
 *  0~19 : Common
 *  20~39 : GET
 *  40~59 : POST
 *  60~79 : PATCH
 *  80~99 : else
 *
 *  [1 단위] - 그외 오류의 고유 식별자
 *          - 순서대로 설정해주면 됨
 *

/**  [ApiResponse 로 나갈 값들을 - 상황에 따른 열거형 값으로 미리 선언해 놓고 - 가져다 쓰는 형태를 위해서 사용]*/
@Getter
public enum ApiResponseStatus {

    /**
     * 1000 : 요청 성공
     * */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),

    /**
     * 2000 : Request 오류
     * */
    //FAILTOFIND(false, 2001, "FIND에 실패하였습니다."),
    //FAILTOPOST(false, 2002, "POST에 실패하였습니다."),
    //FAILTOUPDATE(false, 2003, "UPDATE에 실패하였습니다."),

    VALIDATION_FAIL(false,2000, "요청한 값의 검증 로직에서 오류가 발견되었습니다."),
    // 매 Request의 헤더에 임시적인 유효성 검사를 위해 username을 달아서 보낼 텐데 - 그 헤더의 username이 없는 username일때의 Status
    INVALID_MEMBER(false, 2001, "유효하지 않은 사용자 입니다."),
    FAIL_CREATE_AES128(false, 2002, "AES128 인스턴스 생성에 실패했습니다."),

    NO_USERNAME(false, 2003, "헤더의 username 필드에 값이 없습니다."),
    NO_ITEM_ID(false, 2004, "쿼리파라미터로 와야 할 itemId 값이 없습니다."),
    NO_PATH(false, 2005, "쿼리파라미터로 와야 할 ItemImage의 path 값이 없습니다."),
    INVALID_OFFSET(false, 2006, "offset 값은 0 이상인 정수깂 이어야 합니다."),
    INVALID_LIMIT(false, 2007, "limit 값은 자연수 값이어야 합니다."),
    INVALID_ENUM(false, 2008, "정의되지 않은 enum 값이 들어왔습니다."),
    FAIL_ENCRYPT(false, 2009, "AES128 암호화 ㄷ과정에서 예와가 발생하였습니다."),
    FAIL_DECRYPT(false, 2010, "AES128 복호화 과정에서 예외가 발생하였습니다."),
    NO_JWT_TOKEN(false, 2011, "인증 인가를 위한 jwt token값이 존재하지 않습니다."),
    INVALID_JWT_TOKEN(false, 2012, "인증 인가를 위한 jwt token값이 유효하지 않습니다."),
    INVALID_TOWN_NAME(false, 2013, "유효하지 않는 행정동 입니다"),
    INVALID_CHATTING_ROOM(false, 2021, "로컬에 저장된 채팅룸 데이터는 잘못된 데이터 입니다."),

    NESTED_USERNAME(false, 2101, "이미 사용중인 아이디 입니다."),
    NESTED_NICKNAME(false, 2102, "이미 사용중인 닉네임 입니다."),


    INVALID_USERNAME_OR_PASSWORD(false, 2122, "아이디 또는 비밀번호가 잘못되었습니다."),
    INVALID_MEMBER_STATUS(false, 2123, "이미 탈퇴한 회원입니다."),


    NOT_MATCHED_PASSWORD_RULE(false, 2141, "비밀번호는 영문, 숫자, 특수문자로 이루어진 8~20자리 문자열 이어야 합니다."),




    FAIL_GET_ITEM(false, 2221, "상품 조회에 실패했습니다."),
    FAIL_GET_ITEM_IMAGE(false, 2222, "상품 이미지 조회에 실패했습니다."),
    FAIL_GET_ITEM_LIST(false, 2223, "상품 목록 조회에 실패했습니다."),
    MAX_FILE_SIZE_EXCEEDED(false, 2301, "각 이미지의 사이즈는 최대 15MB 사이즈로 제한됩니다."),
    MAX_REQUEST_SIZE_EXCEEDED(false, 2302, "최대 업로드 할 수 있는 파일의 총 사이즈는 100MB로 제한됩니다."),

    INVALID_FILE_EXT(false, 2303, "지원하는 이미지 파일 형식은 jpeg, png 형식으로 제한됩니다."),

    FAIL_REGISTER_ITEM(false, 2241, "상품 등록에 실패했습니다."),
    INVALID_ITEM_FOR_BUY(false, 2242, "이 상품은 구매할수 없는 상품입니다."),


    INVALID_CATEGORY(false, 2501, "유효하지 않는 카테고리 입니다."),
    INVALID_ITEM_ID(false, 2503, "유효하지 않은 Item Id 입니다"),
    INVALID_ITEM_IMAGE_PATH(false, 2504, "유효하지 않은 상품 이미지 경로 입니다."),
    NO_ITEMLIST(false, 2505, "더이상 등록된 Item이 없습니다."),

    INVALID_DELYN(false, 2261, "구매 확정 으로 상품 상태 정보를 변경하고자 한다면 다른 API를 호출하셔야 합니다."),
    INVALID_CHANGE_DELYN(false, 2262, "이 사용자는 해당 아이템의 DelYn 상태를 변경할 수 없습니다."),
    INVALID_PREV_SITUATION(false,2263, "상품 상태 변경시에 있어, 이전 상품 상태가 유효하지 않습니다."),
    INVALID_RESERVE_MEMBER(false, 2264, "상품 상태 변경시에 있어, 예약자가 유효하지 않습니다."),
    INVALID_BUYER_MEMBER(false, 2265, "상품 상태 변경시에 있어, 구매자 유효하지 않습니다."),
    FAIL_CHANGE_TO_SOLD_OUT(false, 2266, "상품 상태 변경시에 있어, 판매 완료로 변경하는데 실패했습니다."),


    FAIL_CREATE_CHATTING_ROOM(false, 2741, "채팅을 위한 채팅 룸 생성에 실패했습니다."),
    INVALID_ROOM_INFO(false, 2742, "채팅룸 및 참여자 정보가 잘못되었습니다."),


    FAIL_SAVE_CHATTING(false, 2801, "채팅 데이터를 저장하는데 실패했습니다."),

    INVALID_SEND_MESSAGE(false, 2841, "메세지를 보낼 채팅 룸 ID or 송신자 Member ID가 잘못되었습니다."),
    SAME_SENDER_RECEIVER(false, 2842, "메세지를 보내는 송신자와 수신자가 같을수는 없습니다."),
    FAIL_ENTER_CHATTING_ROOM(false,2861, "채팅방 입장 여부를 나타내는 MemberChatting의 InRoomYn 값을 업데이트 하는데 실패하여 , 채팅방 입장.퇴장에 실패하였습니다."),

    NOT_PARTICIPATE_MEMBER(false, 2901, "해당 사용자는 해당 채팅방에 참여하고 있는 멤버가 아닙니다."),

    /**
     * 3000 : Response 오류
     * */

    /**
     * 4000 : Database, Server 오류
     * */

    FAIL_SAVE_IMAGE(false, 4241, "로컬에 사진을 저장하는데 실패했습니다."),
    FAIL_LOAD_IMAGE(false, 4242, "로컬에 있는 사진을 로드하는데 실패했습니다."),
    FAIL_MAKE_DIR_FOR_ITEM(false, 4243, "로컬에 해당 상품의 이미지를 저장할 디렉터리를 생성하는데 실패했습니다."),
    FAIL_DEL_DIR_FOR_ITEM(false, 4244, "상품 사진 저장 실패에 의해 , 해당 상품의 이미지를 저장할 디렉터리를 삭제해야 하는데, 이에 실패했습니다.");




    private final boolean isSuccess;
    private final int code;
    private final String message;

    /**
     * ApiResponseStatus에서 각 해당하는 코드를 생성자로 맵핑
     * [열겨형의 생성자 - 반드시 private]
     * <이렇게 열거형 생성자를 정의하면 - 열거형 값의 선언시 , 소괄호를 통해 생성자에 인자를 전달할 수 있음 >
     *     : 그렇게 되면 결과적으로는 열거형 타입의 (열거형도 class) 객체가 생성되고 - 그 객체의 필드가 소괄호에 인자로 전달한 값 대로 초기화되는것!
     * */
    private ApiResponseStatus(boolean isSuccess, int code, String message){
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }

}
