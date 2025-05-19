package cn.jxufe.ws;

import cn.jxufe.model.dto.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/ws/player/{playerId}")
@Component
public class NativeWebSocketServer {
    private static final Map<Long, Session> sessions = new ConcurrentHashMap<>();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @OnOpen
    public void onOpen(Session session, @PathParam("playerId") Long playerId) {
        sessions.put(playerId, session);
    }

    @OnClose
    public void onClose(Session session, @PathParam("playerId") Long playerId) {
        sessions.remove(playerId);
    }

    @OnError
    public void onError(Session session, Throwable error, @PathParam("playerId") Long playerId) {
        sessions.remove(playerId);
    }

    @OnMessage
    public void onMessage(String message, Session session, @PathParam("playerId") Long playerId) {
        // 可根据需要处理客户端发来的消息
    }

    // 提供静态方法供业务层推送消息
    public static void pushToPlayer(Long playerId, Message msg) {
        Session session = sessions.get(playerId);
        if (session != null && session.isOpen()) {
            try {
                session.getBasicRemote().sendText(objectMapper.writeValueAsString(msg));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
