
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
public class AVLTree<T extends Comparable<T>>
{
	/**
	 * Construct the tree.
	 */
	public AVLTree( )
	{
		root = null;
	}
	public AVLTree(Comparator<Entry> comparator )
	{
		root = null;
		this.comp = comparator;
	}

	/**
	 * Insert into the tree; duplicates are ignored.
	 * @param x the item to insert.
	 */
	public void insert( Entry x )
	{

		root = insert(x, root);

	}

	/**
	 * Remove from the tree. Nothing is done if x is not found.
	 * @param x the item to remove.
	 * @return 
	 */
	public String remove( Entry x )
	{
		root = remove(x, root);
		if(root == null)
		{
			return x.getNameRevised() + "not deleted";
		}
		else{
			return x.getNameRevised() + "deleted";
		}
	}
	/**
	 * Internal method to remove from a subtree.
	 * @param x the item to remove.
	 * @param t the node that roots the subtree.
	 * @return the new root of the subtree.
	 */
	private AvlNode<Entry> remove( Entry x, AvlNode<Entry> t )
	{
		if( t == null )
			return t;   // Item not found; do nothing

		int compareResult = compareObject(x,t.element);

		if( compareResult < 0 )
			t.left = remove( x, t.left );
		else if( compareResult > 0 )
			t.right = remove( x, t.right );
		else if( t.left != null && t.right != null ) // Two children
		{
			t.element = findMin( t.right ).element;
			t.right = remove( t.element, t.right );
		}
		else
			t = ( t.left != null ) ? t.left : t.right;
		return balance( t );
	}

	/**
	 * Find the smallest item in the tree.
	 * @return smallest item or null if empty.
	 */
	public Entry findMin( )
	{
		if( isEmpty( ) )
			throw new UnderflowException( );
		return findMin( root ).element;
	}

	/**
	 * Find the largest item in the tree.
	 * @return the largest item of null if empty.
	 */
	public Entry findMax( )
	{
		if( isEmpty( ) )
			throw new UnderflowException( );
		return findMax( root ).element;
	}

	/**
	 * Find an item in the tree.
	 * @param x the item to search for.
	 * @return true if x is found.
	 */
	public boolean contains( Entry x )
	{
		return contains( x, root );
	}

	/**
	 * Make the tree logically empty.
	 */
	public void makeEmpty( )
	{
		root = null;
	}

	/**
	 * Test if the tree is logically empty.
	 * @return true if empty, false otherwise.
	 */
	public boolean isEmpty( )
	{
		return root == null;
	}

	/**
	 * Print the tree contents in sorted order.
	 * @return 
	 */
	public String printTree( )
	{
		if( isEmpty( ) )
			return "Empty tree";
		else
			return printTree( root );
	}

	private static final int ALLOWED_IMBALANCE = 1;

	// Assume t is either balanced or within one of being balanced
	private AvlNode<Entry> balance( AvlNode<Entry> t )
	{
		if( t == null )
			return t;

		if( height( t.left ) - height( t.right ) > ALLOWED_IMBALANCE )
			if( height( t.left.left ) >= height( t.left.right ) )
				t = rotateWithLeftChild( t );
			else
				t = doubleWithLeftChild( t );
		else
			if( height( t.right ) - height( t.left ) > ALLOWED_IMBALANCE )
				if( height( t.right.right ) >= height( t.right.left ) )
					t = rotateWithRightChild( t );
				else
					t = doubleWithRightChild( t );

		t.height = Math.max( height( t.left ), height( t.right ) ) + 1;
		return t;
	}

	public void checkBalance( )
	{
		checkBalance( root );
	}

	private int checkBalance( AvlNode<Entry> t )
	{
		if( t == null )
			return -1;

		if( t != null )
		{
			int hl = checkBalance( t.left );
			int hr = checkBalance( t.right );
			if( Math.abs( height( t.left ) - height( t.right ) ) > 1 ||
					height( t.left ) != hl || height( t.right ) != hr )
				System.out.println( "OOPS!!" );
		}

		return height( t );
	}


	/**
	 * Internal method to insert into a subtree.
	 * @param x the item to insert.
	 * @param t the node that roots the subtree.
	 * @return the new root of the subtree.
	 */
	private AvlNode<Entry> insert( Entry x, AvlNode<Entry> t )
	{
		if( t == null ){
			//System.out.println("Node is null");
			count++;
			return new AvlNode<>( x, null, null );
		}
		int compareResult = compareObject(x, t.element );

		if( compareResult < 0 ){
			t.left = insert( x, t.left );
		}
		else if( compareResult > 0 ){
			t.right = insert( x, t.right );
		}
		else
		{
			System.out.println(x.getNameRevised());// Duplicate; do nothing
		}
		return balance( t );
	}

	/**
	 * Internal method to find the smallest item in a subtree.
	 * @param t the node that roots the tree.
	 * @return node containing the smallest item.
	 */
	private AvlNode<Entry> findMin( AvlNode<Entry> t )
	{
		if( t == null )
			return t;

		while( t.left != null )
			t = t.left;
		return t;
	}

	/**
	 * Internal method to find the largest item in a subtree.
	 * @param t the node that roots the tree.
	 * @return node containing the largest item.
	 */
	private AvlNode<Entry> findMax( AvlNode<Entry> t )
	{
		if( t == null )
			return t;

		while( t.right != null )
			t = t.right;
		return t;
	}

	/**
	 * Internal method to find an item in a subtree.
	 * @param x is item to search for.
	 * @param t the node that roots the tree.
	 * @return true if x is found in subtree.
	 */
	private boolean contains(Entry x, AvlNode<Entry> t)
	{
		while( t != null )
		{
			int compareResult = compareObject(x,t.element);

			if( compareResult < 0 )
				t = t.left;
			else if( compareResult > 0 )
				t = t.right;
			else
				return true;    // Match
		}

		return false;   // No match
	}

	public Entry search( Entry x, AvlNode<Entry> t )
	{
		while( t != null )
		{
			int compareResult = compareObject(x, t.element );

			if( compareResult < 0 )
				t = t.left;
			else if( compareResult > 0 )
				t = t.right;
			else
				return t.element;    // Match
		}

		return null;   // No match
	}

	public AvlNode<Entry> getRoot()
	{
		return root;
	}
	/**
	 * Internal method to print a subtree in sorted order.
	 * @param t the node that roots the tree.
	 * @return 
	 */
	private String printTree( AvlNode<Entry> t )
	{	
		String str="";
		if( t != null )
		{
			printTree( t.left );
			str=str+t.element.getName() + "\n" + t.element.toString();
			printTree( t.right );
		}
		return str;
	}

	@Override
	public String toString()
	{
		AvlNode<Entry> t = getRoot();
		String toReturn = "";
		if( t != null )
		{
			printTree( t.left );
			toReturn = toReturn + t.element.getName() + "\n" + t.element.toString();
			printTree( t.right );
		}
		return toReturn;
	}

	/**
	 * Return the height of node t, or -1, if null.
	 */
	private int height( AvlNode<Entry> t )
	{
		return t == null ? -1 : t.height;
	}

	/**
	 * Rotate binary tree node with left child.
	 * For Avl trees, this is a single rotation for case 1.
	 * Update heights, then return new root.
	 */
	private AvlNode<Entry> rotateWithLeftChild( AvlNode<Entry> k2 )
	{
		AvlNode<Entry> k1 = k2.left;
		k2.left = k1.right;
		k1.right = k2;
		k2.height = Math.max( height( k2.left ), height( k2.right ) ) + 1;
		k1.height = Math.max( height( k1.left ), k2.height ) + 1;
		return k1;
	}

	/**
	 * Rotate binary tree node with right child.
	 * For Avl trees, this is a single rotation for case 4.
	 * Update heights, then return new root.
	 */
	private AvlNode<Entry> rotateWithRightChild( AvlNode<Entry> k1 )
	{
		AvlNode<Entry> k2 = k1.right;
		k1.right = k2.left;
		k2.left = k1;
		k1.height = Math.max( height( k1.left ), height( k1.right ) ) + 1;
		k2.height = Math.max( height( k2.right ), k1.height ) + 1;
		return k2;
	}

	/**
	 * Double rotate binary tree node: first left child
	 * with its right child; then node k3 with new left child.
	 * For Avl trees, this is a double rotation for case 2.
	 * Update heights, then return new root.
	 */
	private AvlNode<Entry> doubleWithLeftChild( AvlNode<Entry> k3 )
	{
		k3.left = rotateWithRightChild( k3.left );
		return rotateWithLeftChild( k3 );
	}



	public void writeNodesToFile(AvlNode<Entry> current,  FileWriter file) throws IOException
	{
		FileWriter writer = file;
		Iterator<Entry> iter = iteratorInorder();
		while(iter.hasNext())

		{
			Entry temp = iter.next();
			writer.write(temp.getNameRevised()+ temp.toString() + "\n");
		}

		writer.close();
	}

	private int compareObject(Entry o1, Entry o2)
	{
		if(comp != null)
		{
			return comp.compare((Entry) o1,(Entry) o2);
		}
		else {
			return o1.compareTo(o2);
		}
	}
	public EntryManagement resortList(Comparator comp)
	{
		return resortList(getRoot());
	}

	private EntryManagement resortList(AvlNode<Entry> entry)
	{
		EntryManagement newEntryArray = new EntryManagement(comp);
		AvlNode<Entry> current = entry;
		if( current != null )
		{
			resortList( current.left );
			newEntryArray.add(current.element);
			//System.out.println(current.element.getNameRevised());
			resortList( current.right );
		}
		return newEntryArray;
	}

	/**
	 * Double rotate binary tree node: first right child
	 * with its left child; then node k1 with new right child.
	 * For Avl trees, this is a double rotation for case 3.
	 * Update heights, then return new root.
	 */
	private AvlNode<Entry> doubleWithRightChild( AvlNode<Entry> k1 )
	{
		k1.right = rotateWithLeftChild( k1.right );
		return rotateWithRightChild( k1 );
	}

	/** The tree root. */
	private AvlNode<Entry> root;
	private Comparator<Entry> comp;
	public int count;


	public Iterator<Entry> iteratorInorder() { 
		// TODO Auto-generated method stub
		ArrayList<Entry> myList = new ArrayList<Entry>();
		inorder(myList, root);
		return myList.iterator();
	}

	private void inorder(ArrayList<Entry> myList, AvlNode<Entry> node) { //O(n)
		// TODO Auto-generated method stub
		if(node != null) {
			inorder(myList, node.left);
			myList.add(node.element);
			inorder(myList,node.right);
		}
	}
	public Entry search(Entry x)
	{
		Iterator<Entry> iter = iteratorInorder();
		while(iter.hasNext()) 
		{Entry temp = iter.next();
		if(temp.compareTo(x) == 0)
		{
			return temp;
		}

		}
		return null;

	}
}