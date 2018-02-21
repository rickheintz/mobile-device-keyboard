import java.util.*;

public interface AutocompleteProvider {
	public List<Candidate> getWords(String fragment);
	public void train(String passage);
}