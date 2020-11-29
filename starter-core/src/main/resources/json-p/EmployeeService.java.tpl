package [# th:text="${packageName}"/].jsonp;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.stream.JsonParser;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class EmployeeService {

    private List<JsonObject> inMemoryJSONStorage = new ArrayList<JsonObject>();

    public void addEmployee(int age, String firstName, String lastName, boolean worksFromHome){
        JsonObject employee =
                Json.createObjectBuilder()
                    .add(
                            "age", age
                    ).add(
                            "firstName", firstName
                    ).add(
                            "lastName", lastName
                    ).add(
                            "worksFromHome", worksFromHome
                    ).build();

        inMemoryJSONStorage.add(employee);
    }

    public void addEmployee(String employeeAsJSONString){
        JsonReader jsonReader = Json.createReader(new StringReader(employeeAsJSONString));

        JsonObject jsonObject = jsonReader.readObject();

        inMemoryJSONStorage.add(jsonObject);
    }

    public void parseVeryLargeEmployeeJSON(String employeeAsJSONString){
        JsonParser jsonParser = Json.createParser(new StringReader(employeeAsJSONString));

        while(jsonParser.hasNext()){
            JsonParser.Event event = jsonParser.next();
            switch (event){
                case KEY_NAME:
                    String key = jsonParser.getString();
                    System.out.println(key);
                    break;
                case VALUE_NUMBER:
                    int value = jsonParser.getInt();
                    System.out.println(value);
                    break;
            }
        }
    }
}
