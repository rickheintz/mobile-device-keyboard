// The Candidate contains the Candidate information as well as 
// implements Comparable so that higher confidence comes first.
// Also sorts by word when confidence is equal.
public class Candidate implements Comparable<Candidate> {
	private String word;
	private Integer confidence;
	
	public Candidate(String word, int confidence) {
		this.word = word;
		this.confidence = confidence;
	}
	public String getWord() {
		return word;
	}
	
	public Integer getConfidence() {
		return confidence;
	}
		
	// This allows the binary search to work
	public int compareTo(Candidate candidate) {
		int result = candidate.confidence - this.confidence;
		if(result == 0) {
			result = this.word.compareTo(candidate.word);
		}
		return result;
	}
}