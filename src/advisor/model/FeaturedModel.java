package advisor.model;

import advisor.utils.JsonAnalyzer;

import java.util.ArrayList;
import java.util.List;

public class FeaturedModel extends ApiModel{

    /**
     * Sets the featured model configuration.
     * @param limit - number of entries in a page.
     * @throws Exception - When the server fails to get a response from the API.
     */
    public FeaturedModel(int limit) throws Exception {
        super(limit, "v1/browse/featured-playlists", "playlists/");
    }

    /**
     * returns the all data coming from the featured model.
     *
     * @return names and urls of playlists requested from the API.
     */
    @Override
    public List<String[]> getData() {
        ArrayList<String[]> featuredData = new ArrayList<>();
        featuredData.add(JsonAnalyzer.getJsonInfo(json, "playlists/items-", "name", 0));
        featuredData.add(JsonAnalyzer.getJsonInfo(json, "playlists/items-external_urls/", "spotify", 0));
        return ConfigureData(featuredData);
    }
}
