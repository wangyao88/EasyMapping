package com.mohan.project.easymapping.test;

import com.mohan.project.easymapping.Mapping;
import com.mohan.project.easymapping.MappingStruct;
import com.mohan.project.easymapping.convert.ConvertType;
import com.mohan.project.easymapping.generator.GeneratorType;
import lombok.Data;

import java.util.Date;

@Data
@MappingStruct(source = Person.class)
public class Student {

    @Mapping(generator = GeneratorType.UUID)
    private String id;

    @Mapping(source = "personName")
    private String name;

    @Mapping(convert = ConvertType.String)
    private String age;

    @Mapping(generator = GeneratorType.NOW)
    private Date date;

    @Mapping(source = "address.road")
    private String roadName;

    @Mapping
    private Integer height;
}