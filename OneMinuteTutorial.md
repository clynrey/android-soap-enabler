

---

# I've no more time to waste, how it works ? #

Welcome on board, let's go!

## 0. Prerequisite ##

First of all, be sure to get all the needed by [following this instructions](WhatDoINeed.md)

## 1. The POM ##

Copy the following Maven Projet model and paste in a file name pom.xml located in a new project directory (let's name it $PROJECT\_ROOT):
```
<?xml version="1.0" encoding="UTF-8"?>
<project
  xmlns="http://maven.apache.org/POM/4.0.0"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
                      http://maven.apache.org/maven-v4_0_0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>org.asoape.sample</groupId>
  <artifactId>contact</artifactId>
  <version>1.0-SNAPSHOT</version>
  <packaging>jar</packaging>
  <properties>
    <asoape.version>1.0</asoape.version>
  </properties>
  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>2.3.2</version>
        <configuration>
          <source>1.5</source>
          <target>1.5</target>
        </configuration>
      </plugin>
      <plugin>
        <groupId>fr.norsys.asoape</groupId>
        <artifactId>asoape-maven-plugin</artifactId>
        <version>${asoape.version}</version>
        <executions>
          <execution>
            <goals>
              <goal>generate-soap-stub</goal>
            </goals>
            <phase>generate-sources</phase>
          </execution>
        </executions>
      </plugin>
    </plugins>
  </build>
  <dependencies>
    <dependency>
      <groupId>fr.norsys.asoape</groupId>
      <artifactId>runtime-library</artifactId>
      <version>${asoape.version}</version>
    </dependency>
    <dependency>
      <groupId>com.google.android</groupId>
      <artifactId>android</artifactId>
      <version>1.5_r4</version>
      <scope>provided</scope>
    </dependency>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.8.2</version>
      <scope>test</scope>
    </dependency>
  </dependencies>
</project>
```

## 2. The WSDL Contract ##

Download or 'wget' the following WSDL file in $PROJECT\_ROOT/src/main/wsdl/tempconvert.wsdl
```
$ wget http://www.w3schools.com/webservices/tempconvert.asmx?WSDL -O tempconvert.wsdl
```

## 3. Run Android SOAP Enabler ##

Execute the following command in $PROJECT\_ROOT directory in order to generate the SOAP stub source file.
```
$ mvn clean generate-sources
```

## 4. Calling ~~Elvis~~ the Web Service ##

Copy the following Java source content and paste it in a file name TempConvertTest.java located in $PROJECT\_ROOT/src/test/java ...
```
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import java.net.URL;
import org.junit.Before;
import org.junit.Test;
import org.tempuri.CelsiusToFahrenheit;
import org.tempuri.CelsiusToFahrenheitResponse;
import org.tempuri.TempConvertSoap;
import org.tempuri.TempConvertSoapImpl;

public class TempConvertTest
{
    private TempConvertSoap tempconvert;

    @Before
    public void setUp()
        throws Exception
    {
        tempconvert = new TempConvertSoapImpl();
    }

    @Test
    public void test()
        throws Exception
    {
        CelsiusToFahrenheit request = new CelsiusToFahrenheit();
        request.setCelsius("100");
        CelsiusToFahrenheitResponse response = tempconvert.CelsiusToFahrenheit(request);
        assertThat(response.getCelsiusToFahrenheitResult(), is(equalTo("212")));
    }
}
```

... and run the following command :
```
$ mvn test
```

## 5. Voilà! ##

;)