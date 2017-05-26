Dependency:

Note: this app has been tested on below versions:
1. java version "1.8.0_121"
2. Apache Maven 3.2.5

Configuration:

1. login to your bluemix account and get the credentials for below services:

a. Speech to Text
b. Text to Speech
c. Visual Recognition

2. Update these credentials in /scavengerHunt/src/main/java/com/ibm/watson/scavenger/util/ScavengerContants.java
3. Run the maven build through 'mvn clean install'
4. Start the application by running /scavengerHunt/src/main/java/com/ibm/watson/scavenger/App.java main method.

How to play with this App:

once you have listened the welcome message, you need to speak any of below words:
'game' or 'scavenger hunt game' or 'hunt game'

After this camera capture window will launch along with Watson visual recognition prediction result window.
You need to click on any where on camera capture window to capture the image. Once image is captured it will be shown on prediction window with Watson predictions. 

at any point of time you can say any of below keyword to exit your app:
'exit' or 'i am done' or 'i'm done'.