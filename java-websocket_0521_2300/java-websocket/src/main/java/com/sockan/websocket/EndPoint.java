package com.sockan.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.sockan.model.Message;
import com.sockan.model.SensorData;

@ServerEndpoint(value = "/sensor/", decoders = MessageDecoder.class, encoders = MessageEncoder.class)
public class EndPoint {
    Session session;
    public static Set<EndPoint> endPoints = new CopyOnWriteArraySet<>();
    private static HashMap<String, String> sensor = new HashMap<>();
    private Fetcher fetcher = new Fetcher();

    @OnOpen
    public void onOpen(Session session) throws IOException, EncodeException {
        Message message = new Message();
        new FetcherThread(endPoints, message);
        this.session = session;
        endPoints.add(this);
        fetcher.fetchData();
        message.setContent("Waiting for data!");
        message.setDataObject(fetcher.getDataFromDB());
        broadcast(message);
    }

    @OnMessage
    public void onMessage(Session session, Message message) throws IOException, EncodeException {
        fetcher.fetchData();
        message.setContent(fetcher.getDateTime());
        broadcast(message);
    }

    @OnClose
    public void onClose(Session session) throws IOException, EncodeException {
        endPoints.remove(this);
        Message message = new Message();
        message.setFrom(sensor.get(session.getId()));
        message.setContent("Disconnected!");
        broadcast(message);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
    }

    static void broadcast(Message message) throws IOException, EncodeException {
        endPoints.forEach(endpoint -> {
            synchronized (endpoint) {
                try {
                    endpoint.session.getBasicRemote()
                        .sendObject(message);
                } catch (IOException | EncodeException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}