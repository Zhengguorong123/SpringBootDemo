package gwan.zheng.core.model.entity.game;

/**
 * @Author: 郑国荣
 * @Date: 2025-10-05-17:29
 * @Description:
 */
import lombok.Data;
import java.util.*;
@Data
public class GameState {
    private Long turnPlayerId;
    private Map<String, Integer> playerHp = new HashMap<>();
    private List<String> eventLog = new ArrayList<>();
    // deck, discard piles, etc.
}
