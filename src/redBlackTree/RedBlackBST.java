package redBlackTree;

import java.awt.Color;
import java.util.*;

/**
 * This class implements a left leaning red-black tree
 * if there is an extra node, it will always be at the 
 * left side
 * @author shirleyyoung
 * @param <T>
 *
 * @param <T>
 */

public class RedBlackBST<T> {
	private ColoredTreeNode<T> root;
	
	/**
	 * constructor
	 */
	public RedBlackBST(){
		root = null;
		//System.out.println("New tree constructed!");
	}
	
	/************************************************
	 * Search
	 ************************************************/
	public boolean contains(Comparable<T> k) {
		return get(k) != null;
	}
	public Object get(Comparable<T> key){
		return get(root, key);
	}
	private Object get(ColoredTreeNode<T> node, Comparable<T> k){
		while(node != null){
			int cmp = node.key.compareTo((T)k);
			if(cmp > 0) node = node.left;
			else if(cmp < 0) node = node.right;
			else return node.data;
		}
		return null;
	}
	/*************************************************
	 * insertion
	 *************************************************/
	public void insert(Comparable<T> k, Object obj){
		root = insert(k, obj, root);
		//System.out.println(root.key);
		root.color = Color.black;
		assert check();
	}
	private ColoredTreeNode<T> insert(Comparable<T> k, Object obj, ColoredTreeNode<T> root){
		if(root == null){
			//System.out.println("Inserted new node!");
			return new ColoredTreeNode<T>(k, obj, Color.red, 1);
		}
		int cmp = k.compareTo((T)root.key);
		if(cmp < 0)
			root.left = insert(k, obj, root.left);
		else if (cmp > 0)
			root.right = insert(k, obj, root.right);
		else
			root.data = obj;
		/*
		 * rotate and re-adjust color
		 */
		//the new added node is the right child 
		//of the root, since the tree is left leaning
		//we need to left rotate the tree
		if(isRed(root.right) && !isRed(root.left))
			root = rotateLeft(root);
		//two consecutive red nodes, rotate right
		if(isRed(root.left) && isRed(root.left.left))
			root = rotateRight(root);
		//a red node must have two black children
		//flip the color if a node have two red children
		//if the parent is black, and the parent's parent is 
		//red, flip the nodes will cause two red parents on the 
		//left, and the tree will rotate right to correct it
		if(isRed(root.left) && isRed(root.right))
			flipColors(root);
		root.numSubtree = size(root.left) + size(root.right) + 1;
		//System.out.println(root.key);
		return root;	
	}
	/*****************************************************
	 * Deletion
	 *****************************************************/
	/**
	 * delete the node with minimum key
	 */
	public void deleteMin(){
		if(isEmpty())
			throw new NoSuchElementException("Empty tree!");
		if(!isRed(root.left) && !isRed(root.right))
			root.color = Color.red;
		root = deleteMin(root);
		if(!isEmpty())
			root.color = Color.black;
	}
	private ColoredTreeNode<T> deleteMin(ColoredTreeNode<T> node){
		if(node.left == null)
			return null;
		if(!isRed(node.left) && !isRed(node.left.left))
			node = moveRedLeft(node);
		node.left = deleteMin(node.left);
		return balance(node);
	}
	/**
	 * delete the node with the maximum key
	 */
	public void deleteMax(){
		if(isEmpty())
			throw new NoSuchElementException("Empty tree!");
		//delete a red node
		if(!isRed(root.left) && !isRed(root.right))
			root.color = Color.red;
		root = deleteMax(root);
		if(!isEmpty()) 
			root.color = Color.black;
	}
	private ColoredTreeNode<T> deleteMax(ColoredTreeNode<T> node){
		//note the root is red from the above method
		if(isRed(node.left))
			root = rotateRight(node);
		//BST, max is always at right
		if(node.right == null)
			return null;
		if(!isRed(node.right) && !isRed(node.right.left))
			node = moveRedRight(node);
		node.right = deleteMax(node.right);
		return balance(node);
	}
	/**
	 * delete a node given a key
	 * @param key
	 */
	public void delete(Comparable<T> key){
		if(!contains(key)){
			System.err.println("Symbol table does not contain " + key);
			return;
		}
		if(!isRed(root.left) && !isRed(root.right))
			root.color = Color.red;
		root = delete(root, key);
		if(!isEmpty())
			root.color = Color.black;
		assert check();
	}
	private ColoredTreeNode<T> delete(ColoredTreeNode<T> node, Comparable<T> key){
		if(key.compareTo((T)node.key) < 0){
			if(!isRed(node.left) && !isRed(node.left.left))
				node = moveRedLeft(node);
			node.left = delete(node.left, key);
		}
		else {
			if(isRed(node.left))
				node = rotateRight(node);
			if(key.compareTo((T)node.key) == 0 && (node.right == null))
				return null;
			if(!isRed(node.right) && !isRed(node.right.left))
				node = moveRedRight(node);
			if(key.compareTo((T)node.key) == 0){
				//find minimum node in the right subtree
				ColoredTreeNode<T> x = min(node.right);
				node.key = x.key;
				node.data = x.data;
				node.right = deleteMin(node.right);
			}
			else
				node.right = delete(node.right, key);
		}
		return balance(node);	
	}
	/**********************************************
	 * reconstruct methods
	 **********************************************/
	
	private ColoredTreeNode<T> rotateRight(ColoredTreeNode<T> node){
		ColoredTreeNode<T> x = node.left;
		node.left = x.right;
		x.right = node;
		x.color = x.right.color;
		x.right.color = Color.red;
		x.numSubtree = node.numSubtree;
		node.numSubtree = size(node.left) + size(node.right) + 1;
		return x;
	}
	
	private ColoredTreeNode<T> rotateLeft(ColoredTreeNode<T> node){
		ColoredTreeNode<T> x = node.right;
		node.right = x.left;
		x.left = node;
		x.color = x.left.color;
		x.left.color = Color.red;
		x.numSubtree = node.numSubtree;
		node.numSubtree = size(node.left) + size(node.right) + 1;
		return x;
	}
	/**
	 * flip the color of the node and its children
	 * @param node
	 */
	private void flipColors(ColoredTreeNode<T> node){
		node.color = node.color == Color.red ? Color.black : Color.red;
		node.left.color = node.left.color == Color.red ? Color.black : Color.red;
		node.right.color = node.right.color == Color.red ? Color.black : Color.red; 
	}
	/**
	 * Assuming that node is red and both node.left and node.left.left
	 * are black, make node.left or one of its children red
	 * @param node
	 * @return
	 */
	private ColoredTreeNode<T> moveRedLeft(ColoredTreeNode<T> node){
		flipColors(node);
		if(isRed(node.right.left)){
			node.right = rotateRight(node.right);
			node = rotateLeft(node);
			flipColors(node);
		}
		return node;
	}
	/**
	 * Assuming node is red and both node.right and node.right.left
	 * are black, make node.right or one of its children red
	 * @param node
	 * @return
	 */
	private ColoredTreeNode<T> moveRedRight(ColoredTreeNode<T> node){
		flipColors(node);
		if(isRed(node.left.left)){
			node = rotateRight(node);
			flipColors(node);
		}
		return node;
	}
	private ColoredTreeNode<T> balance(ColoredTreeNode<T> node){
		if(isRed(node.right))
			node = rotateLeft(node);
		if(isRed(node.left) && isRed(node.left.left))
			node = rotateRight(node);
		if(isRed(node.left) && isRed(node.right))
			flipColors(node);
		node.numSubtree = size(node.left) + size(node.right) + 1;
		return node;
	}
	/***************************************************
	 * Other helper and utility methods
	 ***************************************************/
	/**
	 * check if a tree node is red,
	 * if not, it is black
	 * @param node
	 * @return
	 */
	private boolean isRed(ColoredTreeNode<T> node){
		if(node == null)
			return false;
		return node.color == Color.red;
	}
	/**
	 * return number of nodes in the tree
	 * @return
	 */
	public int size(){
		return size(root);
	}
	/**
	 * return number of nodes in the subtree rooted at node
	 * @param node
	 * @return
	 */
	private int size(ColoredTreeNode<T> node){
		if(node == null)
			return 0;
		return node.numSubtree;
	}
	
	/**
	 * find the node with the smallest key in subtree rooted at node
	 * @param node
	 * @return
	 */
	public Comparable<T> min(){
		if(isEmpty())
			return null;
		return min(root).key;
	}
	private ColoredTreeNode<T> min(ColoredTreeNode<T> node){
		if(node.left == null)
			return node;
		return min(node.left);
	}
	/**
	 * find the node with the largest key in subtree rooted at node
	 * @param node
	 * @return
	 */
	public Comparable<T> max(){
		if(isEmpty())
			return null;
		return max(root).key;
	}
	private ColoredTreeNode<T> max(ColoredTreeNode<T> node){
		if(node.right == null)
			return node;
		return max(node.right);
	}
	
	/**
	 * if it is an empty tree
	 * @return
	 */
	public boolean isEmpty(){
		return root == null;
	}
	
	/*******************************************************
	 * check integrity of the tree
	 *******************************************************/
	private boolean check(){
		if(!isBST()) 
			System.out.println("Not a BST!");
		if(!isSizeConsistent())
			System.out.println("Subtree counts not consistent!");
		if(!isBalanced())
			System.out.println("Not balanced tree!");
		return isBST() && isSizeConsistent() && isBalanced();
	}
	private boolean isSizeConsistent(){
		return isSizeConsistent(root);
	}
	private boolean isSizeConsistent(ColoredTreeNode<T> root){
		if(root == null)
			return true;
		if(root.numSubtree != size(root.left) + size(root.right) + 1)
			return false;
		return isSizeConsistent(root.left) && isSizeConsistent(root.right);
	}
	private boolean isBST(){
		return isBST(root, null, null);
	}
	private boolean isBST(ColoredTreeNode<T> node, Comparable<T> min, Comparable<T> max){
		if(node == null) return true;
		if(min != null && node.key.compareTo((T)min) <= 0) return false;
		if(max != null && node.key.compareTo((T)max) >= 0) return false;
		return isBST(node.left, min, node.key) && isBST(node.right, node.key, max);
	}
	/**
	 * check if every root to leaf path have the same number
	 * of black nodes
	 * @return
	 */
	public boolean isBalanced(){
		int blackNodes = 0;
		ColoredTreeNode<T> node = root;
		while(node != null){
			if(!isRed(node))
				blackNodes++;
			node = node.left;
		}
		return isBalanced(root, blackNodes);
	}
	private boolean isBalanced(ColoredTreeNode<T> node, int blackNodes){
		if(node == null) return blackNodes == 0;
		if(!isRed(node)) blackNodes--;
		return isBalanced(node.left, blackNodes) && isBalanced(node.right, blackNodes);
	}
	/**
	 * output for check
	 */
	public void printTree ()
	{
		if (root == null)
			return;
		Queue<ColoredTreeNode<T>> queue = new LinkedList<ColoredTreeNode<T>> ();
		queue.offer(root);
		//System.out.println(queue.peek().key);
		while (!queue.isEmpty())
		{
			int size = queue.size();
			for (int i = 0; i < size; i++)
			{
				ColoredTreeNode<T> head = queue.poll();
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
