package com.batch.processing.config;

import com.batch.processing.entity.Customer;
import com.batch.processing.entity.CustomerDetails;
import org.springframework.batch.item.ItemProcessor;

public class CustomerProcessor implements ItemProcessor<Customer, CustomerDetails> {

    @Override
    public CustomerDetails process(Customer item) throws Exception {
        CustomerDetails customerDetails=new CustomerDetails();
        customerDetails.setId(item.getId());
        customerDetails.setDob(item.getDob());
        customerDetails.setEmail(item.getEmail());
        customerDetails.setCountry(item.getCountry());
        customerDetails.setGender(item.getGender());
        customerDetails.setContactNo(item.getContactNo());
        customerDetails.setFirstName(item.getFirstName());
        customerDetails.setLastName(item.getLastName());
        String fullName=item.getFirstName()+" "+item.getLastName();
        customerDetails.setFullName(fullName);
        return customerDetails;
    }
}
