# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)
[![Phase2Diagram](phase3.png)](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2AMQALADMABwATG4gMP7I9gAWYDoIPoYASij2SKoWckgQaJiIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0PgZQADpoAN4ARP2UaMAAtihjtWMwYwA0y7jqAO7QHAtLq8soM8BICHvLAL6YwjUwFazsXJT145NQ03PnB2MbqttQu0WyzWYyOJzOQLGVzYnG4sHuN1E9SgmWyYEoAAoMlkcpQMgBHVI5ACU12qojulVk8iUKnU9XsKDAAFUBhi3h8UKTqYplGpVJSjDpagAxJCcGCsyg8mA6SwwDmzMQ6FHAADWkoGME2SDA8QVA05MGACFVHHlKAAHmiNDzafy7gjySp6mgfAgEGSRCpHZUbs8YAAhYAcAlEsAAUUtKmwBAKXtupXuxXM9UCTicw3Gc3UwEZC2WEag3jqBqmSuNppQIfl8jV6ChCYpSb91QDCirNfa6vQUZjccK-vhyeQqZg6eCWbGOdUefm9TGRZL9UVc0rZtrPbQjcRPpbVWoAexaMoIvBKA4fZQsfyg7bw8qKYw9QArBmpzO5wXF8XoCvDRWJobjAdYNlc6AcJgdp8uovoHlQSKatK2hNnuVLaPa9IgKq6JSlA7IDDy3IYTBAr3MYtQKBwHBIVAPKoYY9zQXSqi1Nh1bogoPh6hiwDcfERFQSRLGChRVE0Vxer0bujH3DCTylseuJQBkqjulg8lwnBQ4vBMAGfDAC7AssfF6u0ED1tuhnLJcCaUHBT5gPU4TvqMenlgZRnfKZ8TmZZSxeeBnCmF4vgBNA7CMjEIpwBG0hwAoMAADIQFkhSOYKOmNK0HTdD0BjqLeWarigay-P8HBXEOFSaQGIwlWV+h-Ds0KPHCFQyfUCCpeKGIpWlYZqGApIyRUzH8vUPnmIQcgoHhfU9Wgg0kkJNKkaJwowGKEp4TKcplu8gESuVOyYCq1ZqqtvIsS2nW7Sho0dfetTBqGKCEkNdlQC2jm1OmmZuZ++YLkuf60UaaAQMwABmvicDuzooLdz3MtM-HQEgABeF5fT9o7PumACMH78l+IO-qWPho3qGPY7sVWI8jh51P14qg1AV43vG1UVL9b4A9mpPA4WFMLhiHBqNhxADjAEDQzA7PEo2EFXZhqhMwhLpuh6DFjcJE1TRgM24WybyCeN6gVBRMDiTb-HSYzNVtQGrNLWo6mYLV31Pczukld+xljD5fkNoFuO8-jTkwC5AvuYdnxeSCwcWaHNnK8FnjeH4-heCg6AxHEiS5-n-W+FgGVOr7jTSBGSURu0EZ5QVqhFdTvkp3ezNO7CAbJ5ZnvO995SdTA3X2GXvH8SHaAjYz5QW6xMAojMEA0JJ7eWZPZkd8Ra03eU1vbRJ9vaLK8p9+gZ2qpdC8a0i68O96SM+zUlEn-IAByUMinDl7RteA5w7lD5q5QWuZhY-mXAdY0-FpogDyAUIwp9IYw1-unSCt8X5Im1p6Ua899b0iXkcVeKB17Ty3hvdAu9rr8itptDIK8aB2ykg9R25Qvb1FLtxVSHsvYazqgzLuEcShRxjlmIKkFM5hX8CiGi-hsDig1ElNEMAADiSoNAV1bFXBoqj655XsEqYYF9O41G7gpSaU8O4Dx7kPEeyAcjqJzBQ6es8n56z3hNGAjIwAdgQE4tQLid6q1InQ+oqimSjxUXLSsCA1FKnouddUISbpYK1u6XBbCsqox8rTC8nNAE82AZHP6ThiajGWEDecIsoFU1yVALGF4liCM1s-Yez1kpogCaoM8px8n-y5qYygeMRG1H5lmSpQtqmQLBuLSWDTBmy3lpQEsSsYASJSbQtJKBajKJyAAHgCTycoutzFwnCUyPx3SeEIA0oPfhpYRjLEMTmAsDRxgvJQAASWkAWQm4RgiBBBJseIuo5r6XnGMb4yRQBqjwpyPY3xPkfyVIsKEMBOgtOGcIscYiKljE+aoN5HylQ-L+QCoFywQVgvhUqRFIIYUgDhRC+lzylQormGii4GKNlSOzhwAA7G4JwKAnAxAjMEOAsUABs8AcKGACTAIokdModOaG0LovRPnGKsZZLMyKlRYqHhwmAIwTFrANXMVqtjtn1HYrNAJQTLIWvZUqUkSTLqPXYYPeoXy0BUBNEgDgASOUoAKbeGxCkHmvlAdOKZ352YMgrEaeBaAUGykMEcWM8poAwBsCgIgJoYAgHiHgUA6IoBEo2Xghedr5WOvNd411cxqFqzCXKjiCqEkoQ9Zsy2tqcGnPaVXV6y1IwDMKfeEZqZ-oTLjeA6ZibZQhiIR9HIzSh3ZLbnkv+-YI1FN+kTOdVSE2iwVNuhpdMN1eqyuZCAABZdglhunhu5lOnFz5xn4pPeTKBd6YCPrQPKbpNtOzmmNOAJANAyrcH0IYTYJxmDABgCvOYGBr1ZI6XAetSpencF3QA-d77imjK-U8+ds4IFLrmaoKWiyYkrOgGsjZmDh2tNqEc1h7izkBmw52gJNy7k2pfnVNlcwyX1H+YC9ZQDfrR1AWJ75vzJMUpkyrPlARLAoA9BATYBckAJDAFpnTemABSEBxTxLXP4Rll0tHwQDM0ZkmqejarbtPLM2AEDAC01AOAEBupQBdeJ6QRqeOPMbV5nzlB-OBeC0p61Ua2OIQAFYWbQA23V6AYPRb8wF6A8WfluNaR4mhhC0viky9vZ1MAou+diwVptIXW3rXIptI+TXDD7R+WsPwWh0RWZQDKCAOgUsFuYJZ4wV8Lp9vVramAg6b0dNHe9cMr6hnexIzOhTFGyY1LBnoGiKI11gAw9xtjAYcno0vf0vdb6hFbYJmU498bf1gzqddxpuwZNLarh2Dc3ZLLrdkyUicL2F2nqgUBas4HQJWRaaIaNMBzOVdw+eAjgyQekZ2z+-bpYaN0ZlgximzGzDBVYyPTj8gh0mpRxlpUgnI3tRE489Y3n6v5agHsAA6iwL5DceiBiSgoOAABpJFpLlMwCk4EH7xG5N4tNby0K2cvA+f04ZtX8pEDVlgMAbAXnja3iVfZrKDQa51wbnlKb1VvW2PqBLbg6JpA6AxKSPhyWXRGG00yIbrviuI-wZ4whIBYN0X97N9tjvfexJArobgGhe0U8Rq6DJm7lshjHcDg9oOcevbx-UQ7q7wxnZKyz+oPI9AGC-mAHXeBbuEfu2Yj9TkyOTIh29-HEtaMLKJ8sknG6VbJ6fhXnQdfZH3qmTTn1Mgx9h4vIzvh5fTXt8o5CyvcG80+FItDDJ8pQ+66aXLh7CvXIr928DMYG+DBb533v4t8-vsbKAA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
