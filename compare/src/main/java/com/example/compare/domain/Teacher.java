package com.example.compare.domain;

import lombok.Data;

/**
 * @Author: liukun
 * @Description:
 * @Date: 2020/4/17.11:11
 */
@Data
public class Teacher {

    private int age;

    private String name;

    public Teacher(int age, String name) {
        this.age = age;
        this.name = name;
    }
}
