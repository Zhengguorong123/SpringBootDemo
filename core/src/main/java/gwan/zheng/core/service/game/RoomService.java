package gwan.zheng.core.service.game;

/**
 * @Author: 郑国荣
 * @Date: 2025-10-05-17:31
 * @Description:
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import gwan.zheng.core.model.dto.game.WsMessageDto;
import gwan.zheng.core.model.entity.game.Player;
import gwan.zheng.core.model.entity.game.Room;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.concurrent.*;

@Service
public class RoomService {
    private final ConcurrentHashMap<String, Room> rooms = new ConcurrentHashMap<>();
    private final ObjectMapper mapper = new ObjectMapper();
    private final GameEngine gameEngine;

    public RoomService(@Lazy GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    public Room createRoom(String roomId) {
        Room r = new Room();
        r.setId(roomId);
        r.setLastActiveAt(System.currentTimeMillis());
        // ❌ 不要再 put
        // rooms.put(roomId, r);
        return r;
    }


    public void joinRoom(String roomId, Long playerId, WebSocketSession session) throws Exception {
        // computeIfAbsent 内部会自动调用 createRoom，并放入 rooms
        Room r = rooms.computeIfAbsent(roomId, this::createRoom);

        Player p = new Player();
        p.setId(playerId);
        p.setSession(session);
        p.setHp(30);

        r.getPlayers().put(playerId, p);
        WsMessageDto wsMessageDto = new WsMessageDto();
        wsMessageDto.setMessage("Player " + playerId + " joined the room.");
        broadcastState(r, wsMessageDto);
        System.out.println("Player " + playerId + " joined room " + roomId);
        if (r.getPlayers().size() == 2) {
            gameEngine.startGame(r);
        }
    }


    public void routeActionToRoom(WsMessageDto msg, WebSocketSession session) {
        Room r = rooms.get(msg.getRoomId());
        if (r == null) return;
        // 将消息交给 game engine 处理（同步化处理）
        gameEngine.handleAction(r, msg);
    }

    public void broadcast(Room room, WsMessageDto msg) {
        room.setLastActiveAt(System.currentTimeMillis());
        room.getPlayers().values().forEach(p -> {
            try {
                if (p.getSession() != null && p.getSession().isOpen()) {
                    TextMessage textMessage = new TextMessage(mapper.writeValueAsString(msg));
                    System.out.println("广播给 player " + p.getId() + ": " + textMessage.getPayload());
                    p.getSession().sendMessage(textMessage);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void broadcastState(Room room, WsMessageDto messageDto) {
        messageDto.setType("STATE_UPDATE");
        messageDto.setRoomId(room.getId());
        messageDto.setPayload(room.getState());
        messageDto.setPlayerIds(room.getPlayers().values());
        broadcast(room, messageDto);
    }

    public void handleDisconnect(Long playerId) {
        // 找到房间并标记掉线；可实现重连逻辑
        rooms.values().forEach(room -> {
            if (room.getPlayers().containsKey(playerId)) {
                room.getPlayers().remove(playerId);
                // 广播对方获胜或等待重连
                WsMessageDto m = new WsMessageDto();
                m.setType("PLAYER_LEFT");
                m.setRoomId(room.getId());
                m.setSenderId(playerId);
                broadcast(room, m);
            }
        });
    }

// ... (在 RoomService.java 文件中)

    public void leaveRoom(String roomId, Long senderId) {
        Room r = rooms.get(roomId);
        if (r == null) return;

        // 1. 移除玩家
        Player p = r.getPlayers().remove(senderId);

        // 2. 移除 sessionToPlayer 映射 (如果 GameWebSocketHandler 中没有处理)
        // 注意: 理论上，GameWebSocketHandler 应该在收到 LEAVE 消息后也移除 sessionToPlayer 映射，
        // 但为了健壮性，这里不直接操作 sessionToPlayer。
        // GameWebSocketHandler.java 应该处理 sessionToPlayer 移除 (见下方改进)。

        // 3. 广播玩家离开
        WsMessageDto m = new WsMessageDto();
        m.setType("PLAYER_LEFT");
        m.setRoomId(roomId);
        m.setSenderId(senderId);
        broadcast(r, m);

        // 4. (可选) 清理房间: 如果房间空了，可以考虑移除房间，或标记为待清理
        if (r.getPlayers().isEmpty()) {
            rooms.remove(roomId);
        }

        // 5. (可选) 通知 GameEngine 结束游戏/判断胜负
        if (r.getPlayers().size() == 1) {
            // 另一位玩家获胜或等待
             gameEngine.handlePlayerLeave(r, senderId); // 需在 GameEngine 中添加此方法
        }

    }

}

