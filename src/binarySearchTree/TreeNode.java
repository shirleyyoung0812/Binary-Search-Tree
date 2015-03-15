package binarySearchTree;

public class TreeNode<T> {

	public Comparable<T> key;
	public Object data;
	protected TreeNode<T> left;
	protected TreeNode<T> right;
	public TreeNode(Comparable<T> key, Object obj) { 
		this.key = key;
		data = obj;
		left = null;
		right = null;
	}
	
	public TreeNode(Comparable<T> key, Object obj, TreeNode<T> left, TreeNode<T> right) {
		this.key = key;
		data = obj;
		this.left = left;
		this.right = right;
	}
	
}
