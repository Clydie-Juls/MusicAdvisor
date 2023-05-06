package advisor.model;

import advisor.utils.JsonAnalyzer;

import java.util.ArrayList;
import java.util.List;

public class NewReleasesModel extends ApiModel{

    /**
     * Sets the new releases model configuration.
     * @param limit - number of entries in a page.
     * @throws Exception - When the server fails to get a response from the API.
     */
    public NewReleasesModel(int limit) throws Exception {
        super(limit, "v1/browse/new-releases", "albums/");
    }

    /**
     * returns the all data coming from the new releases model.
     *
     * @return names, artists, and urls of the new releases requested from the API.
     */
    @Override
    public List<String[]> getData() {
        ArrayList<String[]> newReleasesData = new ArrayList<>();
        newReleasesData.add(JsonAnalyzer.getJsonInfo(json, "albums/items-", "name", 0));
        newReleasesData.add(JsonAnalyzer.getJsonInfo(json, "albums/items-", "artists-name", 4));
        newReleasesData.add(JsonAnalyzer.getJsonInfo(json, "albums/items-external_urls/", "spotify", 0));
        return ConfigureData(newReleasesData);
    }
}
