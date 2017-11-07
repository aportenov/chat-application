package org.chatapp.controllers;

import javax.websocket.*;
import java.io.IOException;


public class EndpointServer extends Endpoint {

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        RemoteEndpoint.Basic remoteEndpoint = session.getBasicRemote();
        session.addMessageHandler(new ServerMessageHandler(remoteEndpoint));
    }

    public static class ServerMessageHandler implements MessageHandler.Whole<String> {

        private final RemoteEndpoint.Basic remoteEndpoint;

        public ServerMessageHandler(RemoteEndpoint.Basic remontEndpoint) {
            this.remoteEndpoint = remontEndpoint;
        }
        @Override
        public void onMessage(String message) {
            try{
                if (remoteEndpoint != null){
                    remoteEndpoint.sendText(message);
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
