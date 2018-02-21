import java.util.*;

// This is a node in a character tree.  If the confidence is 1 or more that indicates that it is
// the last character of a word.  The nextNodes is an ordered array of nodes representing all
// the next characters after this one. Collections.binarySearch is used to optimize finding 
// existing nodes and inserting new nodes into the tree.
public class CharNode implements Comparable<CharNode> {
	private char ch;
	private int confidence;
	private List<CharNode> nextNodes;
	
	public CharNode(char c) {
		ch = c;
		confidence = 0;
		nextNodes = null;
	}
	
	public char getCh() {
		return ch;
	}
	
	public void setCh(char c) {
		ch = c;
	}
	
	public int getConfidence() {
		return confidence;
	}
	
	public int increaseConfidence() {
		confidence++;
		return confidence;
	}
	
	public List<CharNode> getNextNodes() {
		return nextNodes;
	}
	
	// This allows the binary search to work
	public int compareTo(CharNode charNode) {
		return ch - charNode.ch;
	}
	
	// This is called to add or find the next character after this one.
	public CharNode addNext(char c) {
		if(nextNodes == null) {
			nextNodes = new ArrayList<>();			
		}
		return addToNodes(c, nextNodes);
	}
	
	// This is called to add or find a node with the character to the sorted nodes list.
	public static CharNode addToNodes(char c, List<CharNode> nodes) {
		CharNode node = new CharNode(c);
		int i = Collections.binarySearch(nodes, node);
		if(i >= 0) {
			return nodes.get(i);
		} else {
			nodes.add(-i-1, node);
			return node;
		}
	}
	
}