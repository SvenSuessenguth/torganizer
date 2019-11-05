package org.cc.torganizer.rest;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint("/mqlistener")
public class CheckerServerEndpoint {

    private Session session;

    @OnOpen
    public void connectOnOpen(Session session) {
        this.session = session;
    }

    @OnClose
    public void disconnectOnClose() throws IOException {
        session.close();
        session = null;
    }

    @OnMessage
    public void onMessage(String text) {
        this.session.getAsyncRemote().sendText(text);
    }
}
