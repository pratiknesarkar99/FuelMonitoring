#include <ArduinoJson.h>

#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>

/*Firebase Credentials*/
#define FIREBASE_HOST "fuel-monitoring-e60fd.firebaseio.com"
#define FIREBASE_AUTH "rXGhpKnknCVCl6QYFfLiFfuHLMXn6Z4LSUnqIk6p"

/*Wifi Credentials*/
#define WIFI_SSID "MyWiFi"
#define WIFI_PASSWORD "12345678"

const int inSensorPin = 0;                                //This is the input pin GPIO0 on the ESP8266
const int outSensorPin = 2;                               //This is the input pin GPIO2 on the ESP8266

float inflowRate, outflowRate;                            //To calculate the Fuel Flow in (L/min)
float inflowMililitresSec, outflowMililitresSec;          //To calculate the Fuel Flow in (mL/sec)
float intotalMililitres, outtotalMililitres;              //To calculate the Total Fuel in (mL)
float intotalLitres, outtotalLitres;                      //To calculate the Total Fuel in (L)
float oldintotalLitres, oldouttotalLitres;                //To calculate the Total Fuel in (L)

volatile int inPulseCount;                                //This integer needs to be set as volatile to ensure it updates correctly during the interrupt process.
volatile int outPulseCount;                               //This integer needs to be set as volatile to ensure it updates correctly during the interrupt process.

String uid;                                               //This string contains the ID of the user
String inpath, outpath;                                   //This string indicates the path to Firebase Database

boolean inFlag;                                           //To check whether one time fuel input is fully calculated 
boolean outFlag;                                          //To check whether one time fuel output is fully calculated 

ICACHE_RAM_ATTR void incrementInputPulse() {              //ISR to be called upon interrupt
  inFlag = false;
  inPulseCount++;                                         //Every time this function is called, increment "count" by 1
}

ICACHE_RAM_ATTR void incrementOutputPulse() {             //ISR to be called upon interrupt
  outFlag = false;
  outPulseCount++;                                       //Every time this function is called, increment "count" by 1
}

void setup() {
  Serial.begin(9600);

  pinMode(inSensorPin, INPUT);                            //Sets the pin as an input
  pinMode(outSensorPin, INPUT);                            //Sets the pin as an input

  /*Initialize Values*/
  inPulseCount = 0;
  inflowMililitresSec = 0;
  intotalMililitres = 0;
  intotalLitres = 0;
  oldintotalLitres = intotalLitres;

  outPulseCount = 0;
  outflowMililitresSec = 0;
  outtotalMililitres = 0;
  outtotalLitres = 0;
  oldouttotalLitres = outtotalLitres;

  inFlag = false;
  outFlag = false;

  /*Replace with uID from firebase*/
  uid = "LBF6YOLd2uOtc7vN0R0hTHkXDJ12";

  inpath = "/FuelIn/" + uid + "/value";
  outpath = "/FuelOut/" + uid + "/value";

  /*Connect to wifi*/
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Connecting, Please wait!!!");

  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(500);
  }

  Serial.println();
  Serial.print("Connection successful: ");
  Serial.println(WiFi.localIP());

  /*Connect to Firebase*/
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);

  displayInData();
}

/*This section will be running in a continuous loop*/
void loop() {

  flowSensorIn();

  flowSensorOut();
}

/*This function carries out all the important tasks needed for the Fuel (Input) Sensor*/
void flowSensorIn() {
  inPulseCount = 0;                                          // Reset the counter so we start counting from 0 again
  oldintotalLitres = intotalLitres;                          // To check if the value of input has changed or not             

  /*Configures interrupt 0 (pin 2 on the Arduino Uno) to run the function "incrementInputPulse"*/
  attachInterrupt(digitalPinToInterrupt(inSensorPin), incrementInputPulse, RISING);
  delay(1000);                                              //Wait 1 second
  detachInterrupt(digitalPinToInterrupt(inSensorPin));  //Disable the interrupts on the Arduino

  /*Calculate the input fuel amount*/
  inflowMililitresSec = (inPulseCount * 4.5);
  intotalMililitres += inflowMililitresSec;                 //Convert flowrate mL/s to Liters, giving you total mL

  intotalLitres = intotalMililitres / 1000;                 //Convert total mL to Liters, giving you final amount (Liters) to push 

  if (inPulseCount == 0 && intotalLitres != 0 && intotalLitres == oldintotalLitres) {
    if (!inFlag) {
      updateFuelInput();
      displayInData();

      resetInValues();
      inFlag = true;
    }
  }
}

/*Update the value of fuel amount filled in to the tank*/
void updateFuelInput() {
  Serial.println("[Firebase] Pushing the updated value...");
  Firebase.setString(inpath, (String)intotalLitres);        //Set the variable at remote database (Firebase)
  
  // handle error
  if (Firebase.failed()) {
    Serial.println("[Firebase] Failed to update the value!!!:");
    Serial.println("[Firebase] Retrying...");
    Firebase.setString(inpath, (String)intotalLitres);      //Retry the same operation at remote database (Firebase)
    if(Firebase.success()){
        Serial.println("[Firebase] Value pushed successfully...");  
    }
  }
}

/*This function carries out all the important tasks needed for the Fuel (Output) Sensor*/
void flowSensorOut() {
  outPulseCount = 0;                                          // Reset the counter so we start counting from 0 again
  oldouttotalLitres = outtotalLitres;                          // To check if the value of input has changed or not             

  /*Configures interrupt 0 (pin 2 on the Arduino Uno) to run the function "incrementInputPulse"*/
  attachInterrupt(digitalPinToInterrupt(outSensorPin), incrementOutputPulse, RISING);
  delay(1000);                                              //Wait 1 second
  detachInterrupt(digitalPinToInterrupt(outSensorPin));  //Disable the interrupts on the Arduino

  /*Calculate the input fuel amount*/
  outflowMililitresSec = (outPulseCount * 5.8);
  outtotalMililitres += outflowMililitresSec;                 //Convert flowrate mL/s to Liters, giving you total mL

  outtotalLitres = outtotalMililitres / 1000;                 //Convert total mL to Liters, giving you final amount (Liters) to push 

  if (outPulseCount == 0 && outtotalLitres != 0 && outtotalLitres == oldouttotalLitres) {
    if (!outFlag) {
      updateFuelOutput();
      displayOutData();

      resetOutValues();
      outFlag = true;
    }
  }
}

/*Update the value of fuel amount that leaves the tank*/
void updateFuelOutput() {
  Serial.println("[Firebase] Pushing the updated value...");
  Firebase.setString(outpath, (String)outtotalLitres);        //Set the variable at remote database (Firebase)
  
  // handle error
  if (Firebase.failed()) {
    Serial.println("[Firebase] Failed to update the value!!!:");
    Serial.println("[Firebase] Retrying...");
    Firebase.setString(inpath, (String)outtotalLitres);      //Retry the same operation at remote database (Firebase)
    if(Firebase.success()){
        Serial.println("[Firebase] Value pushed successfully...");  
    }
  }
}

/*Reset all the Input Sensor values*/
void resetInValues() {
  inflowMililitresSec = 0;
  intotalMililitres = 0;
  intotalLitres = 0;
  oldintotalLitres = intotalLitres;
}

/*Reset all the Input Sensor values*/
void resetOutValues() {
  outflowMililitresSec = 0;
  outtotalMililitres = 0;
  outtotalLitres = 0;
  oldouttotalLitres = outtotalLitres;
}

/*Display all the data to Serial Monitor before sending [Debugging Purpose]*/
void displayInData() {
  Serial.println("[Flow Sensor: (Input)]");
/*
  Serial.print("Pulse count: ");
  Serial.print(inPulseCount);           //Print the Rate of Fuel Flow to Serial Monitor
  Serial.print("\t|\t");

  Serial.print("Total Mililitres: ");
  Serial.print(intotalMililitres);   //Print the Total Amount of fuel in (mL) to Serial Monitor
  Serial.print("mL\t|\t");
*/
  Serial.print("Total Litres: ");
  Serial.print(intotalLitres);         //Print the variable inflowRate to Serial
  Serial.println("L");
}

/*Display all the data to Serial Monitor before sending [Debugging Purpose]*/
void displayOutData() {
  Serial.println("[Flow Sensor: (Output)]");

/*  Serial.print("Pulse count: ");
  Serial.print(outPulseCount);           //Print the Rate of Fuel Flow to Serial Monitor
  Serial.print("\t|\t");

  Serial.print("Total Mililitres: ");
  Serial.print(outtotalMililitres);   //Print the Total Amount of fuel in (mL) to Serial Monitor
  Serial.print("mL\t|\t");
*/
  Serial.print("Total Litres: ");
  Serial.print(outtotalLitres);         //Print the variable inflowRate to Serial
  Serial.println("L");
}
