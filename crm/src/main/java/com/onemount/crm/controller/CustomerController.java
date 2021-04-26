package com.onemount.crm.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import com.onemount.crm.dto.CustomerDTO;
import com.onemount.crm.model.Customer;
import com.onemount.crm.service.CustomerService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/customer/v1")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public ResponseEntity<List<Customer>> getAll() {
        return ResponseEntity.ok(customerService.findAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Customer>findById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.findById(id));
    }

    @PostMapping(value = "/")
    public ResponseEntity<Customer> addCustomer(@RequestBody CustomerDTO customerDTO) {
        Customer newCustomer = customerService.save(customerDTO);
        try {
            return ResponseEntity.created(new URI("/api/books/" + newCustomer.getId())).body(newCustomer);
          } catch (URISyntaxException e) {
            //log.error(e.getMessage());
            return ResponseEntity.ok().body(newCustomer);      
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Long> deleteCustomerById(@PathVariable long id) {
      customerService.deleteById(id);
      return ResponseEntity.ok(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody CustomerDTO customerDTO) {
        return ResponseEntity.ok().body(customerService.update(id, customerDTO));
    }

    

}
