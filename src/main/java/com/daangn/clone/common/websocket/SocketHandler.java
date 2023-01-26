package com.daangn.clone.common.websocket;

import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@Component
@RequiredArgsConstructor
public class SocketHandler extends TextWebSocketHandler {

    private Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();
    private List<String> sessionIdList = new ArrayList<>();

    /** 각 sessino별로 참여하고 있는 ChattingRoom은 여러개!  */
    private Map<String, List<Long>> roomMap = new ConcurrentHashMap<>();




    @Override
    public void afterConnectionEstablished(WebSocketSession session)  {
        sessionMap.put(session.getId(), session);
        sessionIdList.add(session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {


        sessionIdList.remove(session.getId());
        sessionMap.remove(session.getId());


        /** (2) 비 정상적인 종료일 경우 */
        if(!status.equalsCode(CloseStatus.NORMAL)) {
            roomMap.remove(session.getId());
        }

    }


    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        String role = getRole(message);
        long chattingRoomId = getChattingRoomId(message);
        String content = getContent(message);

        /** 처음 연결 시, 해당 user가 참여하고 있는 모든 chattingRoom의 Id 들 */
        long[] roomIdList = getRoomIdList(getRoomIdList(message));

        if(role.equals("ENTER")){
            List<Long> chattingRoomIdList = new ArrayList<>();

            Arrays.stream(roomIdList)
                    .forEach(rId -> chattingRoomIdList.add(rId));

            roomMap.put(session.getId(), chattingRoomIdList);
        }
        else if(role.equals("EXIT")){
            roomMap.remove(session.getId());
            sessionMap.get(session.getId()).close(CloseStatus.NORMAL);
        }
        else if(role.equals("SEND")){

            for (String sId : roomMap.keySet()) {

                /**
                 * 1) 해당 chattingRoom에 참여하고 있는 session들에 한하여
                 * 2) 동시에 그 session이 보내는 자신과는 다른 session인 경우에 한하여
                 * */

                if(roomMap.get(sId).contains(chattingRoomId) && !sId.equals(session.getId())){
                    //메세지를 JSON 형태로 가공해서
                    JSONObject contentJson = getContentJson(chattingRoomId, content);
                    //찾아낸 그 상대 세션에게 전송
                    sessionMap.get(sId).sendMessage(new TextMessage(contentJson.toString()));

                    break;
                }
            }
        }

    }


    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        /** 메세지가 안보내진 상황을 감지해서 -> 다시 보내라고 직접 그 세션에 응답을 보내줘야 함 */
    }

    private JSONObject getContentJson(Long chattingRoomId, String content) throws JSONException{
        Map<String, String> map = new HashMap<>();
        map.put("chattingRoomId", chattingRoomId.toString());
        map.put("content", content);

        return new JSONObject(map);
    }


    private JSONObject getPayloadJson(TextMessage message) throws JSONException {
        Object payloadObj = message.getPayload();
        String payload = payloadObj.toString();
        return new JSONObject(payload);
    }

    private String getRole(TextMessage message) throws JSONException {
        return getPayloadJson(message).getString("role");
    }


    private long getChattingRoomId(TextMessage message) throws JSONException {
        return getPayloadJson(message).getLong("chattingRoomId");
    }

    private String getContent(TextMessage message) throws JSONException{
        return getPayloadJson(message).getString("content");
    }

    private String getRoomIdList(TextMessage message) throws JSONException{
        return getPayloadJson(message).getString("roomIdList");
    }

    private long[] getRoomIdList(String str){
        String[] idList = str.substring(1, str.length() - 1).split(",");
        long[] roomIdList = new long[idList.length];


        for(int i=0; i<idList.length; i++){
            roomIdList[i] = Long.parseLong(idList[i]);
        }

        return roomIdList;
    }
}
