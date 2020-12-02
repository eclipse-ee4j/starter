package [# th:text="${packageName}"/].jsonb;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.json.bind.annotation.JsonbProperty;
import java.util.Date;

public class Employee {

    @JsonbProperty("first-name")
    private String firstName;

    @JsonbProperty(nillable = true, value = "last-name")
    private String lastName;

    private int age;

    private boolean worksFromHome;

    @JsonbDateFormat("dd/MM/yyyy")
    private Date dateOfAdmission;


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isWorksFromHome() {
        return worksFromHome;
    }

    public void setWorksFromHome(boolean worksFromHome) {
        this.worksFromHome = worksFromHome;
    }

    public Date getDateOfAdmission() {
        return dateOfAdmission;
    }

    public void setDateOfAdmission(Date dateOfAdmission) {
        this.dateOfAdmission = dateOfAdmission;
    }
}
