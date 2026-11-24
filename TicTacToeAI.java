import java.util.*;
import java.io.*;
import java.nio.file.*;

public class TicTacToeAI {

    private static final int SIZE = 3;
    private static final char AI = 'X';
    private static final char HUMAN = 'O';
    private static final String SAVE_FILE = "game_state.txt";

    static char[][] board = new char[SIZE][SIZE];
    static Stack<Move> moveHistory = new Stack<>();
    static char currentTurn = AI;

    // Store details of each move
    static class Move {
        int row, col;
        char symbol;

        Move(int row, int col, char symbol) {
            this.row = row;
            this.col = col;
            this.symbol = symbol;
        }
    }

    // Initialize the game
    static void initializeGame() {
        for (int r = 0; r < SIZE; r++) {
            Arrays.fill(board[r], ' ');
        }

        moveHistory.clear();
        currentTurn = AI;
    }

    // Display the board
    static void displayBoard() {
        System.out.println("\n     0   1   2");

        for (int r = 0; r < SIZE; r++) {
            System.out.print(" " + r + "   ");

            for (int c = 0; c < SIZE; c++) {
                System.out.print(board[r][c]);

                if (c < SIZE - 1) {
                    System.out.print(" | ");
                }
            }

            System.out.println();

            if (r < SIZE - 1) {
                System.out.println("    ---+---+---");
            }
        }

        System.out.println();
    }

    // Check winner
    static char checkWinner() {

        // Check rows and columns
        for (int i = 0; i < SIZE; i++) {

            if (board[i][0] != ' ' &&
                board[i][0] == board[i][1] &&
                board[i][1] == board[i][2]) {
                return board[i][0];
            }

            if (board[0][i] != ' ' &&
                board[0][i] == board[1][i] &&
                board[1][i] == board[2][i]) {
                return board[0][i];
            }
        }

        // Check diagonals
        if (board[0][0] != ' ' &&
            board[0][0] == board[1][1] &&
            board[1][1] == board[2][2]) {
            return board[0][0];
        }

        if (board[0][2] != ' ' &&
            board[0][2] == board[1][1] &&
            board[1][1] == board[2][0]) {
            return board[0][2];
        }

        return ' ';
    }

    // Check whether board is full
    static boolean isBoardFull() {

        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {

                if (board[r][c] == ' ') {
                    return false;
                }
            }
        }

        return true;
    }

    // Validate the move
    static boolean isValidMove(int row, int col) {

        return row >= 0 && row < SIZE &&
               col >= 0 && col < SIZE &&
               board[row][col] == ' ';
    }

    // Store move using Stack
    static void makeMove(int row, int col, char symbol) {

        board[row][col] = symbol;

        moveHistory.push(new Move(row, col, symbol));
    }

    // Undo the previous complete turn
    static void undoTurn() {

        if (moveHistory.empty()) {
            System.out.println("No moves available to undo.");
            return;
        }

        Move last = moveHistory.pop();
        board[last.row][last.col] = ' ';

        // Remove previous human move too
        if (last.symbol == AI &&
            !moveHistory.empty() &&
            moveHistory.peek().symbol == HUMAN) {

            Move humanMove = moveHistory.pop();

            board[humanMove.row][humanMove.col] = ' ';
        }

        currentTurn = AI;

        System.out.println("Previous turn undone.");
    }

    // Cell checking order
    // Centre first, then corners and edges
    static List<int[]> orderedCells() {

        return Arrays.asList(
            new int[]{1, 1},
            new int[]{0, 0},
            new int[]{0, 2},
            new int[]{2, 0},
            new int[]{2, 2},
            new int[]{0, 1},
            new int[]{1, 0},
            new int[]{1, 2},
            new int[]{2, 1}
        );
    }

    // Minimax algorithm with backtracking
    static int minimax(int depth, boolean maximizing) {

        char winner = checkWinner();

        if (winner == AI) {
            return 10 - depth;
        }

        if (winner == HUMAN) {
            return depth - 10;
        }

        if (isBoardFull()) {
            return 0;
        }

        // AI tries to maximize score
        if (maximizing) {

            int bestScore = Integer.MIN_VALUE;

            for (int[] cell : orderedCells()) {

                int r = cell[0];
                int c = cell[1];

                if (board[r][c] == ' ') {

                    board[r][c] = AI;

                    int score = minimax(depth + 1, false);

                    // Backtracking
                    board[r][c] = ' ';

                    bestScore = Math.max(bestScore, score);
                }
            }

            return bestScore;
        }

        // Human tries to minimize AI score
        else {

            int bestScore = Integer.MAX_VALUE;

            for (int[] cell : orderedCells()) {

                int r = cell[0];
                int c = cell[1];

                if (board[r][c] == ' ') {

                    board[r][c] = HUMAN;

                    int score = minimax(depth + 1, true);

                    // Backtracking
                    board[r][c] = ' ';

                    bestScore = Math.min(bestScore, score);
                }
            }

            return bestScore;
        }
    }

    // AI selects its best move
    static void aiMove() {

        int bestScore = Integer.MIN_VALUE;

        int bestRow = -1;
        int bestCol = -1;

        for (int[] cell : orderedCells()) {

            int r = cell[0];
            int c = cell[1];

            if (board[r][c] == ' ') {

                board[r][c] = AI;

                int score = minimax(0, false);

                // Backtracking
                board[r][c] = ' ';

                if (score > bestScore) {

                    bestScore = score;

                    bestRow = r;
                    bestCol = c;
                }
            }
        }

        if (bestRow != -1) {

            makeMove(bestRow, bestCol, AI);

            System.out.println(
                "AI placed X at row " +
                bestRow + ", column " + bestCol
            );

            System.out.println("Move accepted. Board updated.");
        }
    }

    // Save game in text file
    static void saveGame() {

        try (PrintWriter out = new PrintWriter(
                Files.newBufferedWriter(Paths.get(SAVE_FILE)))) {

            out.println(currentTurn);

            for (int r = 0; r < SIZE; r++) {

                for (int c = 0; c < SIZE; c++) {

                    out.print(
                        board[r][c] == ' ' ? '-' : board[r][c]
                    );
                }

                out.println();
            }

            out.println(moveHistory.size());

            for (Move move : moveHistory) {

                out.println(
                    move.row + " " +
                    move.col + " " +
                    move.symbol
                );
            }

            System.out.println("Game saved successfully.");

        } catch (IOException e) {

            System.out.println("Error saving game: " + e.getMessage());
        }
    }

    // Load previously saved game
    static void loadGame() {

        try {

            List<String> lines =
                Files.readAllLines(Paths.get(SAVE_FILE));

            if (lines.size() < 5) {
                throw new IllegalArgumentException();
            }

            String turnLine = lines.get(0).trim();

            if (turnLine.length() != 1) {
                throw new IllegalArgumentException();
            }

            char savedTurn = turnLine.charAt(0);

            if (savedTurn != AI && savedTurn != HUMAN) {
                throw new IllegalArgumentException();
            }

            char[][] loadedBoard = new char[SIZE][SIZE];

            for (int r = 0; r < SIZE; r++) {

                String line = lines.get(r + 1).trim();

                if (line.length() != SIZE) {
                    throw new IllegalArgumentException();
                }

                for (int c = 0; c < SIZE; c++) {

                    char value = line.charAt(c);

                    if (value != 'X' &&
                        value != 'O' &&
                        value != '-') {

                        throw new IllegalArgumentException();
                    }

                    loadedBoard[r][c] =
                        value == '-' ? ' ' : value;
                }
            }

            int historySize =
                Integer.parseInt(lines.get(4).trim());

            if (historySize < 0 ||
                lines.size() != 5 + historySize) {

                throw new IllegalArgumentException();
            }

            Stack<Move> loadedHistory = new Stack<>();

            boolean[][] used = new boolean[SIZE][SIZE];

            char expected = AI;

            for (int i = 0; i < historySize; i++) {

                String[] parts =
                    lines.get(5 + i).trim().split("\\s+");

                if (parts.length != 3) {
                    throw new IllegalArgumentException();
                }

                int r = Integer.parseInt(parts[0]);
                int c = Integer.parseInt(parts[1]);

                if (r < 0 || r >= SIZE ||
                    c < 0 || c >= SIZE ||
                    used[r][c] ||
                    parts[2].length() != 1 ||
                    parts[2].charAt(0) != expected) {

                    throw new IllegalArgumentException();
                }

                char symbol = expected;

                if (loadedBoard[r][c] != symbol) {
                    throw new IllegalArgumentException();
                }

                used[r][c] = true;

                loadedHistory.push(new Move(r, c, symbol));

                expected = (expected == AI) ? HUMAN : AI;
            }

            int xCount = 0;
            int oCount = 0;

            for (int r = 0; r < SIZE; r++) {
                for (int c = 0; c < SIZE; c++) {

                    if (loadedBoard[r][c] == 'X') xCount++;
                    if (loadedBoard[r][c] == 'O') oCount++;

                    if ((loadedBoard[r][c] != ' ') != used[r][c]) {
                        throw new IllegalArgumentException();
                    }
                }
            }

            if (xCount != oCount &&
                xCount != oCount + 1) {

                throw new IllegalArgumentException();
            }

            char expectedTurn =
                (xCount == oCount) ? AI : HUMAN;

            if (savedTurn != expectedTurn) {
                throw new IllegalArgumentException();
            }

            char[][] oldBoard = board;

            board = loadedBoard;

            char winner = checkWinner();

            if ((winner == AI &&
                 (xCount != oCount + 1 || savedTurn != HUMAN)) ||
                (winner == HUMAN &&
                 (xCount != oCount || savedTurn != AI))) {

                board = oldBoard;
                throw new IllegalArgumentException();
            }

            currentTurn = savedTurn;
            moveHistory = loadedHistory;

            System.out.println("Game loaded successfully.");

        } catch (IOException e) {

            System.out.println("No saved game found.");

        } catch (RuntimeException e) {

            System.out.println("Invalid or corrupted save file.");
        }
    }

    // Instructions
    static void printInstructions() {

        System.out.println("======================================");
        System.out.println("       TIC TAC TOE WITH AI");
        System.out.println("======================================");

        System.out.println("AI Player    : X");
        System.out.println("Human Player : O");

        System.out.println("\nEnter row and column from 0 to 2.");

        System.out.println("\nCommands:");
        System.out.println("undo  - Undo previous turn");
        System.out.println("save  - Save game");
        System.out.println("load  - Load game");
        System.out.println("exit  - Exit game");

        System.out.println("======================================");
    }

    // Main method
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        initializeGame();

        printInstructions();

        while (true) {

            // Check winner
            char winner = checkWinner();

            if (winner != ' ') {

                displayBoard();

                if (winner == AI) {
                    System.out.println("AI (X) wins!");
                } else {
                    System.out.println("Human (O) wins!");
                }

                break;
            }

            // Check draw
            if (isBoardFull()) {

                displayBoard();

                System.out.println("Game draw.");

                break;
            }

            // AI turn
            if (currentTurn == AI) {

                aiMove();

                currentTurn = HUMAN;

                displayBoard();

                continue;
            }

            // Human turn
            System.out.print("Your move (row column): ");

            if (!sc.hasNextLine()) {
                break;
            }

            String input = sc.nextLine().trim();

            if (input.equalsIgnoreCase("exit")) {

                System.out.println("Game ended.");

                break;
            }

            if (input.equalsIgnoreCase("undo")) {

                undoTurn();

                continue;
            }

            if (input.equalsIgnoreCase("save")) {

                saveGame();

                continue;
            }

            if (input.equalsIgnoreCase("load")) {

                loadGame();

                continue;
            }

            String[] values = input.split("\\s+");

            if (values.length != 2) {

                System.out.println(
                    "Invalid input. Enter row and column."
                );

                continue;
            }

            try {

                int row = Integer.parseInt(values[0]);
                int col = Integer.parseInt(values[1]);

                if (!isValidMove(row, col)) {

                    System.out.println(
                        "Cell occupied or invalid coordinates. " +
                        "Choose another cell."
                    );

                    continue;
                }

                makeMove(row, col, HUMAN);

                System.out.println(
                    "Move accepted. Board updated."
                );

                currentTurn = AI;

            } catch (NumberFormatException e) {

                System.out.println(
                    "Invalid input. Enter integer coordinates."
                );
            }
        }

        sc.close();
    }
}