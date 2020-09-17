package com.mohan.project.easymapping.test;

import com.mohan.project.easymapping.EasyMappingManager;
import com.mohan.project.easymapping.EasyMappingStarter;
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
        EasyMappingStarter.start("com.mohan.project.easymapping.test");
    }

    @Test
    public void simpleTest() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            Student student = new Student();
            student.setId("1");
            student.setSName("lucy");
            student.setScore(98.5d);
            student.setAddress("北京");

            EasyMappingManager.normalMapping(User.class, student);
        }
        System.out.println(System.currentTimeMillis() - start);
    }

    @Test
    public void fastSmartMappingTest() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            Student student = new Student();
            student.setId("1");
            student.setSName("lucy");
            student.setScore(98.5d);
            student.setAddress("北京");

            EasyMappingManager.fastSmartMapping(User.class, student);
        }
        System.out.println(System.currentTimeMillis() - start);

    }
}