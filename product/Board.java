package sprint5.product;

import java.util.List;
import java.util.Map;

public abstract class Board {   
	
	protected Cell[][] board;    
    protected int boardSize;
    protected int blueScore;
    protected int redScore;
    protected char currentPlayer; 
    private boolean simpleGame;
    
    public static enum Cell {EMPTY, S, O}    
    public enum GameState {PLAYING, DRAW, BLUE_WINS, RED_WINS}
    
    protected GameState currentGameState;    
    protected GameRecorder recordGame;
    protected boolean recording = false;   
     
    public Board() {
    	 this.board = new Cell[3][3];
    	 this.boardSize = 3;
    	 this.simpleGame = true;
         recordGame = new GameRecorder("sos_game_replay.txt"); 
         initializeBoard();
    }
    
    public Board(int size) {
    	this.board = new Cell[size][size];
    	this.boardSize = size;
    	this.simpleGame = true;  
        recordGame = new GameRecorder("sos_game_replay.txt"); 
        initializeBoard();
    }
        
    public Board(int size, boolean mode) {
    	this.board = new Cell[size][size];
    	this.boardSize = size;
        this.simpleGame = mode;
        this.currentPlayer = 'B';        
        this.currentGameState = GameState.PLAYING;
        initializeBoard();
    }
        
    public void initializeBoard() {
   	 	for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = Cell.EMPTY;
            }
        }
   	 	currentGameState = GameState.PLAYING; 
   	 	currentPlayer = 'B';
   	 	blueScore = 0;
   	 	redScore = 0;
    }    
        
    public Cell[][] getBoard() {
        return board;
    }
    
    public void setBoardSize(int size) {
    	this.board = new Cell[size][size];
    	this.boardSize = size;
        initializeBoard();
    }
    
    public int getBoardSize() {
    	return boardSize;
    }        
             
    public void setGameMode(boolean mode) {
    	this.simpleGame = mode;
    }
    
    public boolean getGameMode() {
    	return simpleGame;
    }
    
    public void setCurrentPlayer(char player) {
        this.currentPlayer = player;
    }
    
    public char getCurrentPlayer() {
		return currentPlayer;
	}
    
    public GameState getCurrentGameState() {
		return currentGameState;
	}
    
    public Cell getCell(int row, int column) {
        if (row >= 0 && row < boardSize && column >= 0 && column < boardSize)
            return board[row][column];
        else
            return Cell.EMPTY;
    }
    
    public Cell getSymbol(int row, int column) {    	    	
        return board[row][column];
    }
      
    public boolean makeMove(int row, int column, Cell cell) {
        if (row < 0 || row >= boardSize || column < 0 || column >= boardSize || board[row][column] != Cell.EMPTY) {
            return false; // Invalid move
        }
        board[row][column] = cell;
        currentPlayer = (currentPlayer == 'B') ? 'R' : 'B';   // Toggle current player from blue to red or red to blue
        return true;
    }
   
    public void countSOS() {
    	if (currentPlayer == 'B') {
    		blueScore++;
    	}
    	else {
    		redScore++;
    	}
    }
    
    public void newGame() {
    	recordGame.clearRecordedFile();
    	recordGame = new GameRecorder("sos_game_replay.txt"); 
    	initializeBoard();
    }
            
    protected boolean isBoardFull() {
    	for (int row = 0; row < boardSize; row++) {
            for (int column = 0; column < boardSize; column++) {
                if (board[row][column] == Cell.EMPTY) {
                    return false;	
                }
            }
    	}
    	return true; 
    }
    
    protected void recordMove(char player, int row, int column, Cell cell) {
    	Cell symbol = cell;
    	String letter = symbol.toString();
    	String move = String.format("%c %d %d %s",player, row, column, letter);
    	
    	recordGame.recordMove(move);
    }
    
    protected void beginRecording() {
	    recording = true;
	}

	protected void endRecording() {
	    recording = false;
	}
      
    protected abstract void updateGameState(char turn);
    protected abstract int getBlueScore();
    protected abstract int getRedScore();
    protected abstract boolean hasSOS();    
    protected abstract List<SOSEvent> getSOSEventList();
    protected abstract Map<SOSEvent, Character> getPlayerSOSMap();
}
