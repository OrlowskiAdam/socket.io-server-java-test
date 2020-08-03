package com.example.socketio;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class SocketLauncher {
    private String message = "Wiadmość dnia!";

    @PostConstruct
    public void socketLauncher() {

        Configuration config = new Configuration();
        config.setHostname("localhost");
        config.setPort(9092);

        final SocketIOServer server = new SocketIOServer(config);

        server.addConnectListener(socketIOClient -> {
            System.out.println("POŁĄCZONO Z: " + socketIOClient.getSessionId());
            socketIOClient.sendEvent("message", message);
        });

        server.addEventListener("message", String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient socketIOClient, String s, AckRequest ackRequest) throws Exception {
                message = s;
                server.getBroadcastOperations().sendEvent("message", message);
            }
        });

        server.start();
    }

}
