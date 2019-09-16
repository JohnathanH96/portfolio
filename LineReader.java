
/**
* @author Johnathan Holland
*/
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LineReader implements Iterator<String>{
	//Variables
	private String fileName;
	private String nextLine;
	private BufferedReader reader;
	private File fileToRead;
	public String newFileName;

	//Constructors
	public LineReader(String fileName) throws FileNotFoundException {
		this.fileName = fileName;
		reader = new BufferedReader(new FileReader(this.fileName));
		updateLine();
	}

	public LineReader(File fileToRead) {
		this.setFileToRead(fileToRead);
		try {
			reader = new BufferedReader(new FileReader(fileToRead));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		updateLine();
	}

	//Methods
	@Override
	public boolean hasNext() {
		return reader != null;
	}

	@Override
	public String next() {
		if(reader == null)
		{
			throw new NoSuchElementException("End of File");
		}
		String toReturn = nextLine;
		updateLine();
		return toReturn;
	}

	private void updateLine() {
		if(reader == null)
		{
			return;
		}
		try 
		{
			nextLine = reader.readLine();
			if(nextLine == null)
			{
				throw new IOException();
			}
		} catch (IOException ex) {
			try 
			{
				reader.close();
			} catch (IOException ex1) {
				Logger.getLogger(LineReader.class.getName()).log(Level.SEVERE, null, ex1);
			}
			reader = null;
		}
	}

	public File getFileToRead() {
		return fileToRead;
	}

	public void setFileToRead(File fileToRead) {
		this.fileToRead = fileToRead;
	}  
}
