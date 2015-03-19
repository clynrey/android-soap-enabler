# What do I need to use Android SOAP Enabled #

Android SOAP Enabler is made for people crafting Android Application, so the requirements are almost the same as mentioned in Android developer home site(URL HERE).

## Java Developmnet Kit ##

You must have the latest JDK installed on your system, make sure that $JAVA\_HOME (or %JAVA\_HOME%) is set and points to the JDK root directory. It's properly installed when the following command success :
```
$ java -v
```

## Android Software Development Kit ##

You must have the latest Android SDK installed on your system. It's properly installed when the following command success :
```
$ adb devices
```
Use the following command in order to install the platform SDK you need for the target Android version :
```
$ android
```
It launch a graphical user interface that will fetch the target platform available to download.

## Apache Maven ##

You should have Apache Maven 3.0.3 or later installed on your system, make sure that $M2\_HOME (or %M2\_HOME%) is set and points to Maven JDK root directory. Its properly installed when the following command success :
```
$ mvn -v
```

## An Internet access ##

Because Maven download some jars needed to run, it is recommended to run those tools online at first time. You will be able to work offline after this time.

# What can I do if it does not work #

If Android SOAP Enabler fail to generate your Web Service stub source, feel free to comment an existing issue or create a new one.
Don't forget to indicate us :
  * the steps you made,
  * what went wrong,
  * the WSDL file you used to generate from
In other words, help us to help you ;)