package dogapi;

import java.io.IOException;
import java.util.List;

/**
 * Interface for the service of getting sub breeds of a given dog breed.
 */
public interface BreedFetcher {

    java.util.List<String> getSubBreeds(String breed)
            throws java.io.IOException, BreedNotFoundException;

    // Checked exception
    class BreedNotFoundException extends Exception {
        public BreedNotFoundException(String breed) {
            super("Breed not found: " + breed);
        }
    }
}