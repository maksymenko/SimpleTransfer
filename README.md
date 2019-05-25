## SimpleTransfer
* Sample app exposes rest api to make transfer from one user to another.


### Precondition
* Tools below has to be available to start
  ** Gradle 5+
  ** JDK 8+


### Quick Start
```
gradle execute
```

### Usage
```
curl http://localhost:4567/hello; echo $line;
```

### Build artifact and run
```
gradlew build
java -jar ./build/libs/SimpleTransfer-0.0.1-SNAPSHOT.jar
```

### Debug
```
gradle execute  --debug-jvm
```
