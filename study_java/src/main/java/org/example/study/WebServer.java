package org.example.study;

import com.google.common.io.Resources;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.apache.http.client.utils.URLEncodedUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;

public class WebServer {

    static String rootHtml;

    static {
        try {
            rootHtml = Resources.toString(
                    Resources.getResource("views/stocks.html")
                    , StandardCharsets.UTF_8);
        } catch (Exception e) {
        }
    }

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 100);
        server.createContext("/", new RootWebHandler());
        server.createContext("/stocks", new StockInfoWebHandler());
        server.setExecutor(Executors.newFixedThreadPool(100));
        server.start();
        System.out.println("web server start..");
    }

    static class RootWebHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange t) throws IOException {
            t.sendResponseHeaders(200, rootHtml.length());
            OutputStream os = t.getResponseBody();
            os.write(rootHtml.getBytes());
            os.close();
        }
    }

    static class StockInfoWebHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange t) throws IOException {
            String code = URLEncodedUtils.parse(t.getRequestURI().getQuery()
                    , StandardCharsets.UTF_8).get(0).getValue();
            String data = StockLauncher.getInfos(code); //ex: "005930"
            t.sendResponseHeaders(200, data.length());
            OutputStream os = t.getResponseBody();
            os.write(data.getBytes());
            os.close();
        }
    }
}
