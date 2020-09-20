// CODE_START
package com.example.webreqapp;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class Employee implements Serializable{
    @SerializedName("id")
    public Integer id;
    @SerializedName("employee_name")
    public String employee_name;
    @SerializedName("employee_salary")
    public Integer employee_salary;
    @SerializedName("employee_age")
    public Integer employee_age;
    @SerializedName("profile_image")
    public String profile_image;

}
// CODE_END