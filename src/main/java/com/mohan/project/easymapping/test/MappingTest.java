package com.mohan.project.easymapping.test;

import com.mohan.project.easymapping.MappingStructManager;
import com.mohan.project.easymapping.MappingStructStarter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

/**
 * @author WangYao
 * @since 2020-09-17 09:13
 */
public class MappingTest {

    @Before
    public void init() {
        MappingStructStarter.start("com.mohan.project.easymapping.test");
    }

    @Test
    public void simpleTest() {
        Student student = new Student();
        student.setId("1");
        student.setSName("lucy");
        student.setScore(98.5d);
        student.setAddress("北京");

        Optional<User> userOptional = MappingStructManager.normalMapping(User.class, student);
        Assert.assertTrue(userOptional.isPresent());
        System.out.println(userOptional.get());
    }
}