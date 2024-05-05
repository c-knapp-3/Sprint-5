package sprint5.product;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameRecorder {
	private String filePath;
	private List<String> moves = new ArrayList<>();
		
	public GameRecorder(String file) {
		this.filePath = file;
	}
	
	public void recordMove(String move) {
		// Function to write to file and append subsequent data to end of file
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
			writer.write(move);
			writer.newLine();	
			System.out.println("Move has been recorded.");
			writer.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error while recording move.");
		}
	}
	
	public List<String> readRecordedMoves() throws IOException {
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))){
			String line;
			while((line = reader.readLine()) != null) {
				moves.add(line);
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error while reading recorded moves.");
			throw e;
		} 
		return moves;		
	}
	
	public void clearRecordedFile() {
		// Function to clear file
		File file = new File(filePath);
		if (file.exists()) {		
			if (file.delete()) {
				System.out.println("File has been deleted.");
			} 
			else {
				System.err.println("Cannot delete the file.");
	        }
		} 
		else {
			System.out.println("Cannot find file.");
		}
	}
}
