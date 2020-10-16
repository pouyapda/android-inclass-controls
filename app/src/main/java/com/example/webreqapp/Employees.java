// CODE_START
package com.example.webreqapp;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Employees implements Serializable{
    @SerializedName("data")
    public List<Employee> employees;

}
// CODE_END