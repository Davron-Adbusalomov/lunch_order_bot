package org.example.DTO;

public class EmployeeDTO {
    private int id;

    private String firstName;

    private String lastName;

    private int code;

    public EmployeeDTO(int id, String firstName, String lastName, int code) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.code = code;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id=id;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getFirstName(){
        return firstName;
    }

    public void setFirstName(String firstName){
        this.firstName=firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public void setLastName(String lastName){
        this.lastName=lastName;
    }
}
