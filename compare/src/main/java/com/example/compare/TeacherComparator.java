package com.example.compare;

import com.example.compare.domain.Teacher;

import java.util.Comparator;

/**
 * @Author: liukun
 * @Description:
 * @Date: 2020/4/17.15:14
 */
public class TeacherComparator implements Comparator<Teacher> {

    @Override
    public int compare(Teacher o1, Teacher o2) {
        if (o1.getAge() > o2.getAge()) {
            return 1;
        } else if (o1.getAge() == o2.getAge()) {
            return 0;
        } else {
            return -1;
        }
    }
}
