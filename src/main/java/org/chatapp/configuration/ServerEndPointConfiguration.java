package org.chatapp.configuration;

import org.chatapp.controllers.EchoServer;

import javax.websocket.Endpoint;
import javax.websocket.server.ServerApplicationConfig;

import javax.websocket.server.ServerEndpointConfig;
import java.util.HashSet;
import java.util.Set;


public class ServerEndPointConfiguration implements ServerApplicationConfig{


    @Override
    public Set<ServerEndpointConfig> getEndpointConfigs(Set<Class<? extends Endpoint>> set) {
        Set<ServerEndpointConfig> serverEndPointConfigurations = new HashSet<>();
        if (set.contains(EchoServer.class)){
            serverEndPointConfigurations.add(new ServerEndpointConfig.Builder(
                    EchoServer.class,
                    "/websocket/room").build());
        }

        return serverEndPointConfigurations;
    }

    @Override
    public Set<Class<?>> getAnnotatedEndpointClasses(Set<Class<?>> set) {
        return null;
    }
}
