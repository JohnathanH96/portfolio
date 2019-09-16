
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.Scanner;


//@authors Johnathan Holland

public class Publication {
	private static int count = 0;
	private static Scanner scan1;
	public static void main(String[] args) throws IOException {
		
		String fileToRead = "ProjectConformantBibFile.bib"; //file to be read
		String outputFile = "ProjectConformantBibFile (Finished).bib"; // 
		String alphaFile = "ProjectConformantBibFile (Sorted).bib";
		FileWriter writer2 = new FileWriter(alphaFile);
		FileFormatError.main(args);
		EntryManagement entryArray = new EntryManagement(); // new array holding objects of Entry
		boolean condition1 = true;
		scan1 = new Scanner(System.in);
		while(condition1)
		{
			System.out.println("----------------------------------" +
					"\nPlease enter one of the following: \n" +
					"\tName or (N) \n" +
					"\tTitle or (T) \n" +
					"\tYear or (Y) \n" +
					"\tBY TYPE NAME  (TY)\n" +
					"\tQuit or (X) \n" +   
					"----------------------------------");
			String input1 = scan1.nextLine();
			input1 = input1.toLowerCase();
			if(input1.contains("name") || input1.contains("n"))
			{
				entryArray = new EntryManagement(new NameComparator());
				condition1 = false;
			}
			else if(input1.contains("title") || input1.trim().contentEquals("t"))
			{
				entryArray = new EntryManagement(new TitleComparator());
				condition1 = false;
			}
			else if(input1.contains("year") || input1.trim().contentEquals("y"))
			{
				entryArray = new EntryManagement(new YearComparator());
				condition1 = false;
			}
			else if(input1.contains("type") || input1.trim().contentEquals("ty"))
			{
				entryArray = new EntryManagement();
				condition1 = false;
			}
			else if(input1.contains("quit") || input1.trim().contentEquals("x"))
			{
				System.exit(0);
			}
			else {
				System.out.println("Invalid input!");
				
			}
		}
		try {
			LineReader reader = new LineReader(fileToRead); // Initializes LineReader
			FileWriter writer = new FileWriter(outputFile); // Initializes FileWriter
			while(reader.hasNext())
			{
				String line = reader.next();
				line = reader.next();
				if(!line.isEmpty() && line.charAt(0) == '@')
				{
					Entry ex = new Entry();
					Pair pair = new Pair("name",line);
					ex.setNameRevised(handleLine(line));
					ex.setName(line);
					ex.add(pair);
					line = reader.next();
					while(!line.isEmpty() && line.charAt(0) != '}')
					{
						if(line.contains(" = "))
						{
							String str[] = line.split(" = ");
							Pair pair2 = new Pair(str[0].trim(),str[1].trim());
							ex.add(pair2);
							ex.setFieldCount(ex.getFieldCount() + 1);
							if(str[1].length() - 1 > ex.getLongestLineLength())
							{
								ex.setLongestLineLength(str[1].length());
								ex.setLongestLine(str[0].trim());
							}
							if(str[0].contains("author")) 
							{
								ex.setAuthors(str[1]);
								String authorLine[] = str[1].split(" and ");
								ex.setAuthorCount(authorLine.length);
							}
							if(str[0].contains("title"))
							{
								ex.setTitle(str[1]);
							}
							if(str[0].contains("year"))
							{
								String year = removeAllCurlyBraces(str[1]).replace(',', ' ').trim();
								int yearInt = Integer.parseInt(year);
								ex.setYear(yearInt);
							}
							if(str[0].contains("volume"))
							{
								ex.setVolume(str[1].trim());
							}
							if(str[0].contains("pages"))
							{
								ex.setPages(str[1].trim());
							}
							if(str[0].contains("month"))
							{
								ex.setMonth(str[1].trim());
							}
							if(str[0].contains("doi"))
							{
								ex.setDoi(str[1].trim());
							}
							if(str[0].contains("url = "))
							{
								ex.setUrl(str[1].trim());
							}
							if(str[0].contains("urlDate = "))
							{
								ex.setUrlDate(str[1].trim());
							}
							if(str[0].contains("issn"))
							{
								ex.setIssn(str[1].trim());
							}
						}
						line = reader.next();
					}
					Pair p = new Pair("count", ex.getValues());
					ex.add(p);
					writer.write(ex.getName() + "\n" + ex.toString());
					entryArray.add(ex);
					//entryArray.addOrderedBinary(ex);
				}
			}
			writer.close();
			//java.awt.Desktop.getDesktop().open(new File(outputFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		boolean condition = true;
		//Scanner scan = new Scanner(System.in);  //used for cmd
		Scanner scan = new Scanner(System.in); //used for IDE
		while(condition)
		{
			System.out.println("----------------------------------" +
					"\nPlease enter one of the following: \n" +
					"\tSearch or (S) \n" +
					"\tDelete or (D) \n" +
					"\tPrint or (W) \n" +
					"\tWrite or (W1) \n" +
					"\tMerge or (M) \n" + 
					"\tPurge or (P) \n" +
					"\tQuit or  (E) \n" +  
					"----------------------------------");
			String input = scan.nextLine();
			input = input.toLowerCase();
			if(input.contains("search") || input.contains("s"))
			{
				input = "s";
			}
			else if(input.contains("delete") || input.trim().contentEquals("d"))
			{
				input = "d";
			}
			else if(input.contains("print") || input.trim().contentEquals("w"))
			{
				input = "w";
			}
			else if(input.contains("write") || input.trim().contentEquals("w1"))
			{
				input = "w1";
			}
			else if(input.contains("merge") || input.trim().contentEquals("m"))
			{
				input = "m";
			}
			else if(input.contains("purge") || input.trim().contentEquals("p"))
			{
				input = "p";
			}
			else if(input.contains("quit") || input.contains("exit") || input.trim().contentEquals("e") || input.trim().contentEquals("q"))
			{
				input = "e";
			}
			switch (input)
			{
			//==========================================================================SEARCH===========================================================================================
			case "s": 
				System.out.println("Enter Name: ");
				String entryToSearch = scan.nextLine();
				if(entryToSearch.toLowerCase().contentEquals("back"))
				{
					break;
				}
				if(entryArray.searchByName(entryToSearch) != null)// (indexOfSearch = entryArray.bSearch2(entryToSearch, 0 , entryArray.size() - 1)) >= 0
				{
					Entry entryFound = entryArray.searchByName(entryToSearch);
					System.out.print("Entry "+ entryFound.getNameRevised() +" was found!\n");
				}

				else {
					System.out.println(entryToSearch + " not found");
				}
				break;
				//==========================================================================DELETE===========================================================================================
			case "d": 
				System.out.println("(\"back\" to return to main menu)\nEntry Name:");
				String entryToDelete = scan.nextLine();
				if(entryToDelete.toLowerCase().contentEquals("back"))
				{
					break;
				}
				if(entryArray.searchByName(entryToDelete) != null) //(entryArray.bSearch2(entryToDelete, 0, entryArray.size() - 1)) >= 0
				{
					System.out.println("Are you sure you want to delete \""+entryToDelete+"\" (y/n)?");
					if(scan.nextLine().toLowerCase().contains("y"))
					{
						System.out.println(entryArray.deleteEntry(entryToDelete));
						System.out.println("The entry " + entryToDelete + " has been deleted!");
					}
					else {
						System.out.println("The entry \""+ entryToDelete + "\" was not deleted!");
					}
				}
				else
				{
					System.out.println("The entry \""+ entryToDelete + "\" does not exist");
				}
				break;
				//===========================================================================WRITE===========================================================================================
			case "w1":
				System.out.println("Enter a file name: ");
				String fileName = scan.nextLine();
				if(fileName.contentEquals(""))
				{
					entryArray.tree.printTree();
					break;
				}
				else if(fileName.toLowerCase().contentEquals("back"))
				{
					break;
				}
				//String nameOfFile;
				if(fileName.length() > 30)
				{
					System.out.println("File name is too long. Please enter a file name with fewer than thirty characters.");
					String nextLine = scan.nextLine();
					if(nextLine.length() > 30)
					{
						entryArray.tree.printTree();
						System.out.println("\nInformation printed to the console since a file name could not be determined.\n");
						break;
					}
					else if(nextLine.length() < 30 ) {
						File file = new File(nextLine);
						if(file.isFile())
						{
							System.out.println("File already exists. Would you like to overwrite?");
							String yesOrNo = scan.nextLine();
							if(yesOrNo.toLowerCase().contains("y"))
							{
								FileWriter writer = new FileWriter(nextLine);
								entryArray.writeNodesToFile(writer);
								System.out.println("Wrote to file named: \"" + nextLine +"\"");
								break;
							}

						}
					}
					File file = new File(fileName);
					if(file.isFile())
					{
						System.out.println("File already exists. Would you like to overwrite?");
						String yesOrNo = scan.nextLine();
						if(yesOrNo.toLowerCase().contains("y"))
						{
							FileWriter writer = new FileWriter(fileName);
							entryArray.writeNodesToFile(writer);
							System.out.println("Wrote to file named: \"" +fileName +"\"");
							break;
						}
						else {
							break;
						}
					}

				}
				else {
					File file = new File(fileName);
					if(file.isFile())
					{
						System.out.println("File already exists. Would you like to overwrite?");
						String yesOrNo = scan.nextLine();
						if(yesOrNo.toLowerCase().contains("y"))
						{
							FileWriter writer = new FileWriter(fileName);
							entryArray.writeNodesToFile(writer);
							System.out.println("Wrote to file named: \"" +fileName +"\"");

							break;
						}
						else {
							break;
						}
					}
					else {
						FileWriter writer = new FileWriter(fileName);
						entryArray.writeNodesToFile(writer);
						System.out.println("Wrote to file named: \"" +fileName +"\"");
						break;
					}
				}

				break;
				//===========================================================================EXIT===========================================================================================
			case "e":
				Scanner scanner = new Scanner(System.in);
				System.out.println("\nAre you sure you want to exit (Y/N)?");
				String quitInput = scanner.nextLine();
				if(quitInput.toLowerCase().contains("y"))
				{
					System.out.println("Goodbye!");
					condition1 = false;
					System.exit(0);
				}
				break;
				//===========================================================================PRINT===========================================================================================
			case "w":
				System.out.println("(\"back\" to return to main menu)\nEntry Name:");
				String entryToPrint = scan.nextLine();
				if(entryToPrint.toLowerCase().contentEquals("back"))
				{
					break;
				}
                                Entry thisEntry = entryArray.searchByName(entryToPrint);
				if(thisEntry != null)
				{
					System.out.println("-------------------------------------------------\n\n\n"+
							"\t\t\t"+ entryArray.searchByName(entryToPrint).getNameRevised() + 
							"\n" +
							entryArray.searchByName(entryToPrint).printEntry().replace(",", "") + 
							"-------------------------------------------------");
				}
				else {
					System.out.println(entryToPrint + " not found.");
				}
				break;
				//===========================================================================PURGE===========================================================================================
			case "p":
				LineReader reader = null;
				Scanner scanner1 = new Scanner(System.in);
				int purgeTries = 0;
				while(purgeTries < 6)
				{
					System.out.println("Please enter a file name: ");
					String userInput = scanner1.nextLine();
					File inputFile = new File(userInput);
					if(inputFile.exists())
					{
						System.out.println("File \""+ inputFile.getName()+ "\" found.\nWould you like to open the file to view the merged content?");
						reader = new LineReader(inputFile);
						String openFile = scanner1.nextLine();
						boolean open = false;
						if(openFile.contains("y"))
						{
							open = true;
						}
						System.out.println("\nPurging data...");
						purgeEntries(reader, entryArray);
						//writeOrderedToFile(entryArray,fileToRead, open);
						break;
					}
					else
					{
						purgeTries++;
						System.out.println("File not found!");
					}
				}
				while(reader.hasNext())
				{
					System.out.println(reader.next());
				}
				break;
				//===========================================================================MERGE===========================================================================================
			case "m":
				LineReader reader2 = null;
				Scanner scanner2 = new Scanner(System.in);

					System.out.println("Enter file name, type back to exit: ");
					String userInput = scanner2.nextLine();
					if(userInput.toLowerCase().contentEquals("back"))
					{
						break;
					}
					File inputFile = new File(userInput);
					if(inputFile.exists())
					{
						System.out.println(inputFile.getName()+ " found.\nView merged file? (y/n)");
						String openFile = scanner2.nextLine();
						boolean open = false;
						if(openFile.contains("y"))
						{
							open = true;
						}
						System.out.println("\nMerging...\n");
						reader2 = new LineReader(inputFile);
						mergeEntries(reader2, entryArray);
						//writeOrderedToFile(entryArray,fileToRead, open);
						break;
					}
					else
					{
						System.out.println("File not found!");
					}
				break;

			default:
				System.out.println("Please give a valid input...");
			}

		}
		scan.close();
	}

	static String handleLine(String line) {
		String lineRevised = "";
		String str[] = line.split("\\{");
		lineRevised = str[1].replace(",","").trim();
		return lineRevised;
	}

	public static String getNextLine(LineReader reader, String line)
	{
		if(reader.next() == null)
		{
			return line;
		}
		line = reader.next();
		return line;
	}

	public static int occurence(String line, char c)
	{
		int count = 0;
		for (int i = 0; i < line.length(); i++) {
			if(line.charAt(i) == c)
			{
				count++;
			}
		}
		return count;
	}

	public static String checkLineBalance(LineReader reader,String line)
	{
		if(reader == null)
		{
			return line;
		}
		if(line.startsWith("}"))
		{
			return line;
		}
		while(!line.endsWith("},"))
		{
			line = line + getNextLine(reader, line);
		}
		return line;
	}

	public static String removeAllCurlyBraces(String line) {

		line = line.replace('{', ' ').replace('}', ' ');
		if(line.contains("{") || line.contains("}"))
		{
			return removeAllCurlyBraces(line);
		}
		else {
			return line;
		}
	}

	public static void writeOrderedToFile(EntryManagement entryArray, String fileToWrite, boolean open) throws IOException 
	{
		try {
			File file = new File(fileToWrite);
			FileWriter writer = new FileWriter(fileToWrite);
            entryArray.writeNodesToFile(writer);
			if(open)
			{
				java.awt.Desktop.getDesktop().open(file);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static class TitleComparator implements Comparator<Entry>
	{

		@Override
		public int compare(Entry o1, Entry o2) {
			
				// TODO Auto-generated method stub
				if(o1.getTitle().compareTo(o2.getTitle())<0)
				{
					return -1;
	            }
				else if (o1.getTitle().compareTo(o2.getTitle())>0)
				{
					return 1;
				}
				else
				{
					return o1.getNameRevised().compareTo(o2.getNameRevised());
				}
		}

	}

	static class NameComparator implements Comparator<Entry>
	{

		@Override
		public int compare(Entry o1, Entry o2) {
			// TODO Auto-generated method stub
			return o1.getNameRevised().compareTo(o2.getNameRevised());
		}

	}

	static class YearComparator implements Comparator<Entry>
	{

		@Override
		public int compare(Entry o1, Entry o2) {
			// TODO Auto-generated method stub
			if(o1.getYear() < o2.getYear())
			{
				return -1;
            }
			else if (o1.getYear() > o2.getYear())
			{
				return 1;
			}
			else
			{
				return o1.getNameRevised().compareTo(o2.getNameRevised());
			}
		}

	}

        static String handleLineWithAt(String line) {
            String lineRevised = "";
            String str[] = line.split("\\{");
            lineRevised = str[0].replace("@","").trim();
            return lineRevised;
        }
        
    	public static void mergeEntries(LineReader reader, EntryManagement entryArray) throws IOException
    	{
    		while(reader.hasNext()) 
    		{
    			String line = reader.next();
    			line = reader.next();
    			if(!line.isEmpty() && line.charAt(0) == '@')
    			{
    				Entry ex = new Entry();
    				Pair pair = new Pair("name",line);
    				ex.setNameRevised(handleLine(line));
    				Entry searchTemp = entryArray.searchByName(ex.getNameRevised());
    				if(searchTemp != null)
    				{
    					entryArray.deleteEntry(searchTemp);
    				}
    				ex.setName(line);
    				ex.add(pair);
    				line = reader.next();
    				while(!line.isEmpty() && line.charAt(0) != '}')
    				{
    					if(line.contains(" = "))
    					{
    						String str[] = line.split(" = ");
    						Pair pair2 = new Pair(str[0].trim(),str[1].trim());
    						ex.add(pair2);
    						ex.setFieldCount(ex.getFieldCount() + 1);
    						if(str[1].length() - 1 > ex.getLongestLineLength())
    						{
    							ex.setLongestLineLength(str[1].length());
    							ex.setLongestLine(str[0].trim());
    						}
    						if(str[0].contains("author")) 
    						{
    							ex.setAuthors(str[1]);
    							String authorLine[] = str[1].split(" and ");
    							ex.setAuthorCount(authorLine.length);
    						}
    						if(str[0].contains("title"))
    						{
    							ex.setTitle(str[1]);
    						}
    						if(str[0].contains("year"))
    						{
    							String year = removeAllCurlyBraces(str[1]).replace(',', ' ').trim();
    							int yearInt = Integer.parseInt(year);
    							ex.setYear(yearInt);
    						}
    						if(str[0].contains("volume"))
    						{
    							ex.setVolume(str[1].trim());
    						}
    						if(str[0].contains("pages"))
    						{
    							ex.setPages(str[1].trim());
    						}
    						if(str[0].contains("month"))
    						{
    							ex.setMonth(str[1].trim());
    						}
    						if(str[0].contains("doi"))
    						{
    							ex.setDoi(str[1].trim());
    						}
    						if(str[0].contains("url = "))
    						{
    							ex.setUrl(str[1].trim());
    						}
    						if(str[0].contains("urlDate = "))
    						{
    							ex.setUrlDate(str[1].trim());
    						}
    						if(str[0].contains("issn"))
    						{
    							ex.setIssn(str[1].trim());
    						}
    					}
    					line = reader.next();
    				}
    				Pair p = new Pair("count", ex.getValues());
    				ex.add(p);
    				entryArray.add(ex);
    			}
    		}
    	}

    	
     	public static void purgeEntries(LineReader reader, EntryManagement entryArray) throws IOException
    	{
    		String line = reader.next();
    		while(reader.hasNext()) {
    			if(!line.isEmpty() && line.charAt(0) == '@')
    			{
    				Entry temp = entryArray.searchByName(handleLine(line));
    				entryArray.deleteEntry(temp);
    				
    			}
    			line = reader.next();
    		}
    	}

}