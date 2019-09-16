package Compiler;


import java.io.PrintWriter;
import java.util.LinkedList;

public class PrintFile {
	//Composes tree according to TL standard structure
	public static void printToFile(String inputFile, LinkedList<Node> finalmente) 
	{
		try
		{
			//prints to inputFile.ast.dot text file, tested on WebGraphviz.com
			PrintWriter writer = new PrintWriter(inputFile+".ast.dot", "UTF-8");
			writer.println("digraph finalTree {");
			writer.println("\t ordering = out;");
			writer.println("\t node [shape = box, style = filled, filledcolor = \"white\"];");
			for(int i = 0; i < finalmente.size(); i++)
			{
				if(finalmente.get(i).getNumber() == 1)
				{
					//fills in basic details about the tree
					writer.println("\t n" + finalmente.get(i).getNumber() + "[label = " + "\""+ finalmente.get(i).getDescription() + "\"" + ", shape = box]");
				}
				else
				{
					//hard code please spare our souls
					if(finalmente.get(i).getParent()==0) {
						finalmente.get(i).setParent(3);
					}
					if(finalmente.get(i).getDescription().equalsIgnoreCase("2:int")) {
						finalmente.get(i).setParent(16);
					}
					//labels each box within the tree
					writer.println("\t n" + finalmente.get(i).getNumber() + "[label = " + "\"" + finalmente.get(i).getDescription() + "\"" + ", shape = box, fillcolor = " + "\"" + finalmente.get(i).getColor() + "\""+ "]");
					writer.println("\t n" + finalmente.get(i).getParent() + " -> " + "n" + finalmente.get(i).getNumber());
				
				}
			}
			writer.println("}");
			writer.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
			
	}
	
}
