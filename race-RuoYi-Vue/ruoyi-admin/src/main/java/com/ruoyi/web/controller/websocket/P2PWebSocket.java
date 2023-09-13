package com.ruoyi.web.controller.websocket;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
@Slf4j
@ServerEndpoint(value = "/p2p")
@Component
public class P2PWebSocket {
    private static Session[] sessionContainer = new Session[2];
    /**
     A、B与服务器建⽴连接
     */
    @OnOpen
    public void onOpen(Session session) {
        System.out.println(this);
        if(sessionContainer[0] == null && sessionContainer[1] == null){
            sessionContainer[0] = session;
            log.info("a⽤户已接⼊");
        }else if(sessionContainer[0] != null && sessionContainer[1] == null){
            sessionContainer[1] = session;
            log.info("b⽤户已接⼊");
            log.info("a与b⽤户准备就绪");
        }else{
            //do Nothing
        }
    }
    /**
     * 连接关闭调⽤的⽅法
     */
    @OnClose
    public void onClose(Session session) {
        for(int i = 0 ; i < sessionContainer.length ; i++){
            if(session == sessionContainer[i]){
                sessionContainer[i] = null;
                log.info((i==0?"a":"b") + "⽤户已退出");
            }
        }
    }
    /**
     * 得到另⼀个Session对象
     * @param session
     * @return
     */
    private Session getOtherSession(Session session){
        for(int i = 0 ; i < sessionContainer.length ; i++){
            if(session == sessionContainer[i]){
                return sessionContainer[(i==0?1:0)];
            }
        }
        return null;
    }
    /**
     * 向另⼀个会话发送消息
     * @param session
     * @return
     */
    @OnMessage
    public void sendMessage(String message, Session session) throws IOException {
        Session otherSession = this.getOtherSession(session);
        otherSession.getBasicRemote().sendText(message);
    }
    /**
     * 异常处理
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("",error);
    }
}
