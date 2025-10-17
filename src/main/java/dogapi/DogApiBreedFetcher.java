package dogapi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

/**
 * BreedFetcher implementation that relies on the dog.ceo API.
 * Note that all failures get reported as BreedNotFoundException
 * exceptions to align with the requirements of the BreedFetcher interface.
 */
public class DogApiBreedFetcher implements BreedFetcher {
    private final OkHttpClient client = new OkHttpClient();

    /**
     * Fetch the list of sub breeds for the given breed from the dog.ceo API.
     *
     * @param breed the breed to fetch sub breeds for
     * @return list of sub breeds for the given breed
     * @throws BreedNotFoundException if the breed does not exist (or if the API call fails for any reason)
     */
    @Override
    public List<String> getSubBreeds(String breed) {
        if (breed == null || breed.trim().isEmpty()) {
            throw new IllegalArgumentException("Breed cannot be null or empty.");
        }

        String url = String.format("https://dog.ceo/api/breed/%s/list", breed.trim().toLowerCase());

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {

            if (response.body() == null) {
                throw new RuntimeException("No response body from Dog API");
            }


            String responseBody = response.body().string();

            JSONObject json = new JSONObject(responseBody);


            String status = json.optString("status", "");
            if (!status.equalsIgnoreCase("success")) {
                // If it’s not success, the API will usually include an error message
                String message = json.optString("message", "Breed not found");
                throw new IllegalArgumentException(message);
            }

            JSONArray subBreedsArray = json.getJSONArray("message");

            List<String> subBreeds = new ArrayList<>();
            for (int i = 0; i < subBreedsArray.length(); i++) {
                subBreeds.add(subBreedsArray.getString(i));
            }

            return subBreeds;

        } catch (IOException e) {
            throw new RuntimeException("Network error while contacting Dog API", e);
        } catch (JSONException e) {
            throw new RuntimeException("Error parsing Dog API response", e);
        }
    }
}