package com.mohan.project.easymapping.generator;

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