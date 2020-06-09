package com.example.compare;

import com.example.compare.domain.Student;
import com.example.compare.domain.Teacher;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author: liukun
 * @Description:
 * @Date: 2020/4/17.11:19
 */
public class Compare {

    @Test
    public void testComparable() {
        Student s1 = new Student(30, "kelvin");
        Student s2 = new Student(28, "mirofy");
        Student s3 = new Student(40, "steven");
        List<Student> studentList = new ArrayList<>();
        studentList.add(s1);
        studentList.add(s2);
        studentList.add(s3);
        Collections.sort(studentList);
        for (Student tmp : studentList) {
            System.out.println(tmp);
        }
    }

    @Test
    public void testComparator() {
        Teacher s1 = new Teacher(30, "kelvin");
        Teacher s2 = new Teacher(28, "mirofy");
        Teacher s3 = new Teacher(40, "steven");
        List<Teacher> teacherList = new ArrayList<>();
        teacherList.add(s1);
        teacherList.add(s2);
        teacherList.add(s3);
        Collections.sort(teacherList, new TeacherComparator());
        for (Teacher tmp : teacherList) {
            System.out.println(tmp);
        }
    }
}
