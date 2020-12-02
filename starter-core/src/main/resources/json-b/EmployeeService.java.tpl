package [# th:text="${packageName}"/].jsonb;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EmployeeService {

    public String getEmployeeAsJSON(int age, Date dateOfAdmission, String firstName, String lastName, boolean worksFromHome){

        Employee employee = new Employee();

        employee.setAge(age);
        employee.setDateOfAdmission(dateOfAdmission);
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setWorksFromHome(worksFromHome);

        Jsonb jsonb = JsonbBuilder.create();
        return jsonb.toJson(employee);
    }

    public String getEmployeeAsJSONWithNullValues(int age, String firstName, String lastName, boolean worksFromHome) {
        Employee employee = new Employee();

        employee.setAge(age);
        employee.setDateOfAdmission(null);
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setWorksFromHome(worksFromHome);

        JsonbConfig nillableConfig = new JsonbConfig().withNullValues(true);

        Jsonb jsonb = JsonbBuilder.create(nillableConfig);

        return jsonb.toJson(employee);

    }

    public List getEmployeeListWithCollection(String employeeListAsJSON){
        Jsonb jsonb = JsonbBuilder.create();
        return jsonb.fromJson(employeeListAsJSON, ArrayList.class);
    }

    public List<Employee> getEmployeeListWithGenericCollection(String employeeListAsJSON){
        Jsonb jsonb = JsonbBuilder.create();
        return jsonb.fromJson(employeeListAsJSON, new ArrayList<Employee>(){}.getClass().getGenericSuperclass());
    }

    public Employee getEmployeeObject(String employeeAsJSON){
        Jsonb jsonb = JsonbBuilder.create();

        return jsonb.fromJson(employeeAsJSON, Employee.class);
    }

}