package gwan.zheng.core.advice;

import gwan.zheng.service.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: 郑国荣
 * @Date: 2025-10-05-20:18
 * @Description:
 */
@Component
public class AuthHandshakeInterceptor implements HandshakeInterceptor {

    @Autowired
    private TokenManager tokenManager;
    private final ConcurrentHashMap<String, Long> sessionToPlayer = new ConcurrentHashMap<>();


    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) {
        if (request instanceof ServletServerHttpRequest servletRequest) {
            String token = servletRequest.getServletRequest().getParameter("token");
            System.out.println("拦截到的 token: " + token);

            if (token == null || !tokenManager.validateToken(token)) {
                System.out.println("WebSocket 握手失败：Token 无效或过期");
                return false;
            }

            Long userId = tokenManager.getUserId(token);
            if (userId == null) {
                tokenManager.deleteToken(token);
                return false;
            }

            // 暂存用户信息到 attributes
            attributes.put("userId", userId);
            attributes.put("token", token);

            System.out.println("握手通过，userId=" + userId);
        }
        return true;
    }


    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response,
                               WebSocketHandler wsHandler,
                               Exception exception) {}
}

