package binarySearchTree;


public class BSTTester {

	public static void main(String[] args) {
		BST<Character> rb = new BST<Character>();
		rb.insert('A', 8);
		rb.insert('E', 12);
		rb.insert('M', 9);
		rb.insert('C', 4);
		rb.insert('P', 10);
		rb.insert('S', 0);
		rb.insert('H', 5);
		rb.insert('X', 7);
		rb.insert('L', 11);
		rb.insert('R', 3);
		//System.out.println(rb.contains('A'));
		rb.printTree();

	}

}
