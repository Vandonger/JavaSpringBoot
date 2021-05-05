package com.onemount.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import com.onemount.demo.repository.CustomerRepository;
import com.onemount.demo.model.Customer;
@DataJpaTest
@Sql({"/Customer.sql"})
public class CustomerTest {
    @Autowired
    private CustomerRepository customerRepository;

    @Test
  public void getCustomerCount(){
    assertThat(customerRepository.count()).isGreaterThan(10);    
  }

  @Test
  public void addCustomer() {
      Customer customer = new Customer("Nguyen", "Dong", "example@gmail.com", "0988777666", "Developer");
      customerRepository.save(customer);
      Optional<Customer> optional = customerRepository.findByEmail("example@gmail.com");
      if (optional.isPresent()) {
        assertThat(optional.get()).extracting("lastName").isEqualTo("Dong");
      }
  }

  @Test
  public void getCustomerById() {
    Optional<Customer> optional = customerRepository.findById(1L);
    if (optional.isPresent()) {
      assertThat(optional.get()).extracting("lastName").isEqualTo("Moff");
    }    
  }

  @Test
  public void getCustomerByEmail() {
    Optional<Customer> optional = customerRepository.findByEmail("cmoff0@java.com");
    if (optional.isPresent()) {
      assertThat(optional.get()).extracting("lastName").isEqualTo("Moff");
    }
  }

  @Test
  public void getCustomerByJob() {
    List<Customer> list = customerRepository.findByJob("Human Resources Assistant II");
    if (list.size() > 0) {
      assertThat(list.get(0)).extracting("job").isEqualTo("Human Resources Assistant II");
    }
  }

  @Test
  public void delete() {
    customerRepository.deleteById(2L);
    Optional<Customer> optional = customerRepository.findById(2L);
    assertThat(optional.isPresent()).isEqualTo(false);

  }

}
