package binarySearchTree;


import java.util.LinkedList;
import java.util.Queue;

/**
 * every tree node has a unique key
 * The insertion, search and delete all guarantee O(log n) time
 * Applications: used in many search applications where data 
 * is constantly entering/leaving, such as map and set objects
 * 
 * @author shirleyyoung
 *
 */
public class BST<T> {
	private TreeNode<T> root;
	
	public BST() {
		root = null;
	}
	/**
	 * search if a key exists
	 * time: the depth of the tree
	 * worst case, a linear tree (seriously?), O(n)
	 * best case, balanced tree, O(log(n))
	 * @param k
	 * @return
	 */
	public boolean contains(Comparable<T> k) {
		return containsKey(k, root);
	}
	@SuppressWarnings("unchecked")
	protected boolean containsKey(Comparable<T> k, TreeNode<T> root) {
		if (root == null){
			System.out.println("Null root");
			return false;
		}
		if (root.key == k){
			System.out.println("right key");
			return true;
		}
		if (root.key.compareTo((T) k) > 0){
			System.out.println("left subtree");
			return containsKey(k, root.left);
		}
		System.out.println("right subtree");
		return containsKey(k, root.right);
	}
	
	/**
	 * time: depth of the tree
	 * @param k
	 * @param obj
	 */
	public void insert(Comparable<T> k, Object obj) {
		if (contains ((Comparable<T>)k))
				throw new IllegalArgumentException("Key exists!"); 
		if (root == null) {
			root = new TreeNode<T>(k, obj, null, null);
			return;
		}
		insert(k, obj, root);
	}
	
	@SuppressWarnings("unchecked")
	private void insert(Comparable<T> k, Object obj, TreeNode<T> root) {
		if (root.key.compareTo((T) k) > 0) {
			if (root.left == null)
				root.left = new TreeNode<T>(k, obj, null, null);
			else
				insert(k, obj, root.left);
		}
		else {
			if (root.right == null)
				root.right = new TreeNode<T> (k, obj, null, null);
			else
				insert(k, obj, root.right);
		}
	}
	

	public void delete (Comparable<T> k) {
		root = delete(root, k);
	}
	/**
	 * if the node to be deleted is the root, 
	 * if either left child or right child is null, return the other
	 * else, find the minimum element in the right tree 
	 * or the maximum element in the left tree, replace the value and object of the root 
	 * with that node
	 * delete that element in the subtree
	 * if the node is not the root, recursively find the element and delete
	 * 
	 * the worst case, we need to 
	 * 1. find the node to be deleted, O(logn)
	 * 2. find the minimum element in the right tree or maximum element in the 
	 * left tree, O(log n)
	 * 3. recursively delete that element O(log n)
	 * so overall O(log n)
	 * @param root
	 * @param k
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private TreeNode<T> delete(TreeNode<T> root, Comparable<T> k) {
		// if no such node exists, tree is simply unchanged
		if (root == null)
			return null;
		if (k.equals(root.key)) {
			if (root.left == null && root.right == null)
				return null;
			if (root.left == null)
				return root.right;
			if (root.right == null)
				return root.left;
			//choose the minimum node in the right sub tree
			TreeNode<T> tmp = getMinimum(root);
			root.key = tmp.key;
			root.data = tmp.data;
			root.right = delete(root.right, tmp.key);
		}
		if (root.key.compareTo((T) k) > 0) {
			root.left = delete(root.left, k);
			return root;
		}
		root.right = delete(root.right, k);
		return root;
	}
	
	private TreeNode<T> getMinimum(TreeNode<T> root) {
		if (root.left == null)
			return root;
		return getMinimum(root.left);
	}
	/**
	 * if it is an empty tree
	 * @return
	 */
	public boolean isEmpty(){
		return root == null;
	}
	/*****************************************
	 * check integrity
	 *****************************************/
	protected boolean isBST(){
		return isBST(root, null, null);
	}
	@SuppressWarnings("unchecked")
	private boolean isBST(TreeNode<T> root, Comparable<T> min, Comparable<T> max){
		if(root == null)
			return true;
		if(min != null && root.key.compareTo((T)min) <= 0) return false;
		if(max != null && root.key.compareTo((T)max) >= 0) return false;
		return isBST(root.left, min, root.key) && isBST(root.right, root.key, max);
	}
	public boolean isBalanced(){
		return depth(root) != -1;
	}
	private int depth(TreeNode<T> root){
		if(root == null)
			return 0;
		int left = depth(root.left);
		int right = depth(root.right);
		if(left == -1 || right == -1 || Math.abs(left - right) > 1)
			return -1;
		return Math.max(left, right) + 1;
	}
	public void printTree ()
	{
		if (root == null)
			return;
		Queue<TreeNode<T>> queue = new LinkedList<TreeNode<T>> ();
		queue.offer(root);
		while (!queue.isEmpty())
		{
			int size = queue.size();
			for (int i = 0; i < size; i++)
			{
				TreeNode<T> head = queue.poll();
				System.out.print(head.key + " ");
				if(head.left != null)
					queue.offer(head.left);
				if(head.right != null)
					queue.offer(head.right);
			}
			System.out.println("");

		}
	}

}
