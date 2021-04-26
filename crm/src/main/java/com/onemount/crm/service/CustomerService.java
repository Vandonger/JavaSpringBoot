package com.onemount.crm.service;

import java.util.List;
import java.util.Optional;

import com.onemount.crm.dto.CustomerDTO;
import com.onemount.crm.exception.CRMException;
import com.onemount.crm.mapper.CustomerMapper;
import com.onemount.crm.model.Customer;
import com.onemount.crm.repository.CustomerRepository;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import javax.validation.ConstraintViolation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

@Service
@Slf4j
public class CustomerService {
    
    @Autowired
    private CustomerRepository customerRepository;

    static final String CUSTOMER_ID_NOT_EXIST = "Customer id %d doest not exist.";

    private Validator validator;

    public CustomerService () {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }


    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Customer findById(Long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        if(customer.isPresent()) {
            return customer.get();
        } else {
            throw new CRMException("Can not find customer", new Throwable(String.format(CUSTOMER_ID_NOT_EXIST, id)),HttpStatus.NOT_FOUND);
        }
    }

    public Customer save(CustomerDTO customerDTO) {
        validateCustomer(customerDTO);
        // Customer newCustomer = new Customer();
        // newCustomer.setFullname(customerDTO.getFullname());
        // newCustomer.setEmail(customerDTO.getEmail());
        // newCustomer.setMobile(customerDTO.getMobile());
        Customer customer = CustomerMapper.INSTANCE.customerDtoToCustomer(customerDTO);
        return customerRepository.save(customer);
    }

    private void validateCustomer(CustomerDTO customerDTO) {
        Set<ConstraintViolation<CustomerDTO>> constraintViolations = validator.validate(customerDTO);
        if (!constraintViolations.isEmpty()) {
            String message = constraintViolations
            .stream().map(ConstraintViolation::getMessage)
            .collect(Collectors.joining(", "));
            log.error("Logging" + message);
          throw new CRMException("New customer violates constrains", 
          new Throwable(message), 
          HttpStatus.BAD_REQUEST);
        }
      }

    public void deleteById(Long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        if(customer.isPresent()) {
            customerRepository.delete(customer.get());
        } else {
            throw new CRMException("Cannot delete a book", 
            new Throwable(String.format(CUSTOMER_ID_NOT_EXIST, id)), 
            HttpStatus.NOT_FOUND);
        }
    }

    public Customer update(Long id, CustomerDTO customerDTO) {
        validateCustomer(customerDTO);
        Optional<Customer> customer = customerRepository.findById(id);
        if(customer.isPresent()) {
            Customer updateCustomer = new Customer(id,customerDTO.getFullname(),customerDTO.getEmail(),customerDTO.getMobile());
            customerRepository.save(updateCustomer);
            return updateCustomer;
        } else {
            throw new CRMException("Cannot update a book", 
            new Throwable(String.format(CUSTOMER_ID_NOT_EXIST, id)), 
            HttpStatus.NOT_FOUND);
        }
    }

}
