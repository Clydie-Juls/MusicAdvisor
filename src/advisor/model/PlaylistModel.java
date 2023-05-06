package advisor.model;

import advisor.utils.JsonAnalyzer;

import java.util.ArrayList;
import java.util.List;

public class PlaylistModel extends ApiModel {

    /**
     * Sets the playlist model configuration.
     * @param limit - number of entries in a page.
     * @throws Exception - When the server fails to get a response from the API.
     */
    public PlaylistModel(int limit, String id) throws Exception {
        super(limit, "v1/browse/categories/" + id + "/playlists", "playlists/");
    }

    /**
     * returns the all data coming from the playlist model.
     *
     * @return names and urls of playlists requested from the API.
     */
    @Override
    public List<String[]> getData() {
        ArrayList<String[]> playlistsData = new ArrayList<>();
        try {
            playlistsData.add(JsonAnalyzer.getJsonInfo(json, "playlists/items-", "name", 0));
            playlistsData.add(JsonAnalyzer.getJsonInfo(json, "playlists/items-external_urls/",
                    "spotify", 0));
        } catch (Exception e) {
            playlistsData.add(new String[]{json});
        }
        return ConfigureData(playlistsData);
    }

}
