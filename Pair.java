
/*
 * @author Johnathan Holland
 */
public class Pair extends Entry{
	
	protected String type; // Parameter that will store the first half of the sentence that contains the '='
	protected String value; //Parameter that will store the second half of the sentence that contains the '='
	//Constructor
	public Pair(String type, String value) 
	{
		this.type = type;
		this.value = value;
	}
	//Methods
	public String toString()
	{
		String str = type + " = " +  value;
		
		return str;
	}
	public String getType() {
		return type;
	}
	
	public String getValue() {
		return value;
	}
	
}
