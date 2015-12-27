package com.devoler.minimax;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class TreeNode implements Iterable<TreeNode> {
	private final List<TreeNode> children = new ArrayList<>();
	private final long value;
	
	public TreeNode(long value, TreeNode... children) {
		this.value = value;
		for(TreeNode child: children) {
			this.children.add(child);
		}
	}
	
	public long getValue() {
		return value;
	}
	
	@Override
	public Iterator<TreeNode> iterator() {
		return children.iterator();
	}
	
	public int getChildCount() {
		return children.size();
	}
	
	public TreeNode getChildAt(int childIndex) {
		return children.get(childIndex);
	}

	// taken from http://www.connorgarvey.com/blog/?p=82
	/**
	 * Creates a tree representation of the node
	 * @param node The node, which may not be null
	 * @return A string containing the formatted tree
	 */
	public static String toStringTree(TreeNode node) {
	  final StringBuilder buffer = new StringBuilder();
	  return toStringTreeHelper(node, buffer, new LinkedList<Iterator<TreeNode>>()).toString();
	}
	 
	private static String toStringTreeDrawLines(List<Iterator<TreeNode>> parentIterators, boolean amLast) {
	  StringBuilder result = new StringBuilder();
	  Iterator<Iterator<TreeNode>> it = parentIterators.iterator();
	  while (it.hasNext()) {
	    Iterator<TreeNode> anIt = it.next();
	    if (anIt.hasNext() || (!it.hasNext() && amLast)) {
	      result.append("   |");
	    }
	    else {
	      result.append("    ");
	    }
	  }
	  return result.toString();
	}
	 
	private static StringBuilder toStringTreeHelper(TreeNode node, StringBuilder buffer, List<Iterator<TreeNode>>
	    parentIterators) {
	  if (!parentIterators.isEmpty()) {
	    boolean amLast = !parentIterators.get(parentIterators.size() - 1).hasNext();
	    buffer.append("\n");
	    String lines = toStringTreeDrawLines(parentIterators, amLast);
	    buffer.append(lines);
	    buffer.append("\n");
	    buffer.append(lines);
	    buffer.append("- ");
	  }
	  buffer.append(node.toString());
	  if (node.getChildCount() > 0) {
	    Iterator<TreeNode> it = node.iterator();
	    parentIterators.add(it);
	    while (it.hasNext()) {
	      TreeNode child = it.next();
	      toStringTreeHelper(child, buffer, parentIterators);
	    }
	    parentIterators.remove(it);
	  }
	  return buffer;
	}
	
	@Override
	public String toString() {
		return String.format("[%s]", Long.toString(value));
	}
}