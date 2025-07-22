package vn.agest.selenide.common.utilities.other;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Utilities {

    public static String getProjectPath() {
        return System.getProperty("user.dir");
    }

    public static Object[][] getData(String testName, String dataFilePath) {

        Object[][] data = new Object[0][1];

        //Read json file data using Gson library
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(dataFilePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        JsonElement jsonElement = JsonParser.parseReader(br);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        //Check for the test name in the json file
        boolean blnTCExist = jsonObject.has(testName);
        if (!blnTCExist) {
            Log.error(testName + " is not present in the data.json file - " + dataFilePath);
            return data;
        }

        //Get test data for the specific test case
        JsonArray jsonArray = jsonObject.getAsJsonArray(testName);
        data = jsonArrayToObjectArray(jsonArray);
        return data;
    }

    public static Object[][] jsonArrayToObjectArray(JsonArray jsonArray) {

        Object[][] data = new Object[0][1];
        int index = 0;
        Gson gson = new Gson();

        if (jsonArray.size() > 0) {
            data = new Object[jsonArray.size()][1];
            for (JsonElement obj : jsonArray) {
                HashMap<String, String> hashMap = new LinkedHashMap<>();
                data[index][0] = gson.fromJson(obj, hashMap.getClass());
                index++;
            }
        }
        return data;
    }
}
