#include <DHT_Async.h> // https://docs.arduino.cc/libraries/dht-sensors-non-blocking/
#include <Wire.h>

#define DHT_SENSOR_TYPE DHT_TYPE_11 // Tipo do sensor
static const int DHT_SENSOR_PIN = 8; // Pino do sensor

DHT_Async dhtSensor(DHT_SENSOR_PIN, DHT_SENSOR_TYPE); // Inicializa o sensor

void setup( )
{
  Serial.begin(9600);
}

void loop( )
{
  float temperature;
  float humidity;

  if(dhtSensor.measure(&temperature, &humidity)) // Realiza as medições
  {
    Serial.print( "T = " );
    Serial.print( temperature, 1 );
    Serial.print( " deg. C, H = " );
    Serial.print( humidity, 1 );
    Serial.println( "%" );
  }
}