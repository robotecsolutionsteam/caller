package com.robotec.caller.websocket;

import android.util.Log;

import com.microsoft.signalr.HubConnectionState;
import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.TransportEnum;
import com.microsoft.signalr.HubConnectionBuilder;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SignalRClient {
    private static SignalRClient instance;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private static final String DATA_HUB_URL = "";
    private static final String ROBOT_HUB_URL = "";

    private HubConnection hubConnectionData;
    private HubConnection hubConnectionRobot;

    public SignalRClient() {
        try {
            hubConnectionData = buildAndStartHubConnection(DATA_HUB_URL);
            hubConnectionRobot = buildAndStartHubConnection(ROBOT_HUB_URL);
        } catch (Exception e) {
            Log.e("Robot", "Erro ao se conectar ao hub: " + e.getMessage());
        }
    }

    private HubConnection buildAndStartHubConnection(String url) {
        HubConnection hubConnection = HubConnectionBuilder.create(url)
                .shouldSkipNegotiate(true)
                .withHandshakeResponseTimeout(30 * 1000)
                .withTransport(TransportEnum.WEBSOCKETS).build();
        hubConnection.start().blockingAwait();
        startConnectionCheck();
        return hubConnection;
    }

    public void listenToServer(String map, String atv, String code) {
        try {
            hubConnectionRobot.on(map, (response) -> {
            }, String.class);

            hubConnectionData.on(atv, (response) -> {
            }, String.class);

            hubConnectionRobot.on(code, (response) -> {
            }, String.class);

        } catch (Exception e) {
            Log.e("Robot", "Erro ao receber: " + e.getMessage());
        }
    }

    public boolean startConection() {
        try {
            hubConnectionRobot.start().blockingAwait();
            hubConnectionData.start().blockingAwait();
            Log.v("Robot", "Conectado!!");
            return true;
        } catch (Exception e) {
            Log.e("Robot", "Falha ao conectar: " + e.getMessage());
            return false;
        }
    }

    public static SignalRClient getInstance() {
        try {
            if (instance == null) {
                instance = new SignalRClient();
            }
            return instance;
        } catch (Exception e) {
            Log.e("Robot", "Falha ao conectar: " + e.getMessage());
            return instance;
        }
    }

    private void startConnectionCheck() {
        final Runnable connectionCheck = new Runnable() {
            public void run() {
                checkAndReconnect();
            }
        };
        scheduler.scheduleAtFixedRate(connectionCheck, 5, 5, TimeUnit.SECONDS);
    }

    private void checkAndReconnect() {
        if (hubConnectionData.getConnectionState() == HubConnectionState.DISCONNECTED) {
            try {
                hubConnectionData.start().blockingAwait();
            } catch (Exception e) {
                Log.e("websocket", "Falha ao conectar");
            }
        }
        if (hubConnectionRobot.getConnectionState() == HubConnectionState.DISCONNECTED) {
            try {
                hubConnectionRobot.start().blockingAwait();
            } catch (Exception e) {
                Log.e("websocket", "Falha ao conectar");
            }
        }
    }
}