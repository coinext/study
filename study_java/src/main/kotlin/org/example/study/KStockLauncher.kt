package org.example.study

import java.io.BufferedReader
import java.io.FileWriter
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class KStockLauncher

val STOCK_BASE_URL =
        "https://api.finance.naver.com/siseJson.naver?requestType=1&startTime=%s&endTime=%s&timeframe=day&symbol=%s"

fun main(args: Array<String>) {
    while (true) {
        val scanner = Scanner(System.`in`)
        print("> 주식코드를 입력해주세요. [quit: \\q] : ")
        val cmd = scanner.nextLine() //ex: 005930 삼성
        if ("\\q" == cmd) {
            System.exit(0)
        }
        val dateFormat: DateFormat = SimpleDateFormat("yyyyMMdd")
        val date = Date()
        val today = dateFormat.format(date)
        val stockUrl = String.format(STOCK_BASE_URL, today, today, cmd)
        println(stockUrl)
        val uri = URL(stockUrl)
        val connection = uri.openConnection() as HttpURLConnection
        connection.setRequestProperty("ContentType", "application/json")
        val code = connection.responseCode
        BufferedReader(InputStreamReader(connection.inputStream)).use { input ->
            var line: String?
            val buffer = StringBuffer()
            while (input.readLine().also { line = it } != null) {
                buffer.append(line)
            }
            connection.disconnect()
            val resStockInfo = buffer.toString()
            println(resStockInfo)
            FileWriter("./$code.json").let {
                it.write(resStockInfo)
                it.flush()
                it.close()
            }
        }
    }
}