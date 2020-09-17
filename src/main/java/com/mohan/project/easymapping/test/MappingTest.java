package com.mohan.project.easymapping.test;

import com.mohan.project.easymapping.EasyMappingManager;
import com.mohan.project.easymapping.EasyMappingStarter;
import com.mohan.project.easytools.common.StringTools;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    public void simpleNormalTest() {
        CountDownLatch countDownLatch = new CountDownLatch(3000000);
        long start = System.currentTimeMillis();
        ExecutorService executorService = Executors.newFixedThreadPool(8);
        for (int i = 0; i < 3000000; i++) {
            executorService.submit(() -> {
                Student student = new Student();
                student.setId("1");
                student.setSName("lucy");
                student.setScore(98.5d);
                student.setAddress("北京");

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
    public void fastSmartMappingTest() {
        CountDownLatch countDownLatch = new CountDownLatch(3000000);
        long start = System.currentTimeMillis();
        ExecutorService executorService = Executors.newFixedThreadPool(8);
        for (int i = 0; i < 3000000; i++) {
            executorService.submit(() -> {
                Student student = new Student();
                student.setId("1");
                student.setSName("lucy");
                student.setScore(98.5d);
                student.setAddress("北京");

                Optional<User> userOptional = EasyMappingManager.fastSmartMapping(User.class, student);
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
    public void fastNormalMappingTest() {
        Student student = new Student();
        student.setId("1");
        student.setSName("lucy");
        student.setScore(98.5d);
        student.setAddress("北京");

        Object id = "";

        student.setId((String)id);

        Optional<User> userOptional = EasyMappingManager.fastNormalMapping(User.class, student);
        Assert.assertTrue(userOptional.isPresent());
        System.out.println(userOptional.get());

    }
}