package org.chatapp.configuration;

import org.chatapp.controllers.EndpointServer;
import org.springframework.context.annotation.Configuration;

import javax.websocket.Endpoint;
import javax.websocket.server.ServerApplicationConfig;
import javax.websocket.server.ServerEndpointConfig;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class ServerEndpointConfiguration implements ServerApplicationConfig{
    @Override
    public Set<ServerEndpointConfig> getEndpointConfigs(Set<Class<? extends Endpoint>> set) {
        Set<ServerEndpointConfig> serverEndpointConfigSet = new HashSet<>();
        if (set.contains(EndpointServer.class)){
            serverEndpointConfigSet.add(
                    ServerEndpointConfig.Builder.create(
                    EndpointServer.class,
                    "/websocket/room"
                    )
                    .build()
            );
        }

        return serverEndpointConfigSet;
    }

    @Override
    public Set<Class<?>> getAnnotatedEndpointClasses(Set<Class<?>> set) {
        return null;
    }
}
