package com.mohan.project.easymapping.test;

import com.mohan.project.easymapping.CustomerGenerator;
import com.mohan.project.easymapping.Mapping;
import com.mohan.project.easymapping.MappingStruct;
import com.mohan.project.easymapping.generator.AddressCustomerGenerator;
import com.mohan.project.easymapping.generator.GeneratorType;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author WangYao
 * @since 2020-09-17 09:08
 */
@Data
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

    @Mapping(source = "address")
    @CustomerGenerator(needSourceField = true, customerGenerator = AddressCustomerGenerator.class)
    private String address;
}