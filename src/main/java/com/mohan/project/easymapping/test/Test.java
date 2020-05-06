package com.mohan.project.easymapping.test;

import com.mohan.project.easymapping.MappingStructManager;
import com.mohan.project.easymapping.MappingStructStarter;
import com.mohan.project.easymapping.ScanException;
import com.mohan.project.easytools.log.LogTools;

import java.util.Optional;

/**
 * 实体属性映射器测试类
 * @author mohan
 * @date 2019-08-30 09:24:26
 */
public class Test {

    public static void main(String[] args) throws ScanException {
        Person person = new Person();
        person.setId("person-id");
        person.setPersonName("person-name");
        person.setAge(25);
        person.setHeight(170);
        Address address = new Address();
        address.setRoad("road");
        person.setAddress(address);

        MappingStructStarter.MappingStructStarterBuilder.newBuilder().enableLog().build().start();

//        Optional<Student> optional = MappingStructManager.mapping(Student.class, person);
//        optional.ifPresent(student -> {
//            System.out.println(student.getId());
//            System.out.println(student.getName());
//            System.out.println(student.getAge());
//            System.out.println(student.getHeight());
//            System.out.println(student.getDate());
//            System.out.println(student.getRoadName());
//        });

        LogTools.printDividingLine(85);
        Student student = new Student();
        student.setId("student");
        MappingStructManager.mapping(student, person);
        System.out.println(student);
    }
}