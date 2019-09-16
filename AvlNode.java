public class AvlNode<T> {

            // Constructors
        AvlNode( T theElement )
        {
            this( theElement, null, null );
        }

        AvlNode( T theElement, AvlNode<T> lt, AvlNode<T> rt )
        {
            element  = theElement;
            //left     = lt;
            //right    = rt;
            height   = 0;
        }

        T           element;      // The data in the node
        AvlNode<T>  left;         // Left child
        AvlNode<T>  right;        // Right child
        int               height;       // Height
    

}