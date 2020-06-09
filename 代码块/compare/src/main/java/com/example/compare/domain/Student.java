package com.example.compare.domain;

import lombok.Data;

/**
 * @Author: liukun
 * @Description:
 * @Date: 2020/4/17.11:11
 */
@Data
public class Student implements Comparable<Student>{

    private int age;

    private String name;

    public Student(int age, String name) {
        this.age = age;
        this.name = name;
    }

    @Override
    public int compareTo(Student o) {
        if (this.age > o.getAge()) {
            return 1;
        } else if (this.age == o.getAge()) {
            return 0;
        } else {
            return -1;
        }
    }
}
