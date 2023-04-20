package com.sbplat.chatbridge.server;

import java.io.*;
import java.net.*;
import java.util.*;

public class ServerSingleClient {
    private ServerSocket server;
    private Socket client;
    private ServerMessageListener listener;
    private BufferedReader reader;

    public ServerSingleClient(int port, ServerMessageListener listener) throws IOException {
        server = new ServerSocket(port);
        this.listener = listener;
    }

    public int getPort() {
        return server.getLocalPort();
    }

    public void listen() throws IOException {
        System.out.println("Listening for connections on port " + server.getLocalPort() + "...");
        client = server.accept();
        System.out.println("Connection from " + client.getInetAddress() + ":" + client.getPort() + " has been established!");

        reader = new BufferedReader(new InputStreamReader(client.getInputStream()));

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    listenForMessages();
                } catch (SocketException e) {
                    System.out.println("Connection from " + client.getInetAddress() + ":" + client.getPort() + " has been closed!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public void stop() throws IOException {
        System.out.println("Stopping socket server...");
        if (client != null) {
            client.close();
        }
        server.close();
    }

    public void sendMessage(String message) throws IOException {
        OutputStream out = client.getOutputStream();
        byte[] encodedMessageBytes = Base64.getEncoder().encode(message.getBytes("UTF-8"));
        out.write(encodedMessageBytes);
        out.write("\n".getBytes("UTF-8"));
        out.flush();
    }

    private ServerMessage readMessage() throws IOException {
        String encodedMessage = reader.readLine();
        String[] parts = encodedMessage.split(",");
        byte[] decodedAuthorBytes = Base64.getDecoder().decode(parts[0]);
        byte[] decodedMessageBytes = Base64.getDecoder().decode(parts[1]);
        return new ServerMessage(new String(decodedAuthorBytes, "UTF-8"), new String(decodedMessageBytes, "UTF-8"));
    }

    private void listenForMessages() throws IOException {
        while (!client.isClosed()) {
            ServerMessage message = readMessage();
            System.out.println("Received message from " + client.getInetAddress() + ":" + client.getPort() + ": <" + message.getAuthor() + "> " + message.getContent());
            listener.onMessageReceived(message);
        }
        System.out.println("Stopped listening for messages from " + client.getInetAddress() + ":" + client.getPort());
    }
}
