package Compiler;


import java.util.LinkedList;
import java.util.Stack;

public class Methods {
	
	//feeds makeExpression and sets Node Parents
	public static void compose(LinkedList<Node> initial, Integer top) {
		// TODO Auto-generated method stub
		int two= initial.size()-2;
		int three = initial.size()-3;
		int four= initial.size()-4;
		int five= initial.size()-5;
		int six= initial.size()-6;
		initial.get(0).setParent(top);
		initial.get(1).setParent(initial.get(0).getNumber());
		initial.get(three).setParent(initial.get(0).getNumber());
		initial.get(two).setParent(initial.get(three).getNumber());
		
		LinkedList<Node> list1= new LinkedList<>();
		for (int i = 1; i < three; i++) {
			list1.add(initial.get(i));
		}
		if(list1.size()==3) {
			initial.get(four).setParent(initial.get(five).getNumber());
			initial.get(five).setParent(initial.get(three).getNumber());
			initial.get(six).setParent(initial.get(five).getNumber());
		}else {
			makeExpression(list1,initial.get(five).getNumber());
		}
	}
	
	//returns :bool/:int to be added to string if type is bool/int
	public static String booleanOrInt(String type)
	{
		if(type.equalsIgnoreCase(" ") || type.equals(null)) {
			return "";
		}else if(type.equalsIgnoreCase("true") || type.equalsIgnoreCase("false")){
			return ":bool";
		}else{
			return ":int";
		}
	}

	
	//Sets parent node for root being 1-3
	public static void makeExpression(LinkedList<Node> initial, Integer peek) {
		// TODO Auto-generated method stub
		for (int i = initial.size()-2; i>=2; i-=2) {
			initial.get(i).setParent(initial.get(i-2).getNumber());
			initial.get(i+1).setParent(initial.get(i).getNumber());
			if(i==3) {
				initial.get(i-1).setParent(initial.get(i).getNumber());
			}
		}
	}

	//Sets parent node according to what's on top of the stack
	public static void setEm(LinkedList<Node> initial, Stack<Integer> rootStack) {
		// TODO Auto-generated method stub
		initial.get(0).setParent(rootStack.peek());
		initial.get(1).setParent(initial.get(0).getNumber());
	}

	//feeds makeExpression and assigns nodes to their parents
	public static void assign(LinkedList<Node> initial, Integer peek) {
        initial.get(1).setParent(peek);
        initial.get(0).setParent(initial.get(1).getNumber());

        LinkedList<Node> Eq = new LinkedList<>();

        for (int i = 0; i < initial.size(); i++) {
            Eq.add(initial.get(i));
        }

        if(initial.size() == 3)
        {
            initial.get(0).setParent(initial.get(1).getNumber());
            initial.get(1).setParent(peek);
            initial.get(2).setParent(initial.get(1).getNumber());
        }
        else
        {
            makeExpression(initial, peek);
        }

    }

	//sets type to work with typeCheck or tacks booleanOrInt's return onto token
	public static String setType(LinkedList<String> name, LinkedList<String> type, String token) {
		// TODO Auto-generated method stub
		String newType;
		if(token.equalsIgnoreCase("readint") || token.equalsIgnoreCase("writeint")) {
			newType= token+":int";
		}else if(token.equalsIgnoreCase(":=") || token.equalsIgnoreCase("var") || token.equalsIgnoreCase("while") || token.equalsIgnoreCase("if") || token.equalsIgnoreCase("<") || token.equalsIgnoreCase(">") || token.equalsIgnoreCase("<=") || token.equalsIgnoreCase(">=") || token.equalsIgnoreCase("!=")) {
			newType= token;
		}else {
			newType= token+booleanOrInt(token);
		}
		for (int i = 0; i < name.size(); i++) {
			if(token.equalsIgnoreCase(name.get(i))) {
				newType= name.get(i)+":"+type.get(i);
				return newType;
			}
		}
		return newType;
	}
	

	//populates initial list
	public static void pass(LinkedList<Node> initial, Node rootNode, Node n1, Node n2) {
		// TODO Auto-generated method stub
		initial.add(rootNode);
		initial.add(n1);
		initial.add(n2);
	}

}
