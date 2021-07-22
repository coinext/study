package org.example.study;

import com.google.common.io.Resources;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;

public class WebServer {

    static String html;

    static {
        try {
            html = Resources.toString(
                    Resources.getResource("views/stocks.html")
                    , StandardCharsets.UTF_8);
        } catch (Exception e) {
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("ready.." );
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 100);
        server.createContext("/stocks", new StockWebHandler());
        server.setExecutor(Executors.newFixedThreadPool(100));
        server.start();
        System.out.println("start..");
    }

    static class StockWebHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange t) throws IOException {
            t.sendResponseHeaders(200, html.length());
            OutputStream os = t.getResponseBody();
            os.write(html.getBytes());
            os.close();
        }
    }
}
