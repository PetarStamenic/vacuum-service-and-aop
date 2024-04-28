package rs.raf.vacuumbackend.testiranje_kljuceva;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Banka2 {

    public static void main(String[] args) {
        HttpClient httpClient = HttpClient.newHttpClient();
        int succes = 0;
        int failiure = 0;
        int succesRate = 0;
        int failiurRate = 0;
        String apikey = "OF6BVKZOCXWHD9NS";
        System.out.println("Testiranje kljuca za Banka-2 | apikey: "+ apikey);
        for(int i = 1; i<1000000;i++){
            String querry = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=IBM&interval=5min&apikey="+apikey;
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(querry)).build();
            try {
                Thread.sleep(100);
                HttpResponse<String> quoteResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                if(quoteResponse.body().contains("higher API call volume")){
                    failiure++;
                    succesRate++;
                }else {
                    succes++;
                    failiurRate++;
                }
                if(i%100 == 0) {
                    LocalTime time = LocalTime.now();
                    String t = time.format(DateTimeFormatter.ofPattern("HH:mm"));
                    System.out.printf(t + "     Total: %05d  | Total success: %05d  | Total failure: %05d  | success in batch %05d  | failure in batch %05d \n",i,succes,failiure,succesRate,failiurRate);
                    succesRate = 0;
                    failiurRate = 0;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


}
