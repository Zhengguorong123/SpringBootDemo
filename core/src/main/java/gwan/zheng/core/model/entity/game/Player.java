package gwan.zheng.core.model.entity.game;

/**
 * @Author: 郑国荣
 * @Date: 2025-10-05-17:28
 * @Description:
 */
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;

@Data
public class Player {
    private Long id;
    private String name;
    // runtime
    @JsonIgnore
    private transient WebSocketSession session;
    private transient int hp;
    private transient List<Card> hand;
}
