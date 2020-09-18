# EasyMapping

## 1.简介

​	实体属性映射器。分为反射实现的普通模式映射、智能模式映射和javassist实现的普通模式映射、智能模式映射。

## 2.提供的API

```java
/**
 * 刷新映射信息
 *
 * @param scanPath 扫描包路径 格式为 cn.com.cis
 */
void refreshConfiguration(String scanPath);

/**
 * 普通映射，需要预先根据注解解析映射信息
 *
 * @param target  需要映射的实例
 * @param sources 属性数据源，可传递多个对象
 */
void normalMapping(Object target, Object... sources);

/**
 * 普通映射，需要预先根据注解解析映射信息
 *
 * @param target  需要映射的实例
 * @param sources 属性数据源，可传递多个对象
 */
void fastNormalMapping(Object target, Object... sources);

/**
 * 普通映射，需要预先根据注解解析映射信息
 * 映射完调用回调函数
 *
 * @param callBack 回调函数
 * @param target   需要映射的实例
 * @param sources  属性数据源，可传递多个对象
 */
void normalMapping(Consumer<Object> callBack, Object target, Object... sources);

/**
 * 普通映射，需要预先根据注解解析映射信息
 * 映射完调用回调函数
 *
 * @param callBack 回调函数
 * @param target   需要映射的实例
 * @param sources  属性数据源，可传递多个对象
 */
void fastNormalMapping(Consumer<Object> callBack, Object target, Object... sources);

/**
 * 普通映射，需要预先根据注解解析映射信息
 *
 * @param targetClass 需要映射的对象类型
 * @param sources     属性数据源，可传递多个对象
 * @param <T>         对象类型泛型
 * @return 映射的对象类型的实例，由Optional包裹
 */
<T> Optional<T> normalMapping(Class<T> targetClass, Object... sources);

/**
 * 普通映射，需要预先根据注解解析映射信息
 *
 * @param targetClass 需要映射的对象类型
 * @param sources     属性数据源，可传递多个对象
 * @param <T>         对象类型泛型
 * @return 映射的对象类型的实例，由Optional包裹
 */
<T> Optional<T> fastNormalMapping(Class<T> targetClass, Object... sources);

/**
 * 普通映射，需要预先根据注解解析映射信息
 * 映射完调用回调函数
 *
 * @param callBack    回调函数
 * @param targetClass 需要映射的对象类型
 * @param sources     属性数据源，可传递多个对象
 * @param <T>         对象类型泛型
 */
<T> void normalMapping(Consumer<T> callBack, Class<T> targetClass, Object... sources);

/**
 * 普通映射，需要预先根据注解解析映射信息
 * 映射完调用回调函数
 *
 * @param callBack    回调函数
 * @param targetClass 需要映射的对象类型
 * @param sources     属性数据源，可传递多个对象
 * @param <T>         对象类型泛型
 */
<T> void fastNormalMapping(Consumer<T> callBack, Class<T> targetClass, Object... sources);

/**
 * 智能映射，不需要预先根据注解解析映射信息
 *
 * @param target  需要映射的实例
 * @param sources 属性数据源，可传递多个对象
 */
void smartMapping(Object target, Object... sources);

/**
 * 智能映射，不需要预先根据注解解析映射信息
 *
 * @param target  需要映射的实例
 * @param sources 属性数据源，可传递多个对象
 */
void fastSmartMapping(Object target, Object... sources);

/**
 * 智能映射，不需要预先根据注解解析映射信息
 * 映射完调用回调函数
 *
 * @param callBack 回调函数
 * @param target   要映射的实例
 * @param sources  属性数据源，可传递多个对象
 */
void smartMapping(Consumer<Object> callBack, Object target, Object... sources);

/**
 * 智能映射，不需要预先根据注解解析映射信息
 * 映射完调用回调函数
 *
 * @param callBack 回调函数
 * @param target   要映射的实例
 * @param sources  属性数据源，可传递多个对象
 */
void fastSmartMapping(Consumer<Object> callBack, Object target, Object... sources);

/**
 * 智能映射，不需要预先根据注解解析映射信息
 *
 * @param targetClass 需要映射的对象类型
 * @param sources     属性数据源，可传递多个对象
 * @param <T>         对象类型泛型
 * @return 映射的对象类型的实例，由Optional包裹
 */
<T> Optional<T> smartMapping(Class<T> targetClass, Object... sources);

/**
 * 智能映射，不需要预先根据注解解析映射信息
 *
 * @param targetClass 需要映射的对象类型
 * @param sources     属性数据源，可传递多个对象
 * @param <T>         对象类型泛型
 * @return 映射的对象类型的实例，由Optional包裹
 */
<T> Optional<T> fastSmartMapping(Class<T> targetClass, Object... sources);

/**
 * 智能映射，不需要预先根据注解解析映射信息
 * 映射完调用回调函数
 *
 * @param callBack    回调函数
 * @param targetClass 需要映射的对象类型
 * @param sources     属性数据源，可传递多个对象
 * @param <T>         对象类型泛型
 */
<T> void smartMapping(Consumer<T> callBack, Class<T> targetClass, Object... sources);

/**
 * 智能映射，不需要预先根据注解解析映射信息
 * 映射完调用回调函数
 *
 * @param callBack    回调函数
 * @param targetClass 需要映射的对象类型
 * @param sources     属性数据源，可传递多个对象
 * @param <T>         对象类型泛型
 */
<T> void fastSmartMapping(Consumer<T> callBack, Class<T> targetClass, Object... sources);
```

## 3.准备工作

### (1)引入依赖

```xml
<dependency>
	<groupId>com.mohan.project</groupId>
	<artifactId>easymapping</artifactId>
	<version>1.0</version>
</dependency>
```

### (2)添加注解

​	普通模式下目标实体类添加@MappingStruct，需要映射的属性添加@Mapping注解，如果不添加，默认映射所有属性。

​	另外支持属性值自定义生成器，系统页内置了一些常用的生成器。

​	提供属性值转换器

### (3)应事前需先执行以下代码，解析映射信息。

```java
EasyMappingStarter.start(basePackage);
```

## 4.使用示例

```java
package com.mohan.project.easymapping.test;

public class Address {

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

package com.mohan.project.easymapping.test;


/**
 * @author WangYao
 * @since 2020-09-17 09:08
 */
public class Person {

    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

package com.mohan.project.easymapping.test;

/**
 * @author WangYao
 * @since 2020-09-17 09:08
 */
public class Student {

    private String id;
    private String sName;
    private Double score;
    private Address address;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSName() {
        return sName;
    }

    public void setSName(String sName) {
        this.sName = sName;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}

package com.mohan.project.easymapping.test;

import com.mohan.project.easymapping.CustomerGenerator;
import com.mohan.project.easymapping.Mapping;
import com.mohan.project.easymapping.MappingStruct;
import com.mohan.project.easymapping.generator.AddressCustomerGenerator;
import com.mohan.project.easymapping.generator.GeneratorType;

import java.time.LocalDate;

/**
 * @author WangYao
 * @since 2020-09-17 09:08
 */
@MappingStruct(source = {Student.class})
public class User {

    @Mapping
    private String id;

    @Mapping(source = "sName")
    private String uName;

    @Mapping(generator = GeneratorType.UUID)
    private String password;

    @Mapping(generator = GeneratorType.NOW_LOCAL_DATE)
    private LocalDate date;

    @Mapping(source = "address.value")
    @CustomerGenerator(needSourceField = true, customerGenerator = AddressCustomerGenerator.class)
    private String address;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUName() {
        return uName;
    }

    public void setUName(String uName) {
        this.uName = uName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

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
        for (int i = 0; i < BATCH_SIZE; i++) {
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
        Address address = new Address();
        address.setValue("北京");
        Student student = new Student();
        student.setId("1");
        student.setSName("lucy");
        student.setScore(98.5d);
        student.setAddress(address);

        Optional<User> userOptional = EasyMappingManager.fastNormalMapping(User.class, student);
        Assert.assertTrue(userOptional.isPresent());
        StringTools.printObject(userOptional.get());
    }
}
```

