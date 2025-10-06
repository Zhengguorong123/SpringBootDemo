package gwan.zheng.core.model.entity.game;

/**
 * @Author: 郑国荣
 * @Date: 2025-10-05-17:29
 * @Description:
 */
import lombok.Data;
import java.util.concurrent.ConcurrentHashMap;
@Data
public class Room {
    private String id;
    private ConcurrentHashMap<Long, Player> players = new ConcurrentHashMap<>();
    private GameState state = new GameState();
    private long lastActiveAt;
}
