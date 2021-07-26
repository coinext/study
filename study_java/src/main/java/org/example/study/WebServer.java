package org.example.study;

import com.google.common.io.Resources;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class WebServer {

    static String html;
    static List<String> stockCodes = Arrays.asList("005930", "293490", "181710", "036570", "003550");
    static {
        try {
            html = Resources.toString(
                    Resources.getResource("views/stocks.html")
                    , StandardCharsets.UTF_8);
        } catch (Exception e) {
        }
    }

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 100);
        server.createContext("/sync/stocks", new StockInfoSyncWebHandler());
        server.createContext("/async/stocks", new StockInfoAsyncWebHandler());
        server.setExecutor(Executors.newFixedThreadPool(100));
        server.start();
        System.out.println("web server start..");
    }

    static class StockInfoSyncWebHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange t) throws IOException {
            String stocks = stockCodes.stream()
                    .map(r -> StockLauncher.getInfos(r))
                    .collect(Collectors.joining("</br>"));
            String data = html.replaceAll("\\$stocks", stocks); //ex: "005930"
            t.sendResponseHeaders(200, data.length());
            OutputStream os = t.getResponseBody();
            os.write(data.getBytes());
            os.close();
        }
    }

    static class StockInfoAsyncWebHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange t) throws IOException {
            String stocks = stockCodes.stream().parallel()
                    .map(r -> StockLauncher.getInfos(r))
                    .collect(Collectors.joining("</br>"));
            String data = html.replaceAll("\\$stocks", stocks); //ex: "005930"/ex: "005930"
            t.sendResponseHeaders(200, data.length());
            OutputStream os = t.getResponseBody();
            os.write(data.getBytes());
            os.close();
        }
    }
}
