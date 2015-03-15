package redBlackTree;

import java.awt.Color;




public class ColoredTreeNode<T> {
	Comparable<T> key;
	Object data; 
	Color color;
	ColoredTreeNode<T> left;
	ColoredTreeNode<T> right;
	int numSubtree; //subtree count
	public ColoredTreeNode(Comparable<T> key, Object data, Color c, int N){
		this.key = key;
		this.data = data;
		left = null;
		right = null;
		color = c;
		numSubtree = N;
	}

}
