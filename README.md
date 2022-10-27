# How to start
`./mvnw spring-boot:run`
or build the application
`./mvnw clean package`
and run as a jar
`java -jar target/nim-0.0.1-SNAPSHOT.jar`

# How to use
## Rules
The game is played between two players. Here the second player is a computer. In the standard game each turn a player has to to take between 1 and 3 matches. 
The last player to take a match loses the game. 

## Start a game
```curl -X POST http://localhost:8080/game```

will create a standard game. There are options the customize the game:

| Value | Type | Description|
| --- | --- | --- |
| initialMatches | Int | Sets the value of the inital matches |
| minMatchesPerTurn | Int |  Sets the minium number of matches a player or the computer has to take each turn |
| maxMatchesPerTurn | Int | Sets the maximal number of matches a player or the computer has to take each turn
| isHard | Boolean | If set to true the computer will try to win the game, otherwise computer makes random turns

## Play a game
Each turn a player has to take a number of matches. To execute a turn run

``` curl -X PUT http://localhost:8080/game/{gameId}?matchesTaken=n ```

if the game did not finish after the players turn, the response will include the computers turn. 
Each game is stored in a file Memory Database (H2) and can be viewed and continued any time. 
To find recent matches

``` curl -X GET http://localhost:8080/game ```

this will give basic Informations about all games. 
To view more details of the game

``` curl -X GET http://localhost:8080/game/{gameId} ```
