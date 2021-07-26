import requests
import sys
import datetime

#python -m pip install requests

def stock_launch(stockCode):
    URL = 'https://api.finance.naver.com/siseJson.naver'

    now = datetime.datetime.now()
    today = now.strftime("%Y%m%d")

    response = requests.get(URL, {'requestType': 1, 'startTime': today, 'endTime': today, 'timeframe': 'day',
                                  'symbol': stockCode})
    print(stockCode)
    return response.text

def stock_launch_cmd():
    while 1:
        cmd = input('> 주식코드를 입력해주세요. [quit: q] : ') #ex: 005930 삼성
        if cmd == 'q':
            sys.exit()

        stockInfo = stock_launch(cmd)

        fs = open('./' + cmd + '.json', 'w')
        fs.write(stockInfo)
        fs.flush()
        fs.close()


if __name__ == '__main__':
    stock_launch_cmd()
