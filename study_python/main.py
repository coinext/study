import requests
import sys
import datetime

#python -m pip install requests

def stock_launch():
    while 1:
        cmd = input('> 주식코드를 입력해주세요. [quit: q] : ') #ex: 005930 삼성
        if cmd == 'q':
            sys.exit()

        URL = 'https://api.finance.naver.com/siseJson.naver'

        now = datetime.datetime.now()
        today = now.strftime("%Y%m%d")

        response = requests.get(URL, {'requestType': 1, 'startTime': today, 'endTime':today, 'timeframe':'day', 'symbol':cmd})
        stockInfo = response.text

        fs = open('./' + cmd + '.json', 'w')
        fs.write(stockInfo)
        fs.flush()
        fs.close()


if __name__ == '__main__':
    stock_launch()
