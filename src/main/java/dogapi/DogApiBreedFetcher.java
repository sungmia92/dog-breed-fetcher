package dogapi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

public class DogApiBreedFetcher implements BreedFetcher {
    private final OkHttpClient client = new OkHttpClient();

    @Override
    public List<String> getSubBreeds(String breed)
            throws IOException, BreedFetcher.BreedNotFoundException {
        try {
            String url = "https://dog.ceo/api/breed/" + breed + "/list";
            Request request = new Request.Builder().url(url).build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new BreedFetcher.BreedNotFoundException(breed);
                }

                String responseBody = response.body().string();
                JSONObject jsonObject = new JSONObject(responseBody);

                if (!"success".equals(jsonObject.getString("status"))) {
                    throw new BreedFetcher.BreedNotFoundException(breed);
                }

                JSONArray messageArray = jsonObject.getJSONArray("message");
                List<String> subBreeds = new ArrayList<>();
                for (int i = 0; i < messageArray.length(); i++) {
                    subBreeds.add(messageArray.getString(i));
                }

                return subBreeds;
            }
        } catch (IOException e) {
            throw new BreedFetcher.BreedNotFoundException(breed);
        }
    }
}