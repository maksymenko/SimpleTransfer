## SimpleTransfer
 Sample app exposes rest api to make transfer from one account to another. Updates both accounts balance in one transaction.
 Doesn't maintain transaction history.
 Uses Spark Framework for REST API and Guice as IoC container.

 ### Provides API below
 * Create account
 ```
 POST  http://localhost:4567/accounts/
 payload
 {
    "ownerName":"user_1"
 }
 ```
 * Update account (make deposit, update user name)
 ```
 PUT  http://localhost:4567/accounts/{account_id}
 payload
  {
     "balance":100
  }
 ```
 * Get Account (view actual account status)
 ```
 GET  http://localhost:4567/accounts/{account_id}
 ```
 * Make transfer between accounts
 ```
 POST http://localhost:4567/transactions/
 payload
 {
    "fromAccountId":"{account_id}",
    "toAccountId":"{account_id}",
    "amount": {amount_value}
 }
 ```


### Precondition
* Tools below has to be available to run application:
  * Gradle 5+
  * JDK 12+

### Quick Start
```
gradle execute
```
or
### Build artifact and run
```
gradle build
java -jar ./build/libs/SimpleTransfer-0.0.1-SNAPSHOT.jar
```

## Usage  sample
* Create first account
```
$ curl -d "{"ownerName":"user_1"}" -X POST http://localhost:4567/accounts/ ; echo $line;
{"id":"2f1fb7d9-e0b7-4bfa-9241-1bb7511152d9","ownerName":"user_1","balance":0}
```
* Create second account
```
 $ curl -d "{"ownerName":"user_1"}" -X POST http://localhost:4567/accounts/ ; echo $line;
{"id":"0e201bfe-7bbd-4362-a889-80991c1acf31","ownerName":"user_1","balance":0}
```
* Deposit 100 units to first account
```
$ curl -d "{"balance":100}" -X PUT http://localhost:4567/accounts/2f1fb7d9-e0b7-4bfa-9241-1bb7511152d9 ; echo $line;
{"id":"2f1fb7d9-e0b7-4bfa-9241-1bb7511152d9","ownerName":"user_1","balance":100}
```
* Transfer
```
$ curl -d "{  \
            "fromAccountId":"2f1fb7d9-e0b7-4bfa-9241-1bb7511152d9",  \
            "toAccountId":"0e201bfe-7bbd-4362-a889-80991c1acf31",    \
            "amount": 20   \
           }"   \
      -X POST http://localhost:4567/transactions/ ; echo $line;
```
* Check first account state
```
$ curl -X GET http://localhost:4567/accounts/2f1fb7d9-e0b7-4bfa-9241-1bb7511152d9 ; echo $line;
{"id":"2f1fb7d9-e0b7-4bfa-9241-1bb7511152d9","ownerName":"user_1","balance":80}
```
* Check second account state
```
$ curl -X GET http://localhost:4567/accounts/0e201bfe-7bbd-4362-a889-80991c1acf31 ; echo $line;
{"id":"0e201bfe-7bbd-4362-a889-80991c1acf31","ownerName":"user_1","balance":20}
```



### Debug
```
gradle execute  --debug-jvm
```