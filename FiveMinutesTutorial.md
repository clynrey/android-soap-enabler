

---


# 0. Prerequisite #

First of all, be sure to get all the needed by [following this instructions](WhatDoINeed.md).

# 1. The Android Application #

Create a new application [using this tutorial](http://developer.android.com/guide/developing/projects/projects-eclipse.html).
Let's call it Sample, target the 1.6 runtime, and have a default Activity named SampleAppActivity.
Run for the first time the application in order to check it's running fine.

# 2. The WSDL Contract #

Download or 'wget' the following WSDL file in Sample/wsdl/tempconvert.wsdl
```
$ wget http://www.w3schools.com/webservices/tempconvert.asmx?WSDL -O tempconvert.wsdl
```

# 3. Mavenization #

## 3.1. The POM ##

Create the file Sample/pom.xml with the following content :
```
<?xml version="1.0" encoding="UTF-8"?>
<project
  xmlns="http://maven.apache.org/POM/4.0.0"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
                      http://maven.apache.org/maven-v4_0_0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>fr.norsys.asoape.it</groupId>
  <artifactId>SampleApp</artifactId>
  <version>1.0-SNAPSHOT</version>
  <packaging>apk</packaging>
  <properties>
    <asoape.version>1.1</asoape.version>
  </properties>
  <dependencies>
    <dependency>
      <groupId>fr.norsys.asoape</groupId>
      <artifactId>runtime-library</artifactId>
      <version>${asoape.version}</version>
    </dependency>
    <dependency>
      <groupId>com.google.android</groupId>
      <artifactId>android</artifactId>
      <version>1.6_r2</version>
      <scope>provided</scope>
    </dependency>
  </dependencies>
  <build>
    <finalName>${project.artifactId}</finalName>
    <sourceDirectory>src</sourceDirectory>
    <plugins>
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
            <configuration>
              <definitionsDirectory>${project.basedir}/wsdl</definitionsDirectory>
            </configuration>
          </execution>
        </executions>
      </plugin>
      <plugin>
        <groupId>com.jayway.maven.plugins.android.generation2</groupId>
        <artifactId>android-maven-plugin</artifactId>
        <version>3.0.0</version>
        <extensions>true</extensions>
        <configuration>
          <genDirectory>gen</genDirectory>
          <sdk>
            <path>${env.ANDROID_HOME}</path>
            <platform>4</platform>
          </sdk>
          <deleteConflictingFiles>true</deleteConflictingFiles>
          <undeployBeforeDeploy>true</undeployBeforeDeploy>
        </configuration>
        <executions>
          <execution>
            <id>alignApk</id>
            <phase>package</phase>
            <goals>
              <goal>zipalign</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
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
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-eclipse-plugin</artifactId>
        <version>2.8</version>
        <configuration>
          <additionalBuildcommands>
            <buildcommand>com.android.ide.eclipse.adt.ResourceManagerBuilder</buildcommand>
            <buildcommand>com.android.ide.eclipse.adt.PreCompilerBuilder</buildcommand>
            <buildcommand>com.android.ide.eclipse.adt.ApkBuilder</buildcommand>
          </additionalBuildcommands>
          <additionalProjectnatures>
            <projectnature>com.android.ide.eclipse.adt.AndroidNature</projectnature>
          </additionalProjectnatures>
          <classpathContainers>
            <classpathContainer>com.android.ide.eclipse.adt.ANDROID_FRAMEWORK</classpathContainer>
          </classpathContainers>
          <downloadSources>true</downloadSources>
          <excludes>
            <exclude>com.google.android:android</exclude>
            <exclude>commons-logging:commons-logging</exclude>
            <exclude>org.apache.httpcomponents:httpclient</exclude>
            <exclude>org.apache.httpcomponents:httpcore</exclude>
            <exclude>commons-codec:commons-codec</exclude>
            <exclude>org.khronos:opengl-api</exclude>
            <exclude>xerces:xmlParserAPIs</exclude>
            <exclude>xpp3:xpp3</exclude>
          </excludes>
        </configuration>
      </plugin>
    </plugins>
  </build>
</project>
```

## 3.2. Refresh the project ##

Let's generate some source code with the following commande :
```
$> mvn generate-sources eclipse:eclipse
```
Use F5 to refresh the project in Eclispe and run it to figure out that the project is still running.

# 4. Code'em all #

## 4.1. The UI ##

First of all, build the UI.

### 4.1.1. The Layout ###

Copy/paste the following code into Sample/res/layout/main.xml file.
```
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
  android:layout_width="fill_parent"
  android:layout_height="fill_parent"
  android:orientation="vertical" >
  <EditText
    android:id="@+id/input"
    android:layout_margin="5dp"
    android:layout_width="fill_parent"
    android:layout_height="wrap_content"
    android:layout_alignParentLeft="true"
    android:layout_alignParentTop="true"
    android:hint="@string/input"
    android:inputType="numberDecimal" />
  <Button
    android:id="@+id/button1"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_below="@+id/input"
    android:layout_alignParentLeft="true"
    android:layout_marginLeft="45dp"
    android:text="@string/toCelsius"
    android:onClick="convertToCelsius" />
  <Button
    android:id="@+id/button2"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_below="@+id/input"
    android:layout_alignParentRight="true"
    android:layout_marginRight="45dp"
    android:text="@string/toFahrenheit" 
    android:onClick="convertToFahrenheit"/>
  <EditText
    android:id="@+id/output"
    android:hint="@string/output"
    android:editable="false"
    android:layout_margin="5dp"
    android:layout_width="fill_parent"
    android:layout_height="wrap_content"
    android:layout_below="@+id/button1"
    android:inputType="numberDecimal" >
  </EditText>
</RelativeLayout>
```

### 4.1.2. The Strings ###

Copy/paste the following code into Sample/res/values/strings.xml file.
```
<?xml version="1.0" encoding="utf-8"?>
<resources>
  <string name="hello">Hello World, SampleAppActivity!</string>
  <string name="app_name">SampleApp</string>
  <string name="input">Input Value</string>
  <string name="output">Output Value</string>
  <string name="toCelsius">° F -> ° C</string>
  <string name="toFahrenheit">° C-> ° F</string>
</resources>
```

## 4.2. The Activity ##

Here is the code of the default activity :
```
public class SampleAppActivity
  extends Activity
{
  /** Remote Web Service. */
  private TempConvertSoap service;

  /** User input component. */
  private EditText input;

  /** User output component. */
  private EditText output;

  /** Handler to multithread the call. */
  private Handler handler;

  /** Called when the activity is first created. */
  @Override
  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.main );
    handler = new Handler();
    input = (EditText) findViewById( R.id.input );
    output = (EditText) findViewById( R.id.output );
    try
    {
      service = new TempConvertSoapImpl();
    }
    catch ( Exception exception )
    {
    }
  }

  /**
   * Call by the UI.
   */
  public void convertToCelsius( View view )
  {
    FahrenheitToCelsius request = new FahrenheitToCelsius();
    request.setFahrenheit( input.getText().toString() );
    service.FahrenheitToCelsius( request, new WSCallBack<FahrenheitToCelsiusResponse>()
    {
      public void onSuccess( FahrenheitToCelsiusResponse result )
      {
        updateOutput( result.getFahrenheitToCelsiusResult() );
      }

      public void onFailure( Throwable throwable )
      {
        updateOutput( throwable.getMessage() );
      }
    } );
  }

  /**
   * Call by the UI.
   */
  public void convertToFahrenheit( View view )
  {
    CelsiusToFahrenheit request = new CelsiusToFahrenheit();
    request.setCelsius( input.getText().toString() );
    service.CelsiusToFahrenheit( request, new WSCallBack<CelsiusToFahrenheitResponse>()
    {
      public void onSuccess( CelsiusToFahrenheitResponse result )
      {
        updateOutput( result.getCelsiusToFahrenheitResult() );
      };

      public void onFailure( Throwable throwable )
      {
        updateOutput( throwable.getMessage() );
      }
    } );
  }

  private void updateOutput( final String string )
  {
    handler.post( new Runnable()
    {
      public void run()
      {
        output.setText( string );
      }
    } );
  }
}
```

# 5. Time to Convert #

Run the projet again, the simulator show the new application interface.
Enter a number and ask some comversion, the result will show up in the text box at the bottom.

# 6. Voilà! #

;)