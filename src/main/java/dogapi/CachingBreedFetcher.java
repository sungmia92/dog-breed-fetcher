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
    // TODO Task 2: Complete this class
    private final BreedFetcher fetcher;
    private final Map<String, List<String>> cache = new HashMap<>();
    private int callsMade = 0;

    public CachingBreedFetcher(BreedFetcher fetcher) {
        this.fetcher = fetcher;
    }

    @Override
    public List<String> getSubBreeds(String breed) {
        // Return cached value if available
        if (cache.containsKey(breed)) {
            return cache.get(breed);
        }

        try {
            callsMade++;
            List<String> result = fetcher.getSubBreeds(breed);
            // Cache successful result
            cache.put(breed, List.copyOf(result));
            return result;
        } catch (BreedNotFoundException e) {
            // Do not cache failed requests; rethrow
            throw e;
        } catch (IOException e) {
            // Wrap IOException in an unchecked exception since we can't declare it
            throw new RuntimeException(e);
        }
    }

    public int getCallsMade() {
        return callsMade;
    }
}