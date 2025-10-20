package dogapi;

import java.io.IOException;
import java.util.*;

/**
 * Caching wrapper around a BreedFetcher. Caches successful fetches.
 */
public class CachingBreedFetcher implements BreedFetcher {
    private final BreedFetcher fetcher;
    private final Map<String, List<String>> cache = new HashMap<>();
    private int callsMade = 0;

    public CachingBreedFetcher(BreedFetcher fetcher) {
        this.fetcher = fetcher;
    }

    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        // Return cached result if available
        if (cache.containsKey(breed.toLowerCase())) {
            return cache.get(breed.toLowerCase());
        }

        try {
            // Call underlying fetcher
            List<String> subBreeds = fetcher.getSubBreeds(breed);
            // Cache the successful result
            cache.put(breed.toLowerCase(), subBreeds);
            callsMade++;
            return subBreeds;
        } catch (BreedNotFoundException e) {
            // Do NOT cache not-found results
            callsMade++;
            throw e;
        }
    }

    public int getCallsMade() {
        return callsMade;
    }
}