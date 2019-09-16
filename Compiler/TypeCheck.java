package Compiler;


import java.util.LinkedList;

public class TypeCheck {
	public static LinkedList<Node> checkType(LinkedList<Node> node)
    {
    	
        for(int i = 0; i < node.size(); i++)
        {
            if(node.get(i).getDescription().equalsIgnoreCase("mod:int") || node.get(i).getDescription().equalsIgnoreCase(":=") || node.get(i).getDescription().equalsIgnoreCase("*:int") || node.get(i).getDescription().equalsIgnoreCase("div:int") || node.get(i).getDescription().equalsIgnoreCase("-:int") || node.get(i).getDescription().equalsIgnoreCase("+:int"))
            {
            	//description will potentially be "int" or "bool"
                String t1 = node.get(i - 1).getDescription();
                String t2 = node.get(i + 1).getDescription();
                //adjust t1,t2 to proper size
                if(t1.toLowerCase().contains("int")){
                	t1 = t1.substring(t1.length()-3);
                }else if(t1.toLowerCase().contains("bool")){
                	t1 = t1.substring(t1.length()-4);
                }
                if(t2.toLowerCase().contains("int")){
                	t2 = t2.substring(t2.length()-3);
                }else if(t2.toLowerCase().contains("bool")){
                	t2 = t2.substring(t2.length()-4);
                }

                if(!t1.equals(t2))
                {
                	//color "Red" indicates type error
                	 node.get(i).setColor("Red");
                	 
                     System.out.println("Type error : "+t1+" cannot be "+t2);
                }
            }
        }
        return node;
    }
	
}
