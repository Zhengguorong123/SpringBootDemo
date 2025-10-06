package gwan.zheng.core.config;

/**
 * @Author: 郑国荣
 * @Date: 2025-10-05-17:23
 * @Description:
 */
import gwan.zheng.core.advice.AuthHandshakeInterceptor;
import gwan.zheng.core.game.ws.GameWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final GameWebSocketHandler gameWebSocketHandler;
    private final AuthHandshakeInterceptor authHandshakeInterceptor;

    public WebSocketConfig(GameWebSocketHandler gameWebSocketHandler,
                           AuthHandshakeInterceptor authHandshakeInterceptor) {
        this.gameWebSocketHandler = gameWebSocketHandler;
        this.authHandshakeInterceptor = authHandshakeInterceptor;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(gameWebSocketHandler, "/ws/game")
                .addInterceptors(authHandshakeInterceptor)
                .setAllowedOrigins("*"); // 可根据需要改为前端域名
    }
}

