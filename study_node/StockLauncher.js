const readline = require('readline');
const request = require('request');
const fs = require('fs');

const STOCK_BASE_URL = "https://api.finance.naver.com/siseJson.naver";

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

let r = readline.createInterface({ input:process.stdin, output:process.stdout });
r.setPrompt('> 주식코드를 입력해주세요. [quit: q] : ');
r.prompt();
r.on('line', function(line){
    if (line == 'q') {
        r.close();
    }
    console.log(line);

    let today = getCurrentDate();
    const options = {
        uri: STOCK_BASE_URL,
        qs:{
            requestType:1,
            startTime:today,
            endTime:today,
            timeframe:"day",
            symbol:line //ex: 005930
        }
    };
    request(options,function(err,response,body){
        console.log(body);
        fs.writeFile('./' + line + '.txt', body, 'utf8', function(error){
            console.log('stock write end')
        });
    });

    r.prompt()
});

r.on('close', function() {process.exit();});
