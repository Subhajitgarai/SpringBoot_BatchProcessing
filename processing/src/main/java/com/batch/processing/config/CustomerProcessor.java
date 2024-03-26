package com.batch.processing.config;

import com.batch.processing.entity.Customer;
import org.springframework.batch.item.ItemProcessor;

public class CustomerProcessor implements ItemProcessor<Customer,Customer> {
    @Override
    public Customer process(Customer customer) throws Exception {
        if (customer.getCountry().equalsIgnoreCase("China")){
            return customer;
        }
        else {
            return  null;
        }
    }
}
