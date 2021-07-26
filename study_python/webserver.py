import http.server
import socketserver
import threading

from main import stock_launch


STOCK_CODES = ["005930", "293490", "181710", "036570", "003550"]

def stock_launch2(args):
  return "code"

class MyHandler(http.server.SimpleHTTPRequestHandler):

  def do_GET(self):
    data = ""
    if self.path == '/sync/stocks':
      for stockCode in STOCK_CODES:
        data += stock_launch(stockCode) + "<br/>"

    elif self.path == '/async/stocks':
      threads = [str]
      for i in range(len(STOCK_CODES)):
        print(STOCK_CODES[i])
        th = threading.Thread(target = stock_launch2, args=('005930',))
        threads.append(th)
        th.start()

      print("----")
      for thread in threads:
        data = thread.join()

    else:
      return

    self.send_response(200)
    self.send_header('Content-Type', 'text/html; charset=utf-8')
    self.end_headers()
    self.wfile.write(data.encode('utf-8'))



with socketserver.TCPServer(('127.0.0.1', 8004), MyHandler) as httpd:
  print('Web server start...')
  httpd.serve_forever()