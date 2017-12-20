package com.example.myapplication.bean;

import java.util.ArrayList;


public class BeanBrandList {
    private int error_code;
    private String message;
    private ArrayList<BeanBrand> brand_list;

    public int getErrorCode() {
        return error_code;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<BeanBrand> getBrandList() {
        return brand_list;
    }
}
