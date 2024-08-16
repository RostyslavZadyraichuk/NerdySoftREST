# NerdySoft REST
### Test task by Zadyraichuk Rostyslav

Simple REST web-application for library.

## Preparing

Application requires **[Java v17+](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)** to run.
Before running download **JDK v17+**,create _JAVA_HOME_ system environment variable and add %JAVA_HOME%/bin 
in system variable _Path_. 

_Before running check java version in cmd:_
```
java -version
```

## Installation
1. Download **nerdysoft-rest-test-0.0.1-SNAPSHOT.jar** archive from _jar_ directory.
2. _Additionally:_ download **nerdysoft_rest.mv.db** database from _jar_ directory and move it in 
folder **current_user-folder/nerdysoft_rest/** on your machine.
2. Inside directory with **nerdysoft-rest-test-0.0.1-SNAPSHOT.jar** run following command in _cmd_:
    ```
    java -jar ./nerdysoft-rest-test-0.0.1-SNAPSHOT.jar
    ```
3. Open any browser and input in address line **localhost:8080/** that open main page.

## User URLs
1. **/books** - show and execute appropriate commands with books
2. **/members** - show and execute appropriate commands with members
3. **/borrows** - show and execute appropriate commands with borrows
4. **/h2-console** - manual interaction with database

**Login data for H2 console:**</br>
_Driver Class:_ org.h2.Driver</br>
_JDBC URL:_ jdbc:h2:file:~/nerdysoft_rest/nerdysoft_rest</br>
_User Name:_ sa</br>
~~_Password:_ no password~~