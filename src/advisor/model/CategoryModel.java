package advisor.model;

import advisor.utils.JsonAnalyzer;

import java.util.ArrayList;
import java.util.List;

public class CategoryModel extends ApiModel{

    /**
     * Sets the category model configuration.
     * @param limit - number of entries in a page.
     * @throws Exception - When the server fails to get a response from the API.
     */
    public CategoryModel(int limit) throws Exception {
        super(limit, "v1/browse/categories", "categories/");
    }

    /**
     * returns the all data coming from the category model.
     *
     * @return names of categories requested from the API.
     */
    @Override
    public List<String[]> getData() {
        ArrayList<String[]> categoryData = new ArrayList<>();
        categoryData.add(JsonAnalyzer.getJsonInfo(json, "categories/items-", "name", 0));
        return ConfigureData(categoryData);
    }

    /**
     * Get the ids of categories requested from the API.
     * @return ids of categories requested from the API.
     */
    public String[] getIds() {
        return JsonAnalyzer.getJsonInfo(json, "categories/items-", "id", 0);
    }
}
