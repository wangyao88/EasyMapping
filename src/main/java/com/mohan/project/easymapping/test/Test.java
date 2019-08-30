package com.mohan.project.easymapping.test;

import com.mohan.project.easymapping.MappingStructManager;
import com.mohan.project.easymapping.MappingStructStarter;
import com.mohan.project.easymapping.NoBasePackageException;
import com.mohan.project.easymapping.ScanException;

import java.util.Optional;

public class Test {

    public static void main(String[] args) throws ScanException, NoBasePackageException {
        Person person = new Person();
        person.setId("person-id");
        person.setPersonName("person-name");
        person.setAge(25);
        person.setHeight(170);
        Address address = new Address();
        address.setRoad("road");
        person.setAddress(address);

        MappingStructStarter.MappingStructStarterBuilder.newBuilder().basePackages("com.mohan.project.easymapping").build().start();

        Optional<Student> optional = MappingStructManager.mapping(Student.class, person);
        optional.ifPresent(student -> {
            System.out.println(student.getId());
            System.out.println(student.getName());
            System.out.println(student.getAge());
            System.out.println(student.getHeight());
            System.out.println(student.getDate());
            System.out.println(student.getRoadName());
        });

        System.out.println("---------------------------");
        Student student = new Student();
        student.setId("student");
        MappingStructManager.mapping(student, person);
        System.out.println(student.getId());
        System.out.println(student.getName());
        System.out.println(student.getAge());
        System.out.println(student.getHeight());
        System.out.println(student.getDate());
        System.out.println(student.getRoadName());
    }
}