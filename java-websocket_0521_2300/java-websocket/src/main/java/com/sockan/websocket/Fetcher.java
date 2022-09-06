package com.sockan.websocket;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

import com.sockan.model.SensorData;
import com.sockan.websocket.DatabaseHandler;

public class Fetcher{
    //private static final String FILEPATH = "static\\current_reading.txt";
    //private final String path = this.getClass().getResource("/static/").getFile();
    //private final String path = this.getClass().getResource("../").getFile();
    private final String path = this.getClass().getResource("/").getFile();
    DatabaseHandler db = new DatabaseHandler();

    private String previous;
    private String incoming;
    private boolean update;

    private String dateTime;
    private String temperature;
    private String humidity;

    public Fetcher() {}

    public String getIncoming() {return incoming;}
    public String getPrevious() {return previous;}

    public boolean isUpdate() {return update;}

    public String getDateTime() {return dateTime;}
    public String getTemperature() {return temperature;}
    public String getHumidity() {return humidity;}

    public void setPrevious(String previous) {this.previous = previous;}
    public void setIncoming(String incoming) {this.incoming = incoming;}
    public void setUpdate(boolean update) {this.update = update;}

    public void setDateTime(String dateTime) {this.dateTime = dateTime;}
    public void setTemperature(String temperature) {this.temperature = temperature;}
    public void setHumidity(String humidity) {this.humidity = humidity;}

    public List<SensorData> getDataFromDB(){
        return db.getLastData();
    }

    public void fetchData() {
        readFile();
        tokenize();
    }

    private void readFile() {
        try {
            File file  = new File( "C:\\Program\\Apache Software Foundation\\Tomcat 9.0\\webapps\\java_websocket_war\\WEB-INF\\classes\\current_reading.txt");
            //File file  = new File(path + "current_reading.txt");
            Scanner scanner = new Scanner(file);

            if (scanner.hasNext())
                setIncoming(scanner.nextLine());

            if (!getIncoming().equals(getPrevious())) {
                setPrevious(getIncoming());
                setUpdate(true);
            } else
                setUpdate(false);

            scanner.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void saveFile() throws IOException {
        int rand = (int)(Math.random()*(35-10+1)+10);
        LocalDateTime instant = LocalDateTime.now();
        DateTimeFormatter instantFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedDate = instant.format(instantFormat);

        final String content = rand + ".00,00.00," + formattedDate;
            final Path path = Paths.get("C:\\Program\\Apache Software Foundation\\Tomcat 9.0\\webapps\\java_websocket_war\\WEB-INF\\classes\\current_reading.txt");
            //final Path path = Paths.get("current_reading.txt");
            try (final BufferedWriter writer = Files.newBufferedWriter(path,
                    StandardCharsets.UTF_8, StandardOpenOption.CREATE);) {
                System.out.println(content);
                writer.write(content);
                writer.flush();
            }
    }

    private void tokenize() {

        List<String> items = new ArrayList<String>();
        StringTokenizer tokenizer = new StringTokenizer(getIncoming(), ",");

        while (tokenizer.hasMoreElements())
            items.add(tokenizer.nextToken());

        setTemperature(items.get(0));
        setHumidity(items.get(1));
        setDateTime(items.get(2));
    }
}