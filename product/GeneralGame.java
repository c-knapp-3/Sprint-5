package sprint5.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneralGame extends Board {
	private List<SOSEvent> sosEventList = new ArrayList<>();   // Tracks all SOS events found during game
	private Map<SOSEvent, Character> playerSOSMap = new HashMap<>();   // Associates each SOS event w/ player that made it
	    
    public GeneralGame() {
		super();
        sosEventList.clear();
	}
	
	// Constructor accepting custom game board size
	public GeneralGame(int size) {
		super(size);
        sosEventList.clear();
    }
	
	public Cell[][] getBoard() {
        return board;
    }
	
	// Setup game board for new game
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
    
    @Override
    public void newGame() {
    	super.newGame();
    	sosEventList.clear();
    } 
    
    public void recordMove(char player, int row, int column, Cell cell) {
    	super.recordMove(player, row, column, cell);
    }
    
    public void beginRecording() {
	   super.beginRecording();
	}

	public void endRecording() {
	    super.endRecording();
	}
    
    @Override
    public boolean makeMove(int row, int column, Cell cell) {
        // No move if game is already over
        if (currentGameState != GameState.PLAYING) {
            System.out.println("Game over");
            return false;
        }    
        // Check for valid move
        if (row < 0 || row >= boardSize || column < 0 || column >= boardSize || board[row][column] != Cell.EMPTY) {
        	return false; // Invalid move
        }
        
        board[row][column] = cell;  // Place the cell
        
        if (recording) {
        	recordMove(currentPlayer, row, column, cell);
        }        

        if (!hasSOS() && currentGameState == GameState.PLAYING) {  // If no SOS is made, switch the player
        	currentPlayer = (currentPlayer == 'B') ? 'R' : 'B';     
        } 
        
        else {  
        	System.out.println(currentPlayer + " made an SOS");
        	printScores();  
            printList();    
        }      
        
        updateGameState(currentPlayer);        
        return true;
    }
    
    private void printScores() {  // Print current scores for both players
        System.out.println("Blue Score: " + blueScore);
        System.out.println("Red Score: " + redScore);
    }

    public void printList() {  // Print list of SOS events found
        sosEventList.forEach(event -> System.out.println(event));
    }
    
    public void countSOS() {
    	super.countSOS();
    }
    
    public void updateGameState(char turn) {
		
    	if (isBoardFull() && blueScore > redScore) {
    		currentGameState = GameState.BLUE_WINS;
    		System.out.println("BLUE WiNS!");
    		
    	}
    	else if (isBoardFull() && redScore > blueScore) {
    		currentGameState = GameState.RED_WINS;
    		System.out.println("RED WINS!");
    	}
    	else if (isBoardFull() && blueScore == redScore) {
            currentGameState = GameState.DRAW;
            System.out.println("TIE GAME!");
        }
    }
    
    public boolean isBoardFull() {
    	return super.isBoardFull();
    }
        
    private boolean sosEventExistsInList(SOSEvent event, List<SOSEvent> eventList) {
        for (SOSEvent existingEvent : eventList) {
            if (existingEvent.equals(event)) {
                return true; 
            }
        }
        return false; 
    }
       
    public boolean hasSOS() {   
    	
    	Cell[] symbols = { Cell.S, Cell.O, Cell.S }; 
    	boolean sosFound = false;

    	// Find horizontal SOS Events
        for (int row = 0; row < boardSize; row++) {
            for (int column = 0; column < boardSize - 2; column++) {
                if (board[row][column] == symbols[0] &&
                    board[row][column + 1] == symbols[1] &&
                    board[row][column + 2] == symbols[2]) {
                	SOSEvent newEvent = new SOSEvent(symbols[0], row, column, "row");
                	if (!sosEventExistsInList(newEvent, sosEventList)) {
                		sosEventList.add(newEvent);
                		playerSOSMap.put(newEvent, currentPlayer); // Record player with SOS
                		sosFound = true;
                		countSOS();
                	}               	
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
                	if (!sosEventExistsInList(newEvent, sosEventList)) {
                		sosEventList.add(newEvent);
                		playerSOSMap.put(newEvent, currentPlayer); // Record player with SOS
                		sosFound = true;
                		countSOS();
                	}
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
                	if (!sosEventExistsInList(newEvent, sosEventList)) {
                		sosEventList.add(newEvent);
                		playerSOSMap.put(newEvent, currentPlayer); // Record player with SOS
                		sosFound = true;
                		countSOS();
                	}
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
                	if (!sosEventExistsInList(newEvent, sosEventList)) {
                		sosEventList.add(newEvent);
                		playerSOSMap.put(newEvent, currentPlayer); // Record player with SOS
                		sosFound = true;
                		countSOS();
                	}
                }
            }
        }
        return sosFound;
    }
}
