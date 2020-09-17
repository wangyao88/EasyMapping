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