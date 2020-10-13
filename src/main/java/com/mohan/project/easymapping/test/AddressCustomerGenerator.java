package com.mohan.project.easymapping.test;

import com.mohan.project.easymapping.generator.Generator;

/**
 * @author mohan
 * @since 2020-09-17 09:29
 */
public class AddressCustomerGenerator implements Generator {

    @Override
    public Object doGenerate(Object sourceValue) {
        return "user " + sourceValue;
    }
}