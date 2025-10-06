package gwan.zheng.core.model.entity.game;

/**
 * @Author: 郑国荣
 * @Date: 2025-10-05-17:29
 * @Description:
 */
import lombok.Data;
@Data
public class Card {
    private String id;
    private String name;
    private int cost;
    private String effect; // 可进一步做成策略类
}
