//website:  http://www.webgraphviz.com/

package Compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;

public class Compiler {

	public static void main(String[] args) throws FileNotFoundException {
		Scanner scan= new Scanner(System.in);
		String inputFile="";
		String readLine="";
		//String token="";


		int numberOfNodes=4;
		LinkedList<String> tokens= new LinkedList<String>();
		LinkedList<Node> nodeList= new LinkedList<Node>();
		LinkedList<Node> initial= new LinkedList<Node>();
		LinkedList<Node> finalmente= new LinkedList<Node>();
		//keeps track of root node
		Stack<Integer> rootStack= new Stack<Integer>();
		LinkedList<String> name= new LinkedList<String>();	//useful for TypeCheck.java
		LinkedList<String> type= new LinkedList<String>();
		
		//Node contains nodeNumber, nodeParent, nodeDescription, nodeColor, where description is what populates each box
		Node rootNode= new Node(1,0,"Program","White");
		Node declarationList= new Node(2,1,"decl list","White");
		Node stmtList= new Node(3,1,"stmt list","White");
		Methods.pass(nodeList,rootNode,declarationList,stmtList);
		
		//begin with node 2 as root
		rootStack.push(2);
		System.out.println("Enter name of a file: ");
		inputFile=scan.next();
		File inFile=new File(inputFile);
		Scanner in= new Scanner(inFile);
		while(in.hasNext()) {
			//read file by line
			readLine=in.nextLine();
			initial.clear();
			Node tokeNode= new Node(numberOfNodes,0,"","");
			String token="";
			for (int i = 0; i < readLine.length(); i++) {
				//then read file by token in line
				if((readLine.charAt(i)==' ') || (readLine.charAt(i)=='\t')) {	
					if(!token.equalsIgnoreCase(" ") && !token.isEmpty()) {
						tokens.add(token);
						if(!(token.equalsIgnoreCase("then")) && !(token.equalsIgnoreCase("do")) && !(token.equalsIgnoreCase(";"))){
							tokeNode= new Node(numberOfNodes,0,"","White");
							numberOfNodes++;
							tokeNode.setDescription(Methods.setType(name,type,token));
							initial.add(tokeNode);
							token="";
						}
					}
				}else {
					token=token+readLine.charAt(i);
				}
			}
			tokens.add(token);
			if(!token.equalsIgnoreCase(";") && !token.equalsIgnoreCase("do") && !token.equals(null)){
				tokeNode= new Node(numberOfNodes,0,"","");
				numberOfNodes++;
				tokeNode.setDescription(token);;
				initial.add(tokeNode);
			}
			//beginning of right side of tree
			if(token.equalsIgnoreCase("begin")) {
				rootStack.push(3);
			}

			if(tokens.size()>1) {
				if(tokens.get(0).equalsIgnoreCase("var")) {
					Node decNode= new Node(numberOfNodes, rootStack.peek()," decl:'"+tokens.get(1)+"':"+tokens.get(3),"");
					numberOfNodes++;
					nodeList.add(decNode);
					name.add(tokens.get(1));
					type.add(tokens.get(3));
				}else if(tokens.get(1).equalsIgnoreCase(":=")) {
					Methods.assign(initial,rootStack.peek());
					for (int i = 0; i < initial.size(); i++) {
						nodeList.add(initial.get(i));
					}
				}else if(tokens.get(0).equalsIgnoreCase("while")) {
					Node statement= new Node(numberOfNodes,initial.get(0).getNumber(),"stmt list","White");
					numberOfNodes++;
					initial.add(statement);
					//see Methods.java
					Methods.compose(initial,rootStack.peek());

					for(int i=0; i<initial.size(); i++) {
						nodeList.add(initial.get(i));
					}
					rootStack.push(statement.getNumber());
				}else if(tokens.get(0).equalsIgnoreCase("if")) {
					Node statement= new Node(numberOfNodes,initial.get(0).getNumber(),"stmt list","White");
					numberOfNodes++;
					initial.add(statement);
					//see Methods.java
					Methods.compose(initial,rootStack.peek());

					for(int i=0; i<initial.size(); i++) {
						nodeList.add(initial.get(i));
					}
					rootStack.push(statement.getNumber());
				}else if(tokens.get(0).equalsIgnoreCase("writeint")) {
					if(initial.size()==2) {
						//see Methods.java
						Methods.setEm(initial,rootStack);
					}else {
						//see Methods.java
						Methods.makeExpression(initial,rootStack.peek());
					}

					for (int i = 0; i < initial.size(); i++) {
						nodeList.add(initial.get(i));
					}
				}else if(tokens.get(0).equalsIgnoreCase("begin")) {
					rootStack.push(3);
				}else if(tokens.get(0).equalsIgnoreCase("end")) {
					rootStack.pop();
				}else if(tokens.get(0).equalsIgnoreCase("else")) {
					Node statement= new Node(numberOfNodes,initial.get(0).getNumber(),"Else","White");
					numberOfNodes++;
					initial.add(statement);
					rootStack.pop();
					rootStack.push(statement.getNumber());
				}else if(!tokens.get(0).equalsIgnoreCase("program")) {
					System.err.println("Scan and Parse Error, unrecognized: "+tokens.get(0));
					//System.out.println("No Error, 100% Correct Graph");
					for (int i = 0; i < tokens.size(); i++) {
						System.out.println(tokens.get(i)+"\n");
					}
				}
			}
			tokens.clear();
		}
		//perform type checking
		finalmente= TypeCheck.checkType(nodeList);
		//print tree to file with same name as input
		inputFile=inputFile.replaceAll(".tl", "");
		PrintFile.printToFile(inputFile,finalmente);
		System.out.println("Printed to file: "+inputFile+".ast\"");
		scan.close();
	}
}
