package gwan.zheng.core.model.dto.game;

/**
 * @Author: 郑国荣
 * @Date: 2025-10-05-17:28
 * @Description:
 */

import gwan.zheng.core.model.entity.game.Player;
import lombok.Data;

import java.util.Collection;
import java.util.List;

@Data
public class WsMessageDto {
    private String type;      // e.g. "AUTH","JOIN_ROOM","PLAY_CARD","STATE_UPDATE"
    private String roomId;
    private Long senderId;
    private Object payload;   // 自由 JSON，具体内容由 type 定义
    private Collection<Player> playerIds;
    private String message;
}
