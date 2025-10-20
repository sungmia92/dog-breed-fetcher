package dogapi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
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
     * @throws IOException if a network error occurs
     * @throws BreedNotFoundException if the breed does not exist or the API returns an error
     */
    @Override
    public List<String> getSubBreeds(String breed) throws IOException, BreedNotFoundException {
        String url = "https://dog.ceo/api/breed/" + breed + "/list";
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new BreedNotFoundException(breed);
            }
            String responseBody = response.body().string();
            JSONObject jsonObject = new JSONObject(responseBody);
            String status = jsonObject.getString("status");
            if (!"success".equals(status)) {
                throw new BreedNotFoundException(breed);
            }
            JSONArray messageArray = jsonObject.getJSONArray("message");
            List<String> subBreeds = new ArrayList<>();
            for (int i = 0; i < messageArray.length(); i++) {
                subBreeds.add(messageArray.getString(i));
            }
            return subBreeds;
        }
    }
}