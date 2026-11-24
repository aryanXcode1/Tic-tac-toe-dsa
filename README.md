#  Tic Tac Toe Game Using Java

##  Project Overview
This project is a console-based Tic Tac Toe game developed using Java. It allows a human player to play against an AI opponent. The AI uses the **Minimax Algorithm** to select the best possible moves.

The project demonstrates important Data Structures and Algorithms (DSA) concepts through an interactive game.

##  Features
- 3×3 Tic Tac Toe game board.
- Human vs AI gameplay.
- AI decision-making using the Minimax Algorithm.
- Backtracking to evaluate possible moves.
- Stack data structure to maintain move history.
- Undo previous turn functionality.
- Save game functionality.
- Load previously saved game.
- Input validation and error handling.
- Automatic winner and draw detection.

##  Technologies Used
- **Programming Language:** Java
- **Data Structures:** 2D Array, Stack
- **Algorithms:** Minimax Algorithm, Backtracking
- **File Handling:** Java I/O and NIO
- **IDE:** VS Code / IntelliJ IDEA / Eclipse

##  DSA Concepts Used
| Concept | Application |
|---|---|
| 2D Array | Stores the game board |
| Stack | Stores move history |
| Recursion | Implements Minimax |
| Backtracking | Explores possible moves |
| Searching | Checks winning combinations |

##  How to Run the Project

### 1. Clone the Repository
```bash
git clone YOUR_REPOSITORY_URL
```

### 2. Open the Project Folder
```bash
cd YOUR_REPOSITORY_NAME
```

### 3. Compile the Java File
```bash
javac TicTacToeAI.java
```

### 4. Run the Game
```bash
java TicTacToeAI
```

##  How to Play
- AI plays with **X**.
- Human player plays with **O**.
- Enter row and column numbers between 0 and 2.
- The first player to get three symbols in a row, column, or diagonal wins.

### Available Commands
| Command | Description |
|---|---|
| `undo` | Undo the previous turn |
| `save` | Save the current game |
| `load` | Load a saved game |
| `exit` | Exit the game |

##  Project Structure
```text
TicTacToe/
│
├── TicTacToeAI.java
├── game_state.txt
└── README.md
```

`game_state.txt` is created when the game is saved.

##  Future Enhancements
- Graphical User Interface (GUI).
- Multiple difficulty levels.
- Two-player mode.
- Scoreboard and player statistics.

##  Author
**Aryan Raj**
*erp id - RU-25-10297*
*B.Tech Computer Science Engineering*
*Rungta international skills university*
*Instructor name - Mr - Surya mani verma sir*

