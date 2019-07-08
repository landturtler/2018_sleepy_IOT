import cgi 
import RPi.GPIO as gpio
import os
from BaseHTTPServer import HTTPServer
from SimpleHTTPServer import SimpleHTTPRequestHandler

#Actuator define BCM GPIO Pin - is not wiringPi Pin number
PUMP = 5
FAN = 6
MOTOR = 13
RGB = 19

RED = 4
GREEN = 3
BLUE = 2

class Handler(SimpleHTTPRequestHandler):
    def do_POST(self):
        if self.path == '/rccar':
            form = cgi.FieldStorage(fp=self.rfile, 
                                    headers=self.headers,
                                    environ={'REQUEST_METHOD':'POST'})
            cmd = form['cmd'].value
            print cmd

            if cmd == "PUMPON":
                gpio.output(PUMP, gpio.HIGH)                
            elif cmd == "PUMPOFF":
				gpio.output(PUMP, gpio.LOW) 
            elif cmd == "FANON":
				gpio.output(FAN, gpio.HIGH)
            elif cmd == "FANOFF":
				gpio.output(FAN, gpio.LOW)
            elif cmd == "MOTORON":
				gpio.output(MOTOR, gpio.HIGH)
            elif cmd == "MOTOROFF":
				gpio.output(MOTOR, gpio.LOW)
            elif cmd == "RGBON":
				gpio.output(RGB, gpio.HIGH)
				gpio.output(RED, gpio.HIGH)
				gpio.output(GREEN, gpio.HIGH)
				gpio.output(BLUE, gpio.HIGH)
            elif cmd == "RGBOFF":
				gpio.output(RGB, gpio.LOW)

            self.send_response(100)
            self.send_header('Content-type', 'text/html')

            return

        return self.do_GET() 

gpio.setwarnings(False)
gpio.setmode( gpio.BCM ) 

#Pin Output Setup
gpio.setup(PUMP, gpio.OUT)
gpio.setup(FAN, gpio.OUT)
gpio.setup(MOTOR, gpio.OUT)
gpio.setup(RGB, gpio.OUT)

gpio.setup(RED, gpio.OUT)
gpio.setup(GREEN, gpio.OUT)
gpio.setup(BLUE, gpio.OUT)

#Pin Initialization
gpio.output(PUMP, gpio.LOW)
gpio.output(FAN, gpio.LOW)
gpio.output(MOTOR, gpio.LOW)
gpio.output(RGB, gpio.LOW)

gpio.output(RED, gpio.LOW)
gpio.output(GREEN, gpio.LOW)
gpio.output(BLUE, gpio.LOW)
os.system("./MySleep")

server = HTTPServer(('', 8002), Handler).serve_forever()
