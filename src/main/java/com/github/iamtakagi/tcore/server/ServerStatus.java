package com.github.iamtakagi.tcore.server;

import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.net.*;

@Getter @Setter
public class ServerStatus {

    public static final byte NUM_FIELDS = 6;
    public static final int DEFAULT_TIMEOUT = 10;

    private String address;
    private int port;
    private int timeout;
    private boolean serverUp;
    private String motd;
    private String version;
    private int currentPlayers;
    private int maximumPlayers;
    private long latency;

    public ServerStatus(String address, int port) {
        this(address, port, DEFAULT_TIMEOUT);
    }

    public ServerStatus(String address, int port, int timeout) {
        setAddress(address);
        setPort(port);
        setTimeout(timeout);
        refresh();
    }

    public boolean refresh() {
        String[] serverData;
        String rawServerData;
        try {
            //Socket clientSocket = new Socket(getAddress(), getPort());
            Socket clientSocket = new Socket();
            long startTime = System.currentTimeMillis();
            clientSocket.connect(new InetSocketAddress(getAddress(), getPort()), timeout);
            setLatency(System.currentTimeMillis() - startTime);
            DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            byte[] payload = {(byte) 0xFE, (byte) 0x01};
            //dos.writeBytes("\u00FE\u0001");
            dos.write(payload, 0, payload.length);
            rawServerData = br.readLine();
            clientSocket.close();
        } catch (Exception e) {
            serverUp = false;
            return serverUp;
        }

        if (rawServerData == null)
            serverUp = false;
        else {
            serverData = rawServerData.split("\u0000\u0000\u0000");
            if (serverData != null && serverData.length >= NUM_FIELDS) {
                serverUp = true;
                setVersion(serverData[2].replace("\u0000", ""));
                setMotd(serverData[3].replace("\u0000", ""));
                setCurrentPlayers(Integer.parseInt(serverData[4].replace("\u0000", "")));
                setMaximumPlayers(Integer.parseInt(serverData[5].replace("\u0000", "")));
            } else
                serverUp = false;
        }
        return serverUp;
    }
}
