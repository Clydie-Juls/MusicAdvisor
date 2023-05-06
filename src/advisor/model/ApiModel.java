package advisor.model;

import advisor.server.Config;
import advisor.server.Server;
import advisor.utils.JsonAnalyzer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class ApiModel {
    private final int limit;
    private int offset;
    private int total;
    private final String relPath;
    private final String totalsPath;
    protected String json;


    /**
     * Initialize API model config and set data for response.
     * @param limit - Number of Entries in a page.
     * @param relPath - Relative path for the URI
     * @param totalsPath - Path of the "total" key in the json
     * @throws Exception - When the server fails to get a response from the API.
     */
    protected ApiModel(int limit, String relPath, String totalsPath) throws Exception {
        offset = 0;
        this.limit = limit;
        this.relPath = relPath;
        this.totalsPath = totalsPath;
        setModelData();
    }

    /**
     * Gets response based on the specific model issued.
     *
     * @return an array of a set of data from the response of a given API request.
     */
    public abstract List<String[]> getData();

    /**
     * Set API config to with regard to the subclasses' config.
     * @throws Exception - When the server fails to get a response from the API.
     */
    private void setModelData() throws Exception {
        String uri = String.format("%s/%s?offset=%d&limit=%d", Config.RESOURCE.getInfo(), relPath, offset, limit);
        json = Server.apiGetRequest(uri);
        if(offset >= 0) {
            total = Integer.parseInt(JsonAnalyzer.getJsonInfo(json, totalsPath, "total", 0)[0]);
        }
    }

    /**
     * Returns the next page of a paginated model data by adjusting the limit.
     * @return - next page data of a subclass.
     * @throws Exception - When the server fails to get a response from the API.
     */
    public List<String[]> next() throws Exception {
        offset += limit;
        setModelData();
        if(getPage() > getTotalPage()) {
            offset -= limit;
            return null;
        }

        return getData();
    }

    /**
     * Returns the previous page of a paginated model data by adjusting the limit.
     * @return - previous page data of a subclass.
     * @throws Exception - When the server fails to get a response from the API.
     */
    public List<String[]> previous() throws Exception {
        offset -= limit;
        setModelData();
        if(offset < 0) {
            offset += limit;
            return null;
        }
        return getData();
    }

    /**
     * Get the current page of the model.
     * @return - current page number.
     */
    public int getPage() {
        return (offset / limit) + 1;
    }

    /**
     * Get the total page of the model
     * @return - total page number.
     */
    public int getTotalPage() {
        return (int) Math.ceil(total / (double) limit);
    }

    protected List<String[]> ConfigureData(ArrayList<String[]> currData) {
        if(offset < 0) {
            return currData;
        }

        List<String[]> newData = new ArrayList<>();

        for (String[] currDatum : currData) {
            List<String> data = new ArrayList<>(Arrays.asList(currDatum).subList(offset,
                    Math.min(offset + limit, currData.get(0).length)));
            newData.add(data.toArray(new String[0]));
        }

        return newData;
    }
}
