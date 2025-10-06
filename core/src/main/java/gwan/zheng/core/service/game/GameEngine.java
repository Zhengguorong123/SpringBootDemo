package gwan.zheng.core.service.game;

/**
 * @Author: 郑国荣
 * @Date: 2025-10-05-17:31
 * @Description:
 */
import gwan.zheng.core.model.dto.game.WsMessageDto;
import gwan.zheng.core.model.entity.game.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class GameEngine {

    @Autowired
    private RoomService roomService;
    // 推荐对每个房间使用锁来保证 action 顺序一致
    private final ConcurrentHashMap<String, ReentrantLock> roomLocks = new ConcurrentHashMap<>();

    public GameEngine(RoomService roomService) {
        this.roomService = roomService;
    }

    public void startGame(Room room) {
        // 初始化牌库/抽牌/设定先手
        room.getState().setTurnPlayerId(room.getPlayers().keySet().iterator().next());
        WsMessageDto wsMessageDto = new WsMessageDto();
        wsMessageDto.setMessage("Game Started");
        roomService.broadcastState(room, wsMessageDto);
    }

    public void handleAction(Room room, WsMessageDto msg) {
        ReentrantLock lock = roomLocks.computeIfAbsent(room.getId(), k -> new ReentrantLock());
        lock.lock();
        try {
            String type = msg.getType();
            if ("PLAY_CARD".equals(type)) {
                // payload 应包含 cardId, targetId 等
                // 1) 校验是否合法（当前回合、资源、卡牌在手）
                // 2) 计算效果并修改 room.state
                // 3) 记录日志，广播变更
                // 示例：直接使 target 受 3 点伤害
                String targetId = ((java.util.Map)msg.getPayload()).get("targetId").toString();
                room.getState().getPlayerHp().put(targetId, room.getState().getPlayerHp().getOrDefault(targetId, 30) - 3);
                room.getState().getEventLog().add(msg.getSenderId() + " played card to damage " + targetId);
                WsMessageDto wsMessageDto = new WsMessageDto();
                wsMessageDto.setMessage(msg.getSenderId() + " played a card");
                roomService.broadcastState(room, wsMessageDto);
                checkEndCondition(room);
            } else if ("END_TURN".equals(type)) {
                // 切换回合
                room.getState().setTurnPlayerId(nextPlayerId(room, msg.getSenderId()));
                WsMessageDto wsMessageDto = new WsMessageDto();
                wsMessageDto.setMessage("Turn ended. Now it's " + room.getState().getTurnPlayerId() + "'s turn.");
                roomService.broadcastState(room, wsMessageDto);
            }
            // 其他 action...
        } finally {
            lock.unlock();
        }
    }

    private Long nextPlayerId(Room room, Long current) {
        for (Long pid : room.getPlayers().keySet()) {
            if (!pid.equals(current)) return pid;
        }
        return current;
    }

    private void checkEndCondition(Room room) {
        room.getState().getPlayerHp().forEach((pid, hp) -> {
            if (hp <= 0) {
                WsMessageDto m = new WsMessageDto();
                m.setType("GAME_OVER");
                m.setRoomId(room.getId());
                m.setPayload(pid); // 败者 id
                roomService.broadcast(room, m);
            }
        });
    }

    public void handlePlayerLeave(Room room, Long senderId) {
        ReentrantLock lock = roomLocks.computeIfAbsent(room.getId(), k -> new ReentrantLock());
        lock.lock();
        try {
            room.getPlayers().remove(senderId);
            WsMessageDto m = new WsMessageDto();
            m.setType("PLAYER_LEFT");
            m.setRoomId(room.getId());
            m.setPayload(senderId);
            roomService.broadcast(room, m);
            if (room.getPlayers().size() < 2) {
                // 结束游戏
                m = new WsMessageDto();
                m.setType("GAME_OVER");
                m.setRoomId(room.getId());
                m.setPayload("Not enough players");
                roomService.broadcast(room, m);
            }
        } finally {
            lock.unlock();
        }
    }
}

