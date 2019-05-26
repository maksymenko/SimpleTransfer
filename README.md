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


https://github.com/despegar/spark-test
https://javadoc.io/doc/com.sparkjava/spark-core/2.9.1
https://www.eviltester.com/2018/04/overview-of-spark-and-testing.html
https://github.com/eviltester/TestingApp/blob/master/java/testingapps/sparktesting/src/main/java/uk/co/compendiumdev/sparktesting/SparkStarter.java