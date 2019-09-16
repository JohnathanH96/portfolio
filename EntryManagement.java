
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;

public class EntryManagement {
 
	private Entry[] allEntries = new Entry[100];
	private int count = 0;
	//private LinkedListNotJava<Entry> list;
        public AVLTree<Entry> tree;

	public EntryManagement() {
		
		//list = new LinkedListNotJava<Entry>();
                tree = new AVLTree<>();
	}
	
	public EntryManagement(Comparator<Entry> comp)
	{
		//list = new LinkedListNotJava<Entry>(comp);
               tree = new AVLTree<Entry>(comp);
		
	}
	public void add(Entry entry) {
		//list.addOrdered(entry);
		//list.addOrderedCustom(entry);
                tree.insert(entry);
                
	}
        
	public String deleteEntry(String entry)
	{
		return tree.remove(new Entry(entry));
			
	}
	
	public String deleteEntry(Entry entry)
	{
		return tree.remove(entry);
			
	}
	

	public int size() {
		return tree.count;
	}

	public Entry[] getAllEntries() {
		return allEntries;
	}

	public void setAllEntries(Entry[] allEntries) {
		this.allEntries = allEntries;
	}
	
	public String printList()
	{
		return tree.printTree();
	}
	
	public Entry searchByName(String entry)
	{
		//return tree.search(new Entry(entry), tree.getRoot());
		return tree.search(new Entry(entry));
	}
	
	public boolean entryDoesExist(String entry)
	{
            
		return !tree.contains(new Entry(entry));
	}
	
	
	public void writeNodesToFile(FileWriter writer) throws IOException
	{
		tree.writeNodesToFile(tree.getRoot(),writer);
	}

    private void setCount(int i) {
    	this.count = i;
    }

}