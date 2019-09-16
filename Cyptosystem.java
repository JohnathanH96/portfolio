package Cryptosystem;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.TextStyle;
import java.util.HashSet;
import java.util.Scanner;

public class Cyptosystem 
{
	String outputFile = "EncryptionTest.txt";
	String outputFile2 ="Decrypted.txt";
	String outputFile3 ="BruteForce_Decryption.txt";
	static HashSet<String> set = new HashSet<>();


	String XOR(byte[] Message, byte[] Key) {//function used for XOR
		byte[] s = new byte[Message.length];
		for (int i = 0; i < Message.length; i++) {
			s[i] = (byte) (Message[i] ^ Key[i]);// XOR Process
		}
		String S = new String(s);
		return S;
	}

	void EncryptKey(String Message, String Key) throws IOException {//Encryption Method taking message and key as input
		FileWriter writer = new FileWriter(outputFile); // Initializes FileWriter
		Message = XOR(Message.getBytes(), Key.getBytes());//calling XOR method
		System.out.println("Your encrypted message is: ");
		System.out.println(Message);
		writer.write(Message);
		writer.close();
	}
	void DecyrptKey(String Message2, String Key) throws IOException //Decryption Method taking message and key as input
	{
		FileWriter writer2 = new FileWriter(outputFile2);
		Message2 = XOR(Message2.getBytes(), Key.getBytes());//calling XOR method
		System.out.println("Your Decrypted message is: ");
		System.out.println(Message2);
		writer2.write(Message2);
		writer2.close();
	}

	public void BruteForce(String Message3, String Key) throws IOException //Decryption Method taking message and key as input
	{
		Message3 = XOR(Message3.getBytes(), Key.getBytes());//calling XOR method
		if(CheckMessage(Message3) == true)
		{
			FileWriter writer3 = new FileWriter(outputFile3);
			System.out.println(Message3);
			writer3.write(Message3);
			writer3.close();
		}

	}
	
	public static void dictionary(HashSet<String> set) throws FileNotFoundException {
		String fileName = "Dictionary.txt";


		Scanner reader = new Scanner(new File(fileName));
		


		//dictionary set up



		while(reader.hasNext())
		{	
			set.add(reader.next());		//adds dictionary to hashset
		}
		reader.close();
	}

	public boolean CheckMessage(String Message3) throws FileNotFoundException {		//Used to Check the files with the Dictionary.
		




		String [] split = Message3.split(" ");					
		int j=0;	//j tallies #of correct words
		// _______________________ Compare__________________
		for (int i = 0; i < split.length; i++) 
		{

			if(set.contains(split[i]))
			{
				j++;					
			}
			//main logic for brute force accuracy check
		}
		if(j >= 10)
		{
			System.out.println("Your Decrypted message is: ");
			return true;
		}
		return false;						
	}


	public static void main(String[] args) throws IOException
	{

		String fileName = "";
		String mainFile = "Decrypted.txt";
		int fKey=65;
		int lKey=65;
		int n;
		String result ="";
		String result2="";
		String Key,Message3 = "",Message2,Message,p;
		boolean condition1 = true;
		
		dictionary(set);
		
		Scanner scan1 = new Scanner(System.in);
		while(condition1)
		{
			System.out.println("\n----------------------------------" +
					"\n ---- Please Select a number ----"+
					"\n\t1. Encrypt a file \n" +
					"\t2. Decrypt a file (With Key) \n" +
					"\t3. Decrypt a file (Without Key)\n" +
					"\t4. Quit \n" +   
					"----------------------------------");
			String input1 = scan1.nextLine();
			input1 = input1.toLowerCase();

			//----------------------------------------------Encrytpion----------------------------------------------------------------------------------------
			if(input1.contains("1"))	
			{
				System.out.println("Please insert a 2 character key: "); //Key to Encrypt message with 
				{
					Scanner sc = new Scanner(System.in);
					Key = sc.nextLine();

					System.out.println("Enter file name:"); //File Checking
					Scanner in = new Scanner(System.in);
					fileName = in.nextLine();
					File file = new File(fileName);
					if (!file.exists()) {
						System.out.println(fileName + " does not exist.");
					}
					if (!(file.isFile() && file.canRead())) {
						System.out.println(file.getName() + " cannot be read from.");
					}
					try {
						FileInputStream fis = new FileInputStream(file);
						char current;



						while (fis.available() > 0)
						{
							current = (char) fis.read();
							result+=current;
						}
						Message = result;
						System.out.println("\nYour file reads: \n" + Message + "\n");


						n = Message.length();
						if(n%2 == 1){
							Message = Message + " ";//adding space in case of odd length message
							n++;
						}
						p=Key;
						for(int i= 1 ; i < n/2 ; i++ ){
							Key = Key+p;//n/2 times key concatenation
						}
						Cyptosystem obj = new Cyptosystem();
						obj.EncryptKey(Message,Key);//calling encryption method


					} catch (IOException e) {
						System.out.print("Please insert a valid file."); // This allows the user the chance to retry with a new file and not end. 
					}
				}

			}




			//----------------------------------------------Decryption----------------------------------------------------------------------------------------

			else if(input1.contains("2"))
			{
				System.out.println("Please insert a 2 character key: ");  //In Decryption method the user MUST have the key for this to work. 
				{
					Scanner sc2 = new Scanner(System.in);
					Key = sc2.nextLine();

					System.out.println("Enter file name:"); //File reading and checking if it exists/is readable
					Scanner in2 = new Scanner(System.in);
					fileName = in2.nextLine();
					File file = new File(fileName);
					if (!file.exists()) {
						System.out.println(fileName + " does not exist.");
					}
					if (!(file.isFile() && file.canRead())) {
						System.out.println(file.getName() + " cannot be read from.");
					}
					try {
						FileInputStream fis = new FileInputStream(file);
						char current2;



						while (fis.available() > 0)
						{
							current2 = (char) fis.read();
							result2+=current2;
						}
						Message2 = result2;
						System.out.println("\nYour file reads: \n" + Message2 + "\n");


						n = Message2.length();
						if(n%2 == 1){
							Message2 = Message2 + " ";//adding space in case of odd length message
							n++;
						}
						p=Key;
						for(int i= 1 ; i < n/2 ; i++ ){
							Key = Key+p;//n/2 times key concatenation
						}
						Cyptosystem obj = new Cyptosystem();
						obj.DecyrptKey(Message2, Key);//calling encryption method


					} catch (IOException e) {
						System.out.print("Please insert a valid file.");
					}
				}
			}







			//----------------------------------------------Brute Force----------------------------------------------------------------------------------------




			else if(input1.contains("3"))
			{



				System.out.println("Enter file name:"); //File choosing and checking the file if it exists, and if it can be read from.
				Scanner in = new Scanner(System.in);
				mainFile = in.nextLine();
				File file = new File(mainFile);
				if (!file.exists()) 
				{
					System.out.println(mainFile + " does not exist.");
				}
				if (!(file.isFile() && file.canRead())) 
				{
					System.out.println(file.getName() + " cannot be read from.");
				}
				try {
					FileInputStream fis = new FileInputStream(file);
					char current;



					while (fis.available() > 0)
					{
						current = (char) fis.read();
						Message3+=current;
					}




					n = Message3.length();
					System.out.println("\nYour file reads: \n" + Message3 + "\n");
					long startTime = System.nanoTime();






					for (int x = 0; x < 95; x++) //Begin loop for first character of the key
					{
						if(x == 61)
						{
							fKey = 32;
							lKey = 32;
						}
						String s1= Character.toString((char) fKey);
						for (int y = 0; y < 95; y++)   // Begin loop for last character of the key
						{

							String d= Character.toString((char) lKey);
							Key = s1+d;
							if(n%2 == 1)
							{
								Message3 = Message3 + " ";//adding space in case of odd length message
								n++;
							}
							p=Key;
							for(int i1= 1 ; i1 < n/2 ; i1++ )
							{
								Key = Key+p;//n/2 times key concatenation
							}

							Cyptosystem obj = new Cyptosystem();
							obj.BruteForce(Message3, Key);//calling encryption method
							lKey++;

						}
						lKey = 32;
						fKey++;
					}


				}
				catch (IOException e) 
				{
					System.out.print("Please insert a valid file.");
				}

			}





			//--------------------------------------Quit-----------------------------------
			else if(input1.contains("4"))
			{
				System.exit(0);
			}
			else {
				System.out.println("Invalid input!");

			}
		}
	}

}

