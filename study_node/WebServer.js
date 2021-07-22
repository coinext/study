const http = require('http');
const fs = require('fs');
const asyncRequest = require('request-promise');
const request = require('request');
const syncRequest = require('sync-request');

const STOCK_URL = "https://api.finance.naver.com/siseJson.naver";
const stockCodes = ["005930", "293490", "181710", "036570", "003550"];

function getCurrentDate()
{
    let date = new Date();
    let year = date.getFullYear().toString();
    let month = date.getMonth() + 1;
    month = month < 10 ? '0' + month.toString() : month.toString();
    let day = date.getDate();
    day = day < 10 ? '0' + day.toString() : day.toString();
    return year + month + day ;
}

function getStockInfo(stockCode) {
    let today = getCurrentDate();
    const options = {
        uri: STOCK_URL,
        qs:{
            requestType:1,
            startTime:today,
            endTime:today,
            timeframe:"day",
            symbol:stockCode
        }
    };

    var body = syncRequest("POST", STOCK_URL, options).getBody("utf-8")
        .split("],")[1]
        .split("]")[0]
        .replace("[", "")
        .trim();
    console.log(body);
    return body;
}

async function getAsyncStockInfo(stockCode) {
    let today = getCurrentDate();
    const options = {
        uri: STOCK_URL,
        qs:{
            requestType:1,
            startTime:today,
            endTime:today,
            timeframe:"day",
            symbol:stockCode
        }
    };

    console.log("xxx");
    return await asyncRequest(options);
}

const app = http.createServer(async function(request,response){
    let data = "";
    if(request.url == '/sync/stocks'){
        for (let index in stockCodes) {
            data += getStockInfo(stockCodes[index]) + "<br/>";
        }
    } else if (request.url == '/async/stocks') {
        let awaits = [];
        for (let index in stockCodes) {
            awaits.push(getAsyncStockInfo(stockCodes[index]));
        }
       // let stocks = await Promise.all(awaits);
       // data = stocks.join("<br/>");
    }

    response.writeHead(200);
    response.end(fs.readFileSync(__dirname + '/views/stocks.html').toString().replace("$stocks", data));
});
app.listen(8000);