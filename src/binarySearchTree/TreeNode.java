package binarySearchTree;

public class TreeNode<T> {

	Comparable<T> key;
	Object data;
	TreeNode<T> left;
	TreeNode<T> right;
	TreeNode(Comparable<T> key, Object obj) { 
		this.key = key;
		data = obj;
		left = null;
		right = null;
	}
	
	TreeNode(Comparable<T> key, Object obj, TreeNode<T> left, TreeNode<T> right) {
		this.key = key;
		data = obj;
		this.left = left;
		this.right = right;
	}
	
}
