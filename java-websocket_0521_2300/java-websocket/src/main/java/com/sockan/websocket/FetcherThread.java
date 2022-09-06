package com.sockan.websocket;

import com.sockan.model.Message;

import javax.websocket.EncodeException;
import java.io.IOException;
import java.util.Set;

import static com.sockan.websocket.EndPoint.endPoints;

public class FetcherThread extends Thread {
    Message message;
    Fetcher fetch = new Fetcher();
    DatabaseHandler db = new DatabaseHandler();
    private final Set<EndPoint> sessions;

    public FetcherThread (Set<EndPoint> sessions, Message message){
        this.message = message;
        this.sessions = sessions;
        this.start();
    }
    public void run() {
        while(true) {
            try {
                Thread.sleep(10000);
                fetch.saveFile();
                System.out.println("File saved!");
                fetch.fetchData();
                db.setToDatabase(fetch.getTemperature(), fetch.getDateTime());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            endPoints.forEach(endpoint -> {
                if(fetch.isUpdate()){
                    synchronized (endpoint) {
                        try {
                            Thread.sleep(1000);
                            message.setContent("  " + fetch.getTemperature() + " ║ " + fetch.getDateTime() + " ║ ");
                            System.out.println(message.getContent());
                            endpoint.session.getBasicRemote()
                                    .sendObject(message);
                        } catch (IOException | EncodeException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }
}
