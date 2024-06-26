package org.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Employee {
    private int id;

    private String firstName;

    private String lastName;

    private int code;

    public Employee(int id, String firstName, String lastName, int code) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.code = code;
    }

    public Employee() {
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
