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

    public static String getInfos(String stockCode) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            Date date = new Date();
            String today = dateFormat.format(date);

            String stockUrl = String.format(STOCK_BASE_URL, today, today, stockCode);
            System.out.println(stockUrl);

            URL uri = new URL(stockUrl);
            HttpURLConnection connection = (HttpURLConnection) uri.openConnection();
            connection.setRequestProperty("ContentType", "application/json");

            try (BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                StringBuffer buffer = new StringBuffer();
                while ((line = input.readLine()) != null) {
                    buffer.append(line);
                }

                connection.disconnect();
                String resStockInfo = buffer.toString()
                        .split("],")[1]
                        .split("]")[0]
                        .replaceAll("\\[", "")
                        .trim();
                System.out.println(resStockInfo);

                return resStockInfo;
            }
        } catch (Exception ex) {
            return "";
        }
    }

    public static void main(String[] args) throws IOException {
        while(true) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("> 주식코드를 입력해주세요. [quit: \\q] : ");
            String cmd = scanner.nextLine();
            if ("\\q".equals(cmd)) {
                System.exit(0);
            }

            String resStockInfo = getInfos(cmd);
            FileWriter fileWriter = new FileWriter("./" + cmd + ".json");
            fileWriter.write(resStockInfo);
            fileWriter.flush();
            fileWriter.close();

        }
    }
}
