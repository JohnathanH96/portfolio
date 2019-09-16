package Compiler;

//tree node containing info for nodeNumber, nodeParent, nodeDescription, and boxColor
public class Node {

	public String description;
    public String color;
    public int nodeNumber;
    public int parentNode;
    

    public Node(int number, int parent, String description, String color)
    {
        this.nodeNumber = number;
        this.parentNode = parent;
        this.description = description;
        this.color = color;
    }
    
    //getter for node description
    public String getDescription()
    {
        return description;
    }
    
    //getter for node Color
    public String getColor()
    {
        return color;
    }
    
    //getter for node number
    public int getNumber()
    {
        return nodeNumber;
    }
    
    //getter for node parent
    public int getParent()
    {
        return parentNode;
    }
    
    //setter for node description
    public void setDescription(String description)
    {
        this.description = description;
    }
    
    //setter for node color
    public void setColor(String color)
    {
        this.color = color;
    }
    
    //setter for node number
    public void setNumber(int number)
    {
        this.nodeNumber = number;
    }
    
    //setter for node parent
    public void setParent(int parent)
    {
        this.parentNode = parent;
    }
       
}


