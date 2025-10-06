package gwan.zheng.core.game.ws;

/**
 * @Author: 郑国荣
 * @Date: 2025-10-05-17:27
 * @Description:
 */

import com.fasterxml.jackson.databind.ObjectMapper;

import gwan.zheng.core.model.dto.game.WsMessageDto;
import gwan.zheng.core.service.game.RoomService;
import gwan.zheng.service.TokenManager;
import gwan.zheng.utils.UserContext;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class GameWebSocketHandler extends TextWebSocketHandler {
    private final ObjectMapper mapper = new ObjectMapper();
    private final RoomService roomService;
    // 保存 sessionId -> playerId 映射
    private final ConcurrentHashMap<String, Long> sessionToPlayer = new ConcurrentHashMap<>();
    private final TokenManager tokenManager;

    public GameWebSocketHandler(RoomService roomService, TokenManager tokenManager) {
        this.roomService = roomService;
        this.tokenManager = tokenManager;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Long userId = (Long) session.getAttributes().get("userId");
        String token = (String) session.getAttributes().get("token");

        if (userId != null || !tokenManager.validateToken(token)) {
            sessionToPlayer.put(session.getId(), userId);
            System.out.println("WebSocket 建立成功: userId=" + userId + " sessionId=" + session.getId());
        } else {
            System.out.println("WebSocket 建立失败: 未找到 userId");
            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Invalid token"));
        }
    }


    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        WsMessageDto msg = mapper.readValue(message.getPayload(), WsMessageDto.class);
        Long playerId = sessionToPlayer.get(session.getId());

        if (playerId == null) {
            System.err.println("未绑定用户的会话消息: " + message.getPayload());
            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Unauthorized"));
            return;
        }

        switch (msg.getType()) {
            case "JOIN_ROOM":
                roomService.joinRoom(msg.getRoomId(), playerId, session);
                break;
            case "PLAY_CARD":
            case "END_TURN":
            case "ACTION":
                roomService.routeActionToRoom(msg, session);
                break;
            case "LEAVE":
                roomService.leaveRoom(msg.getRoomId(), playerId);
                sessionToPlayer.remove(session.getId());
                session.close();
                break;
            default:
                // 可忽略或记录
        }
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String token = (String) session.getAttributes().get("token");
        System.out.println("WebSocket connected, token=" + token);
        Long playerId = sessionToPlayer.remove(session.getId());
        if (playerId != null) roomService.handleDisconnect(playerId);
    }
}
