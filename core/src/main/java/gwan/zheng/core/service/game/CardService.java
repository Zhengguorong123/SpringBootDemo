package gwan.zheng.core.service.game;

/**
 * @Author: 郑国荣
 * @Date: 2025-10-05-17:33
 * @Description:
 */

import gwan.zheng.core.model.entity.game.Card;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CardService {
    private final Map<String, Card> cardPool = new HashMap<>();

    public CardService() {
        // 初始化一些卡牌
        Card c = new Card();
        c.setId("strike");
        c.setName("Strike");
        c.setCost(1);
        c.setEffect("damage:3");
        cardPool.put(c.getId(), c);
        // 更多卡...
    }

    public Card getCard(String id) {
        return cardPool.get(id);
    }

    public List<Card> sampleStarterDeck() {
        List<Card> deck = new ArrayList<>();
        for (int i = 0; i < 10; i++) deck.add(cardPool.get("strike"));
        return deck;
    }
}
