package com.mohan.project.easymapping.test;

import com.mohan.project.easymapping.EasyMappingManager;
import com.mohan.project.easymapping.EasyMappingStarter;
import com.mohan.project.easymapping.MappingParameter;
import com.mohan.project.easymapping.convert.ConvertType;
import com.mohan.project.easymapping.convert.Converts;
import com.mohan.project.easymapping.generator.Generator;
import com.mohan.project.easymapping.generator.GeneratorType;
import com.mohan.project.easymapping.generator.Generators;
import com.mohan.project.easytools.common.StringTools;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author WangYao
 * @since 2020-09-17 09:13
 */
public class MappingTest {

    public static final int BATCH_SIZE = 3000000;

    @Before
    public void init() {
        EasyMappingStarter.start("com.mohan.project.easymapping.test");
    }

    @Test
    public void batchNormalTest() {
        CountDownLatch countDownLatch = new CountDownLatch(BATCH_SIZE);
        long start = System.currentTimeMillis();
        ExecutorService executorService = Executors.newFixedThreadPool(8);
        Address address = new Address();
        address.setValue("北京");
        for (int i = 0; i < BATCH_SIZE; i++) {
            executorService.submit(() -> {
                Student student = new Student();
                student.setId("1");
                student.setSName("lucy");
                student.setScore(98.5d);
                student.setAddress(address);

                Optional<User> userOptional = EasyMappingManager.normalMapping(User.class, student);
                countDownLatch.countDown();
                if(userOptional.isPresent()) {
                    if(StringTools.isBlank(userOptional.get().getAddress())) {
                        System.out.println("-----");
                    }
                }
            });

        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(System.currentTimeMillis() - start);
    }

    @Test
    public void batchFastNormalTest() {
        CountDownLatch countDownLatch = new CountDownLatch(BATCH_SIZE);
        long start = System.currentTimeMillis();
        ExecutorService executorService = Executors.newFixedThreadPool(8);
        Address address = new Address();
        address.setValue("北京");
        for (int i = 0; i < BATCH_SIZE; i++) {
            executorService.submit(() -> {
                Student student = new Student();
                student.setId("1");
                student.setSName("lucy");
                student.setScore(98.5d);
                student.setAddress(address);

                Optional<User> userOptional = EasyMappingManager.fastNormalMapping(User.class, student);
                countDownLatch.countDown();
                if(userOptional.isPresent()) {
                    if(StringTools.isBlank(userOptional.get().getAddress())) {
                        System.out.println("-----");
                    }
                }
            });

        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(System.currentTimeMillis() - start);
    }

    @Test
    public void batchSmartMappingTest() {
        CountDownLatch countDownLatch = new CountDownLatch(BATCH_SIZE);
        long start = System.currentTimeMillis();
        ExecutorService executorService = Executors.newFixedThreadPool(8);
        Address address = new Address();
        address.setValue("北京");
        for (int i = 0; i < BATCH_SIZE; i++) {
            executorService.submit(() -> {
                Student student = new Student();
                student.setId("1");
                student.setSName("lucy");
                student.setScore(98.5d);
                student.setAddress(address);

                Optional<User> userOptional = EasyMappingManager.smartMapping(User.class, student);
                countDownLatch.countDown();
            });

        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(System.currentTimeMillis() - start);
    }

    @Test
    public void batchFastSmartMappingTest() {
        CountDownLatch countDownLatch = new CountDownLatch(BATCH_SIZE);
        long start = System.currentTimeMillis();
        ExecutorService executorService = Executors.newFixedThreadPool(8);
        Address address = new Address();
        address.setValue("北京");
        for (int i = 0; i < BATCH_SIZE; i++)



        {
            executorService.submit(() -> {
                Student student = new Student();
                student.setId("1");
                student.setSName("lucy");
                student.setScore(98.5d);
                student.setAddress(address);

                Optional<User> userOptional = EasyMappingManager.fastSmartMapping(User.class, student);
                countDownLatch.countDown();
            });

        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(System.currentTimeMillis() - start);
    }

    @Test
    public void onceFastSmartMappingTest() {
        Address address = new Address();
        address.setValue("北京");
        Student student = new Student();
        student.setId("1");
        student.setSName("lucy");
        student.setScore(98.5d);
        student.setAddress(address);
        Optional<User> userOptional = EasyMappingManager.fastSmartMapping(User.class, student);
        userOptional.ifPresent(StringTools::printObject);
    }

    @Test
    public void fastNormalMappingTest() {
        Address address1 = new Address();
        address1.setValue("北京111");
        Student student1 = new Student();
        student1.setId("id1111");
        student1.setSex("sex111");
        student1.setSName("lucy111");
        student1.setScore(98.5d);
        student1.setAddress(address1);

        Address address2 = new Address();
        address2.setValue("北京222");
        Student student2 = new Student();
        student2.setId("id222");
        student2.setSex("sex222");
        student2.setSName("lucy222");
        student2.setScore(98.5d);
        student2.setAddress(address2);

        Person person = new Person();
        person.setId("personId");
        person.setSex("person sex");

        Optional<User> userOptional = EasyMappingManager.fastNormalMapping(User.class, person, student1);
        Assert.assertTrue(userOptional.isPresent());
        StringTools.printObject(userOptional.get());
    }
}