package dogapi;

import java.io.IOException;
import java.util.List;

/**
 * Interface for the service of getting sub breeds of a given dog breed.
 */
public interface BreedFetcher {
    java.util.List<String> getSubBreeds(String breed)
            throws BreedNotFoundException;   // <-- no IOException here

    // Checked, semantic error for "invalid breed"
    class BreedNotFoundException extends Exception {
        public BreedNotFoundException(String message) { super(message); }
    }
}
