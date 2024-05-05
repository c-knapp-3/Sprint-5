package sprint5.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleGame extends Board {

    private List<SOSEvent> sosEventList = new ArrayList<>();   // List to store found SOS's
	private Map<SOSEvent, Character> playerSOSMap = new HashMap<>(); // Maps found SOS's to player that found them
    
	public SimpleGame() {
    	super();
        sosEventList.clear();
    }

    public SimpleGame(int size) {
    	super(size);
        sosEventList.clear();
    }
    
    public Cell[][] getBoard() {
        return board;
    }
    
    // Initializes or resets the board to start a new game
    public void initializeBoard() {    	
        super.initializeBoard();
    }  
    
    public int getBoardSize() {
    	return boardSize;
    }     
    
    @Override
    public void setBoardSize(int size) {
    	super.setBoardSize(size);
    }
    
    public char getCurrentPlayer() {
        return currentPlayer;
    }    
   
    public void setCurrentPlayer(char player) {
        this.currentPlayer = player;
    }   
    
    public GameState getCurrentGameState() {
		return currentGameState;
	}

    @Override
    public Cell getCell(int row, int column) {
        return super.getCell(row, column);
    }
    
    public int getBlueScore() {
    	return blueScore;
    }
    
    public int getRedScore() {
    	return redScore;
    }
    
    public List<SOSEvent> getSOSEventList() {
        return sosEventList;
    }
    
    public Map<SOSEvent, Character> getPlayerSOSMap() {
        return playerSOSMap;
    }

    // Resets board to start a new game
    @Override
    public void newGame() {
    	super.newGame();
    	sosEventList.clear();
    }
    
    public void recordMove(char player, int row, int col, Cell cell) {
    	super.recordMove(player, row, col, cell);
    }
    public void beginRecording() {
	   super.beginRecording();
	}

	public void endRecording() {
	    super.endRecording();
	}
    
    // Handles a player's move on the boards
    @Override
    public boolean makeMove(int row, int col, Cell cell) {
        if (currentGameState != GameState.PLAYING) {
            System.out.println("Game over");
            return false;
        }

        // Check if the move is within bounds and the cell is empty
        if (row < 0 || row >= boardSize || col < 0 || col >= boardSize || board[row][col] != Cell.EMPTY) {
            return false; // Invalid move
        }

        // Place the move on the board
        board[row][col] = cell;

        if (recording) {
            recordMove(currentPlayer, row, col, cell);
        }

        updateGameState(currentPlayer);
        if (currentGameState == GameState.PLAYING) {
        	currentPlayer = (currentPlayer == 'B') ? 'R' : 'B'; // Switches the turn between players
            System.out.println("Switch players");
        }

        return true; // Move was successful
    }
   
    public void countSOS() {
    	super.countSOS();
    }

    public void updateGameState(char turn) {
    	if (hasSOS()) {        // If SOS Event detected, update game status to indicate winner
            currentGameState = (turn == 'B') ? GameState.BLUE_WINS : GameState.RED_WINS;
            System.out.println(currentGameState + "!");
        } 
    	else if (isBoardFull()) {  // If board is full and no SOS Event detected, game is a draw
            currentGameState = GameState.DRAW;
            System.out.println("Tie game");
        }
    }
    
    public boolean isBoardFull() {
    	return super.isBoardFull();
    }
    
    public boolean hasSOS() {   
    	
    	Cell[] symbols = {Cell.S, Cell.O, Cell.S}; 

        // Find horizontal SOS Events
        for (int row = 0; row < boardSize; row++) {
            for (int column = 0; column < boardSize - 2; column++) {
                if (board[row][column] == symbols[0] &&
                    board[row][column + 1] == symbols[1] &&
                    board[row][column + 2] == symbols[2]) {
                	SOSEvent newEvent = new SOSEvent(symbols[0], row, column, "row");
                	sosEventList.add(newEvent);
            		playerSOSMap.put(newEvent, currentPlayer);
                	countSOS();
                    return true;
                }
            }
        }

        // Find vertical SOS Events
        for (int column = 0; column < boardSize; column++) {
            for (int row = 0; row < boardSize - 2; row++) {
                if (board[row][column] == symbols[0] &&
                    board[row + 1][column] == symbols[1] &&
                    board[row + 2][column] == symbols[2]) {
                	SOSEvent newEvent = new SOSEvent(symbols[0], row, column, "column");
                	sosEventList.add(newEvent);
            		playerSOSMap.put(newEvent, currentPlayer);
                	countSOS();
                    return true;
                }
            }
        }

        // Find top-left to bottom-right diagonal SOS Events
        for (int row = 0; row < boardSize - 2; row++) {
            for (int column = 0; column < boardSize - 2; column++) {
                if (board[row][column] == symbols[0] &&
                    board[row + 1][column + 1] == symbols[1] &&
                    board[row + 2][column + 2] == symbols[2]) {
                	SOSEvent newEvent = new SOSEvent(symbols[0], row, column, "diagTlBr");
                	sosEventList.add(newEvent);
            		playerSOSMap.put(newEvent, currentPlayer);
                	countSOS();
                    return true;
                }
            }
        }

        // Find top-right to bottom-left diagonal SOS Events
        for (int row = 0; row < boardSize - 2; row++) {
            for (int column = boardSize - 1; column >= 2; column--) {
                if (board[row][column] == symbols[0] &&
                    board[row + 1][column - 1] == symbols[1] &&
                    board[row + 2][column - 2] == symbols[2]) {
                	SOSEvent newEvent = new SOSEvent(symbols[0], row, column, "diagTrBl");
                	sosEventList.add(newEvent);
            		playerSOSMap.put(newEvent, currentPlayer);
                	countSOS();
                    return true;
                }
            }
        }
        return false;
    }
}
