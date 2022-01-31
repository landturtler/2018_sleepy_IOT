#include <signal.h> //Signal 사용 헤더파일
#include <stdio.h>
#include <string.h>
#include <errno.h>
#include <stdlib.h>
#include <stdint.h>
#include <sys/types.h>
#include <wiringPi.h>
#include <unistd.h>


// Use GPIO Pin 17, which is Pin 0 for wiringPi library

#define MAXTIMINGS 85
#define MOTION_IN 0  // 모터 
#define LIGHTCONTROL  24	// 빛
#define FAN	22 // 선풍기
#define PUMP	21 // 펌프
#define BUZCONTROL  28	// 부저
int ret_humid, ret_temp;

//static int DHTPIN = 7;
static int DHTPIN = 11;

static int dht22_dat[5] = {0,0,0,0,0};

static uint8_t sizecvt(const int read)
{
  /* digitalRead() and friends from wiringpi are defined as returning a value
  < 256. However, they are returned as int() types. This is a safety function */

  if (read > 255 || read < 0)
  {
    printf("Invalid data from wiringPi library\n");
    exit(EXIT_FAILURE);
  }
  return (uint8_t)read;
}

int read_dht22_dat()
{
	uint8_t laststate = HIGH;
  	uint8_t counter = 0;
  	uint8_t j = 0, i;

	dht22_dat[0] = dht22_dat[1] = dht22_dat[2] = dht22_dat[3] = dht22_dat[4] = 0;

	// pull pin down for 18 milliseconds
	pinMode(DHTPIN, OUTPUT);
	digitalWrite(DHTPIN, HIGH);
	delay(10);
	digitalWrite(DHTPIN, LOW);
	delay(18);
	// then pull it up for 40 microseconds
	digitalWrite(DHTPIN, HIGH);
	delayMicroseconds(40); 
	// prepare to read the pin
	pinMode(DHTPIN, INPUT);

	// detect change and read data
	for ( i=0; i< MAXTIMINGS; i++) {
		counter = 0;
		while (sizecvt(digitalRead(DHTPIN)) == laststate) {
			counter++;
			delayMicroseconds(1);
			if (counter == 255) {
				break;
			}
		}
		laststate = sizecvt(digitalRead(DHTPIN));

		if (counter == 255) break;

		// ignore first 3 transitions
		if ((i >= 4) && (i%2 == 0)) {
		// shove each bit into the storage bytes
		dht22_dat[j/8] <<= 1;
		if (counter > 16)
			dht22_dat[j/8] |= 1;
		j++;
		}
	}

	// check we read 40 bits (8bit x 5 ) + verify checksum in the last byte
	// print it out if data is good
	if ((j >= 40) && 
		(dht22_dat[4] == ((dht22_dat[0] + dht22_dat[1] + dht22_dat[2] + dht22_dat[3]) & 0xFF)) ) {
			float t, h;
			
			h = (float)dht22_dat[0] * 256 + (float)dht22_dat[1];
			h /= 10;
			t = (float)(dht22_dat[2] & 0x7F)* 256 + (float)dht22_dat[3];
			t /= 10.0;
			if ((dht22_dat[2] & 0x80) != 0)  t *= -1;
			
			ret_humid = (int)h;
			ret_temp = (int)t;
			//printf("Humidity = %.2f %% Temperature = %.2f *C \n", h, t );
			//printf("Humidity = %d Temperature = %d\n", ret_humid, ret_temp);
			
		return ret_temp;
	}
	else{
		return 0;
	}
}

// the event counter 
volatile int eventCounter = 0;
unsigned char humandetect = 0;
int counter = 0;


// -------------------------------------------------------------------------
// myInterrupt:  called every time an event occurs
void myInterrupt(void) {
   eventCounter++;
   humandetect = 1;

}


// -------------------------------------------------------------------------
// main
int main(void) {
  // sets up the wiringPi library
	if (wiringPiSetup () < 0) {
		fprintf (stderr, "Unable to setup wiringPi: %s\n", strerror (errno));
		return 1;
	}

	// set Pin 17/0 generate an interrupt on high-to-low transitions
	// and attach myInterrupt() to the interrupt
	if ( wiringPiISR (MOTION_IN, INT_EDGE_RISING, &myInterrupt) < 0 ) {
		fprintf (stderr, "Unable to setup ISR: %s\n", strerror (errno));
		return 1;
		}	

	// display counter value every second.
	while ( 1 ) {
		if(humandetect == 1)
		{	
			pinMode (PUMP, OUTPUT) ;
			pinMode (LIGHTCONTROL, OUTPUT);
			pinMode (FAN, OUTPUT) ;
			printf("잠에서 깨고 있는 것 같습니다.\n");
			printf("센서가 감지되었습니다.\n");
			humandetect = 0;
			while(digitalRead(MOTION_IN)){
				//빛 나오게하기
				digitalWrite(LIGHTCONTROL, 1);
				//빛 끝

				//모터나오게하기
				digitalWrite (PUMP, 1) ; // On
				//모터 끝

				//선풍기
				digitalWrite (FAN, 1) ; // On
				
				//부저나오게하기
				digitalWrite(BUZCONTROL, 0);
				//부저끝
			}
			counter = 0;
		}
		else{

			printf("센서가 감지되지 않습니다\n");
			int a;
			if(a==67600){
					
					printf("일어나세요!!!!\n");
					
					pinMode (BUZCONTROL, OUTPUT);
					
					//delay(1000);
						
					digitalWrite(BUZCONTROL, 0);
				
			}
			int received_temp;
			
			received_temp = ret_temp ;
			if(received_temp > 30){
				printf("활동 중 입니다.");
			}
			else if(received_temp <=26){
				printf("렘 수면에 잠든 것 같습니다.\n");
				digitalWrite(LIGHTCONTROL, 0);
				digitalWrite (PUMP, 0) ; // Off
				digitalWrite (FAN, 0) ; // Off
			}
			else{
				printf("렘 수면이 아닌 얕은 수면에 잠든 것 같습니다.\n");
			}
			a++;
			
		}		
		//eventCounter = 0;
		delay( 500 ); // wait 1 second
	}
	int received_temp;

		if (wiringPiSetup() == -1)
			exit(EXIT_FAILURE) ;
			
		if (setuid(getuid()) < 0){
			perror("Dropping privileges failed\n");
			exit(EXIT_FAILURE);
		}
		while (read_dht22_dat() == 0){
			delay(500); // wait 1sec to refresh
		}
		received_temp = ret_temp ;
		if(received_temp > 60){
			while(1){	
			printf("불이났습니다!!!!!!!!!!! \n");
			
			pinMode (BUZCONTROL, OUTPUT);
				
			digitalWrite(BUZCONTROL, 1);
			}
		}	
	return 0;
}

void sig_handler(int signo)
{
    printf("process stop\n");
	digitalWrite(LIGHTCONTROL, 0);
	digitalWrite (PUMP, 0) ; // Off
	digitalWrite (FAN, 0) ; // Off
	digitalWrite(BUZCONTROL, 0);
	
	exit(0);
} 
