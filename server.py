#!/opt/python-3.4/linux/bin/python3

import sys
import random
from socket import *

def help():
  s = '''
  server.py - server program for timestamp and integer ID stream

  USAGE:
    server.py -h
    server.py <#data> <min> <max>

  OPTIONS:
    -h   get this help page
    <#data> number of data (default is 100000)
    <min> minimum delay (default is 1000)
    <max> maximum delay (default is 2000)

  EXAMPLE:
    server.py -h
    server.py 1000 500 1500

  CONTACT:
    Ming-Hwa Wang, Ph.D. 408/805-4175  m1wan@scu.edu
  '''
  print(s)
  raise SystemExit(1)

num, min, max = (100000, 5000, 10000)
if len(sys.argv) == 2 and sys.argv[1] == "-h":
  help()
elif len(sys.argv) == 4:
  num = int(sys.argv[1])
  min = int(sys.argv[2])
  max = int(sys.argv[3])
  if num <= 0 or min <= 0:
    help()
  if min > max:
    help()
else:
  help()

s = socket(AF_INET, SOCK_STREAM)
s.bind(('', 0))
host, port = s.getsockname()
print("connect to port number %s" % port)
s.listen(10)
while True:
  client, addr = s.accept()
  print("Got a connection from %s" % str(addr))
  random.seed(32767)
  for i in range(num):
    j = random.randint(0,65535)
    x = str(i)
    x = x + " "
    x = x + str(j)
    x = x + "\n"

#    print(x)
    if i%4 == 0:
        x = "10 " + str(j) + "\n"
    client.send(x.encode('ascii'))
    for k in range(random.randint(min,max)):
      j = j + k
  print("closing connection")
  client.close()
