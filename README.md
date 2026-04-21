# Sudoku Console Game

## Application Description

Sudoku Console Game is a Java-based command-line application that allows users to play a classic **9×9 Sudoku puzzle** directly from the terminal.

The application automatically generates a valid Sudoku board, removes numbers to create a playable puzzle, and allows the player to solve the puzzle interactively using simple text commands.

Players can:

* Enter numbers into cells
* Clear incorrect values
* Request hints
* Validate the grid
* Quit the game

The project is built using **Java** and **Maven**, and can be executed using:

* compiled **JAR file**

---

# Technologies Used

* Java (JDK 21)
* Maven

---

# Project Structure

```
sudoku/
|
|-- src/
|   |-- main/java/org/sudoku/
|   |   |-- constant
|   |   |   |-- SudokuConstants.java
|   |   |-- exception
|   |   |   |-- SudokuException.java
|   |   |-- game
|   |   |   |-- SudokuGame.java
|   |   |-- model
|   |   |   |-- Command.java
|   |   |-- util
|   |   |   |-- CommandParser.java
|   |   |   |-- SudokoGenerator.java
|   |   |   |-- SudokuValidator.java
|   |   |-- SudokuApp.java
|   |
|   |-- test/java/org/sudoku/
|   |   |-- game
|   |   |   |-- SudokuGameTest.java
|   |   |-- util
|   |   |   |-- CommandParserTest.java
|   |   |   |-- SudokoGeneratorTest.java
|   |   |   |-- SudokuValidatorTest.java
|       |-- SudokuAppIntegrationTest.java
|-- target/
|
|-- pom.xml
|-- README.md
```

---

# Prerequisites

Install:

```
Java 21 or later
Maven
```
---

# Build the Project (Maven)

Run:

```
mvn clean package
```

This will:

* compile source code
* execute tests
* generate runnable jar file

Output:

```
target/Sudoku-1.0-SNAPSHOT.jar
```

---

# Run Using JAR File

After building:

```
java -jar target/Sudoku-1.0-SNAPSHOT.jar
```

---

# Game Menu (Commands)

The puzzle is displayed when the application starts.

Example:

```
        Welcome to Sudoku!

Here is your puzzle:
    1 2 3 4 5 6 7 8 9
  A 5 3 _ _ 7 _ _ _ _
  B 6 _ _ 1 9 5 _ _ _
  C _ 9 8 _ _ _ _ 6 _
```

Supported commands:

| Command  | Description                        |
|----------|------------------------------------|
| B3 7     | Place number 7 into row B column 3 |
| C5 clear | Clear value in row C column 5      |
| hint     | Reveal one correct number          |
| check    | Validate grid correctness          |
| quit     | Exit the game                      |

---

# Example Gameplay
# Insert Number
```
Enter command:
C1 5
```

If invalid:
Cells generated at puzzle start cannot be modified.
```
Invalid move. B1 is pre-filled.
```

If valid:

```
Move accepted.
```

---

# Clear

```
Enter command:
B1 clear
```

If invalid:
Cells generated at puzzle start cannot be modified.
```
Invalid move. B1 is pre-filled.
```

If valid:

```
B1 cleared.
```

---

# Quit Game

Command:

```
quit
```

Example:

```
Enter command:
quit
```
```
Thanks for playing Sudoku!
```

---

# Hint Feature

Command:

```
hint
```

Behavior:

* the program should reveal one correct number
* fills empty editable cell

Example:

```
Hint: Cell C8 = 1
```
---

# Validation Command

Command:

```
check
```

Behavior:
* Check the current grid for validity (no duplicates in rows, columns, or subgrids).

Example:

```
No rule violations detected.
```

or

```
Number 5 already exists in Column 1.
```
---

# Grid Validation Rules

The application checks:

* duplicate values in rows
* duplicate values in columns
* duplicate values inside 3×3 subgrids

Invalid moves are rejected immediately.

---

# Puzzle Completion Behavior

When puzzle is solved:

```
You have successfully completed the Sudoku puzzle!
Press any key to play again...
```

Game restarts automatically after pressing ENTER.

---




