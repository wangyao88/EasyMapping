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
        student1.setSex("sex222");
        student2.setSName("lucy222");
        student2.setScore(98.5d);
        student2.setAddress(address2);

        Person person = new Person();
        person.setId("personId");
        person.setSex("person sex");

        Optional<User> userOptional = EasyMappingManager.fastNormalMapping(User.class, student1, student2, person);
        Assert.assertTrue(userOptional.isPresent());
        StringTools.printObject(userOptional.get());
    }



    public void doSet(Object target, List sources, List mappingParameters) {
        com.mohan.project.easymapping.test.User realTarget = (com.mohan.project.easymapping.test.User)target;

        int sourceSize = sources.size();

        MappingParameter mappingParameter0 = (com.mohan.project.easymapping.MappingParameter)mappingParameters.get(0);
        Object sourceValue0 = null;
        ConvertType convertType0 = mappingParameter0.getConvertType();
        String sourceClassName0 = mappingParameter0.getSourceClassName();
        for (int i = 0; i < sourceSize; i++) {
            Object source = sources.get(i);
            if (!source.getClass().getName().equals(sourceClassName0)) {
                continue;
            }
            try {
                sourceValue0 = ((com.mohan.project.easymapping.test.Student)sources.get(0)).getId();
            }catch (Exception e) {
                sourceValue0 = null;
            }
            try {
                sourceValue0 = ((com.mohan.project.easymapping.test.Student)sources.get(1)).getId();
            }catch (Exception e) {
                sourceValue0 = null;
            }
            if (ConvertType.NONE != convertType0) {
                sourceValue0 = Converts.convert(convertType0, sourceValue0);
            }
        }
        realTarget.setId((java.lang.String)sourceValue0);

        MappingParameter mappingParameter1 = (com.mohan.project.easymapping.MappingParameter)mappingParameters.get(1);
        Object sourceValue1 = null;
        ConvertType convertType1 = mappingParameter1.getConvertType();
        String sourceClassName1 = mappingParameter1.getSourceClassName();
        for (int i = 0; i < sourceSize; i++) {
            Object source = sources.get(i);
            if (!source.getClass().getName().equals(sourceClassName1)) {
                continue;
            }
            try {
                sourceValue1 = ((com.mohan.project.easymapping.test.Student)sources.get(0)).getSName();
            }catch (Exception e) {
                sourceValue1 = null;
            }
            try {
                sourceValue1 = ((com.mohan.project.easymapping.test.Student)sources.get(1)).getSName();
            }catch (Exception e) {
                sourceValue1 = null;
            }
            if (ConvertType.NONE != convertType1) {
                sourceValue1 = Converts.convert(convertType1, sourceValue1);
            }
        }
        realTarget.setUName((java.lang.String)sourceValue1);

        MappingParameter mappingParameter2 = (com.mohan.project.easymapping.MappingParameter)mappingParameters.get(2);
        GeneratorType generatorType2 = mappingParameter2.getGeneratorType();
        Object generateValue2 = Generators.generator(generatorType2);
        realTarget.setPassword((java.lang.String)generateValue2);

        MappingParameter mappingParameter3 = (com.mohan.project.easymapping.MappingParameter)mappingParameters.get(3);
        GeneratorType generatorType3 = mappingParameter3.getGeneratorType();
        Object generateValue3 = Generators.generator(generatorType3);
        realTarget.setDate((java.time.LocalDate)generateValue3);

        MappingParameter mappingParameter4 = (com.mohan.project.easymapping.MappingParameter)mappingParameters.get(4);
        Object sourceValue4 = null;
        ConvertType convertType4 = mappingParameter4.getConvertType();

        if (ConvertType.NONE != convertType4) {
            sourceValue4 = Converts.convert(convertType4, sourceValue4);
        }
        Generator generator4 = mappingParameter4.getGenerator();
        Object customerGenerateValue4 = generator4.doGenerate(sourceValue4);
        realTarget.setAddress((java.lang.String)customerGenerateValue4);

        MappingParameter mappingParameter5 = (com.mohan.project.easymapping.MappingParameter)mappingParameters.get(5);
        Object sourceValue5 = null;
        ConvertType convertType5 = mappingParameter5.getConvertType();
        String sourceClassName5 = mappingParameter5.getSourceClassName();
        for (int i = 0; i < sourceSize; i++) {
            Object source = sources.get(i);
            if (!source.getClass().getName().equals(sourceClassName5)) {
                continue;
            }
            try {
                sourceValue5 = ((com.mohan.project.easymapping.test.Person)sources.get(2)).getId();
            }catch (Exception e) {
                sourceValue5 = null;
            }
            if (ConvertType.NONE != convertType5) {
                sourceValue5 = Converts.convert(convertType5, sourceValue5);
            }
        }
        realTarget.setId((java.lang.String)sourceValue5);

        MappingParameter mappingParameter6 = (com.mohan.project.easymapping.MappingParameter)mappingParameters.get(6);
        GeneratorType generatorType6 = mappingParameter6.getGeneratorType();
        Object generateValue6 = Generators.generator(generatorType6);
        realTarget.setPassword((java.lang.String)generateValue6);

        MappingParameter mappingParameter7 = (com.mohan.project.easymapping.MappingParameter)mappingParameters.get(7);
        GeneratorType generatorType7 = mappingParameter7.getGeneratorType();
        Object generateValue7 = Generators.generator(generatorType7);
        realTarget.setDate((java.time.LocalDate)generateValue7);

        MappingParameter mappingParameter8 = (com.mohan.project.easymapping.MappingParameter)mappingParameters.get(8);
        Object sourceValue8 = null;
        ConvertType convertType8 = mappingParameter8.getConvertType();

        if (ConvertType.NONE != convertType8) {
            sourceValue8 = Converts.convert(convertType8, sourceValue8);
        }
        Generator generator8 = mappingParameter8.getGenerator();
        Object customerGenerateValue8 = generator8.doGenerate(sourceValue8);
        realTarget.setAddress((java.lang.String)customerGenerateValue8);

    }



}