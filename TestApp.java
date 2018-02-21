import java.util.*;

// Test App to test train and getWords
class TestApp {
	public static void main(String args[]) {
		AutocompleteProvider provider = new AutocompleteProviderImpl();
		String trainStr = "The third thing that I need to tell you is that this thing does not think thoroughly.";
		System.out.println("Train: \"" + trainStr + "\"");
		provider.train(trainStr);
		printCandidatesFor(provider, "thi");
		printCandidatesFor(provider, "nee");
		printCandidatesFor(provider, "th");
	}
	
	public static void printCandidatesFor(AutocompleteProvider provider, String fragment) {
		List<Candidate> candidates = provider.getWords(fragment);
		System.out.print("Input: \"" + fragment + "\" --> ");
		boolean first = true;
		for(Candidate candidate : candidates) {
			if(first) {
				first = false;
			} else {
				System.out.print(", ");
			}
			System.out.print("\"" + candidate.getWord() + "\" (" + candidate.getConfidence() + ")");
		}
		System.out.println();
		
	}
}