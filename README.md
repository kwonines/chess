# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Phase 2 Sequence Diagram
[https://sequencediagram.org/index.html#initialData=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](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=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)

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
