package advisor.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

public class JsonAnalyzer {

    /**
     * Parse an element from a given json
     * @param json - String formatted json to be parsed.
     * @param key - element that you want to get.
     * @return - String element value based on the key.
     */
    public static String getElementString(String json, String key) {
        JsonObject object = JsonParser.parseString(json).getAsJsonObject();
        return object.get(key).getAsString();
    }

    /**
     * Gets data/s from a simple ot complex json.
     * @param json - String formatted json to be parsed.
     * @param path - JSON path which leads to the key.
     * @param key - element that you want to get(Can be a path).
     * @param type - integer variable specifying on which element type you want to obtain.
     *             0 - Gets a simple element value.
     *             1 - Gets an array of string and combines it.(to maintain same number of elements)
     *             2 - Gets a JSON object string.
     *             3 - Gets an element in an JSON object array.
     * @return - Requested element value in string format.
     */
    public static String[] getJsonInfo(String json, String path, String key, int type) {
        List<String> info = new ArrayList<>();
        JsonObject object = JsonParser.parseString(json).getAsJsonObject();
        jsonPathParser(info, object, path, key, type);
        return info.toArray(String[]::new);
    }

    /**
     * Recursively goes to the current path until given key is found. Then parse that value.
     * @param info - gathers all element values.
     * @param currObject - current object being parsed.
     * @param path - JSON path which leads to the key.
     * @param key - element that you want to get(Can be a path).
     * @param type -integer variable specifying on which element type you want to obtain.
     *             0 - Gets a simple element value.
     *             1 - Gets an array of string and combines it.(to maintain same number of elements)
     *             2 - Gets a JSON object string.
     *             3 - Gets an element in an JSON object array.
     */
    private static void jsonPathParser(List<String> info, JsonObject currObject, String path, String key, int type) {
        List<String> paths = new ArrayList<>(List.of(path.split("(-)|(/)")));
        String currPath = paths.get(0);
        // Stops recursion.
        if(path.length() == 0) {
            // Element type.
            if(type == 0) {
                info.add(currObject.get(key).getAsString());
                // Array type.
            } else if(type == 1) {
                List<String> strings = new ArrayList<>();
                for (JsonElement jsonElement : currObject.getAsJsonArray(key)) {
                    strings.add(jsonElement.getAsString());
                }
                info.add(String.join(", ", strings));
                // Object type.
            } else if(type == 2) {
                info.add(currObject.get(key).getAsString());
                // Elements of an array of object type.
            } else {
                List<String> childInfo = new ArrayList<>();
                String[] childPaths = key.split("(-)|(/)");
                String childPath = key.substring(0, key.length() - childPaths[childPaths.length - 1].length());
                jsonPathParser(childInfo, currObject, childPath,
                        childPaths[childPaths.length - 1], 0);
                info.add(String.join(", ", childInfo));
            }
        } else {
            // Updates the path by removing the parent path.
            paths.remove(currPath);
            // Checks if the path is supposed to be an object or an array.
            String predicate = path.substring(currPath.length(), currPath.length() + 1);

            // If the path is an object.
            if(predicate.equals("-")) {
                for (JsonElement jsonElement : currObject.getAsJsonArray(currPath)) {
                    jsonPathParser(info, jsonElement.getAsJsonObject(),
                            path.substring(currPath.length() + 1), key, type);
                }
                // If the path is an array.
            } else {
                jsonPathParser(info, currObject.getAsJsonObject(currPath),
                        path.substring(currPath.length() + 1), key, type);
            }
        }
    }
}
