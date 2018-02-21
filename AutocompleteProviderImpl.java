import java.util.*;

// Implements the AutocompleteProvider interface
// Contains the root of the word tree which is an array of the first CharNodes in the tree.
public class AutocompleteProviderImpl implements AutocompleteProvider {
	
	List<CharNode> wordTree;
	
	public AutocompleteProviderImpl() {
		wordTree = new ArrayList<>();
	}
	
	// This returns the list of Candidates that match the word fragment.
	// This function has two parts.  First get the fragment node which
	// is the node matching the last character of the fragment.
	// If the fragmentNode is found, that means the fragment exists
	// and there will be candidates.  At this point, the private
	// getWords is called with the fragment and an initially empty
	// extraChars array that has characters added and removed for
	// each thread traveled in the tree.
	public List<Candidate> getWords(String fragment) {
		fragment = fragment.toLowerCase();
		List<Candidate> candidates = new ArrayList<>();
		StringBuilder sb = new StringBuilder(fragment.length());
		CharNode fragmentNode = getFragmentNode(fragment, sb);
		if(fragmentNode != null) {
			List<Character> extraChars = new ArrayList<>();
			getWords(candidates, fragmentNode, sb, sb.length(), extraChars);
		}
		return candidates;
	}
	
	// This function splits the passage up into words and adds each
	// word to the tree.
	public void train(String passage) {
		String[] words = passage.split("\\s");
		for(int i = 0; i < words.length; i++) {
			addWord(words[i]);
		}		
	}
	
	// This uses the binarySearch function to find each character of the
	// fragment in the tree.  It uses the same searchNode to reduce
	// memory reallocation.  Only letters are processed so hyphens in
	// words are ignored.  This could be easily addressed with a bit more code.
	// If the fragment is not found, null is returned.
	private CharNode getFragmentNode(String fragment, StringBuilder sb) {
		CharNode fragmentNode = null;
		CharNode searchNode = new CharNode((char)0);
		List<CharNode> nodes = wordTree;
		for(int i = 0; i < fragment.length(); i++) {
			if(nodes == null) {
				return null;
			}
			char ch = fragment.charAt(i);
			if(Character.isLetter(ch)) {
				ch = Character.toLowerCase(ch);
				sb.append(ch);
				searchNode.setCh(ch);
				int index = Collections.binarySearch(nodes, searchNode);
				if(index < 0) {
					return null;
				}
				fragmentNode = nodes.get(index);
				nodes = fragmentNode.getNextNodes();
			}
		}
		return fragmentNode;
	}
	
	// This is called when the fragment is successfully found.  This is recursively called to process every node in 
	// the fragmentNode subtree including the fragmentNode.  Any node which has a confidence of 1 or more, represents 
	// the end of a word and is a candidate.  The same extraChars array is reused to reduce memory reallocation.  
	// When a word is found, the fragment is concatenated with the extraChars to create a string.
	// The same StringBuilder is used with the fragment to improve performance.
	// Candidates are sorted by confidence and then word.
	private void getWords(List<Candidate> candidates, CharNode charNode, StringBuilder sb, int fragLen, List<Character> extraChars) {
		if(charNode.getConfidence() > 0) {
			sb.setLength(fragLen);
			for(int i = 0; i < extraChars.size(); i++) {
				sb.append(extraChars.get(i));
			}
			Candidate candidate = new Candidate(sb.toString(), charNode.getConfidence());
			int i = Collections.binarySearch(candidates, candidate);
			if(i < 0) {
				candidates.add(-i-1, candidate);
			}
		}
		List<CharNode> nodes = charNode.getNextNodes();
		int lastIndex = extraChars.size();
		if(nodes != null) {
			for(CharNode node : nodes) {
				extraChars.add(new Character(node.getCh()));
				getWords(candidates, node, sb, fragLen, extraChars);
				extraChars.remove(lastIndex);
			}
		}
	}
	
	// This adds a word to the tree ignoring any non letters.
	// At the end, increase the confidence of the last node.
	private void addWord(String word) {
		CharNode nextNode = null;
		for(int i = 0; i < word.length(); i++) {
			char ch = word.charAt(i);
			if(Character.isLetter(ch)) {
				ch = Character.toLowerCase(ch);
				if(nextNode == null) {
					nextNode = CharNode.addToNodes(ch, wordTree);
				} else {
					nextNode = nextNode.addNext(ch);
				}
			}
		}
		if(nextNode != null) {
			nextNode.increaseConfidence();
		}
	}
}