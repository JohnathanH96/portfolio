
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileFormatError {
	

	public static void main(String[] args) throws IOException {
		File fileToWrite = new File("ProjectConformantBibFile (Updated).bib"); //Creating a new file to write the "updated" lines to
		File fileToRead = new File("ProjectConformantBibFile.bib"); // Initializing the file that we need to read
		FileWriter writer = new FileWriter(fileToWrite);	// Creating a FileWriter
		LineReader reader = new LineReader(fileToRead);  // Creating a FileReader
		while(reader.hasNext()) // true as long a next line exists within the file
		{
			String line = reader.next(); 
			if(line.contains("{\\"))   // "{\\" marks the beginning of replaceable phrases
			{
				String change = line.replace("{\\textendash", " – ").replace("}", ""); // 
				change = change.replace("{\\textemdash", " – ");						//
				change = change.replace("{\\\"", "");									//
				change = change.replace("{\\'", "'");									//
				change = change.replace("{\\textquoteright", "’");						//
				change = change.replace("{\\textquoteleft", "‘");						//
				change = change.replace("{\\textquotedblright", "”");					//
				change = change.replace("{\\textquotedblleft", "“");					//
				change = change.replace("{\\$n\\$-", "");								//
				change = change.replace("{\\textbackslash", "\\");						// Checking the line to see if it contains any sort of these strings. If so replace them.
				change = change.replace("{\\textasciicircum", "^");						// 
				change = change.replace("{\\textgreater", ">");						//
				change = change.replace("$^{\\textrm{{TM\\$$", "™");					//
				change = change.replace("{\\^", "");									//
				change = change.replace("{\\r", "");									//
				change = change.replace("{\\`e", "é");									//
				change = change.replace("{\\`E", "É");									//
				change = change.replace("{\\", "");									//								
				
				char str[] = change.toCharArray(); 
				if(str[str.length - 1] == ',') // checking if the last character is a comma
				{
					str[str.length - 1 ] = '}'; // if true, replace comma with a curly brace
				}
				String string = "";
				for(int i = 0; i < str.length; i++) 
				{
					string = string + str[i]; // reforms the string
				}
				writer.write(change + "\n"); // updating file with correct lines			
			}
			else 
			{
				writer.write(line + "\n"); // accessed if replaceable sequence is not found
			}
		}	
		writer.close(); 
		//java.awt.Desktop.getDesktop().open(fileToWrite); //opens the file upon running of Publication.java
	}
}