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
@MappingStruct(source = {Student.class, Person.class})
public class User {

    /**
     * 未指定index 会使用@MappingStruct(source = {Student.class, Person.class})
     * source数组中最后一个匹配的元素的属性进行设值
     */
    @Mapping
    private String id;

    /**
     * 指定了index 用Student类型的第一个实例的sex属性进行设值
     */
    @Mapping(index = 0)
    private String sex;

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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
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