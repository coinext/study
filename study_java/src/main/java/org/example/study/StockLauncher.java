package org.example.study;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class StockLauncher {

    final static String STOCK_BASE_URL = "https://api.finance.naver.com/siseJson.naver?requestType=1&startTime=%s&endTime=%s&timeframe=day&symbol=%s";

    public static void main(String[] args) throws IOException {
        while(true) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("> 주식코드를 입력해주세요. [quit: \\q] : ");
            String cmd = scanner.nextLine();
            if ("\\q".equals(cmd)) {
                System.exit(0);
            }

            String stockNm = cmd; //ex: 005930 삼성

            DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            Date date = new Date();
            String today = dateFormat.format(date);

            String stockUrl = String.format(STOCK_BASE_URL, today, today, stockNm);
            System.out.println(stockUrl);

            URL uri = new URL(stockUrl);
            HttpURLConnection connection = (HttpURLConnection) uri.openConnection();
            connection.setRequestProperty("ContentType", "application/json");
            int code = connection.getResponseCode();

            try (BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                StringBuffer buffer = new StringBuffer();
                while ((line = input.readLine()) != null) {
                    buffer.append(line);
                }

                connection.disconnect();
                String resStockInfo = buffer.toString();
                System.out.println(resStockInfo);

                FileWriter fileWriter = new FileWriter("./" + code + ".json");
                fileWriter.write(resStockInfo);
                fileWriter.flush();
                fileWriter.close();
            }
        }
    }
}
