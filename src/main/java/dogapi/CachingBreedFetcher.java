package dogapi;

import java.io.IOException;
import java.util.*;

/**
 * This BreedFetcher caches fetch request results to improve performance and
 * lessen the load on the underlying data source. An implementation of BreedFetcher
 * must be provided. The number of calls to the underlying fetcher are recorded.
 *
 * If a call to getSubBreeds produces a BreedNotFoundException, then it is NOT cached
 * in this implementation. The provided tests check for this behaviour.
 *
 * The cache maps the name of a breed to its list of sub breed names.
 */
public class CachingBreedFetcher implements BreedFetcher {
    private final BreedFetcher fetcher;           // the underlying fetcher (e.g. DogApiBreedFetcher)
    private final Map<String, List<String>> cache; // stores breed → sub-breeds
    private int callsMade = 0;                    // counts how many times we actually call the inner fetcher

    public CachingBreedFetcher(BreedFetcher fetcher) {
        this.fetcher = fetcher;
        this.cache = new HashMap<>();
    }

    @Override
    public List<String> getSubBreeds(String breed)
            throws BreedNotFoundException {

        // Normalize breed name to lowercase (so "Bulldog" and "bulldog" count as same)
        String key = breed.trim().toLowerCase();

        // 1️⃣ Check cache first
        if (cache.containsKey(key)) {
            return cache.get(key); // return the stored result instantly
        }

        // 2️⃣ Otherwise, fetch from the real API and store it
        callsMade++;
        List<String> result = fetcher.getSubBreeds(key);
        cache.put(key, result); // save the result so next time we skip the API call

        return result;
    }

    /** Optional helper so tests can check how many actual API calls happened. */
    public int getCallsMade() {
        return callsMade;
    }
}

