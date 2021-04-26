package com.onemount.crm;

import com.onemount.crm.RESTClient.*;
import com.onemount.crm.dto.CustomerDTO;
import com.onemount.crm.model.Customer;
import com.onemount.crm.exception.*;
import org.junit.jupiter.api.Test;
import feign.*;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import lombok.extern.slf4j.Slf4j;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.http.HttpStatus;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.annotation.Backoff;

@TestInstance(Lifecycle.PER_CLASS)
@Slf4j
class RestTest {
	private CustomerRequest client;
	private Faker faker;
	@BeforeAll
	public void buildFeignClient() {
		final Decoder decoder = new GsonDecoder();
		final Encoder encoder = new GsonEncoder();
		client = Feign.builder()
				// .retryer(new Retryer.Default(100, 300, 5))
				.client(new OkHttpClient()).decoder(decoder).encoder(encoder).logLevel(Logger.Level.BASIC)
				.errorDecoder(new APIErrorDecoder(decoder)).target(CustomerRequest.class, "http://localhost:8080/api/customer/v1/");

		faker	= new Faker();
	}

	@Test
	@DisplayName(" 1. GET /api/customer/v1/")
	@Order(1)
	void findAllCustomer() {
		List<Customer> customers = client.list();
		assertThat(customers.size()).isGreaterThan(1);
	}

	@Test
	@DisplayName(" 2. GET /api/customer/v1/{id}")
	@Order(2)
	void findCustomerById() {
		Customer customer = client.get(1L);
		assertThat(customer).isNotNull();
	}

	@Test
	@DisplayName(" 3. POST /api/customer/v1/")
	@Order(2)
	void addCustomer() {
		String fullname = faker.name().fullName();
		String email = faker.internet().emailAddress();
        String mobile = faker.phoneNumber().phoneNumber();

		CustomerDTO customerDTO = new CustomerDTO(fullname,email,mobile);
		Customer createdCustomer = client.post(customerDTO);
		assertThat(createdCustomer.getId()).isPositive();
	}

	@Test
	@DisplayName(" 4. PUT /api/customer/{id}")
	@Order(4)
	void updateCustomer() {
		String fullname = faker.name().fullName();
		String email = faker.internet().emailAddress();
        String mobile = faker.phoneNumber().phoneNumber();
		CustomerDTO customerDTO = new CustomerDTO(fullname,email,mobile);
		Customer updatedCustomer = client.put(3, customerDTO);
		assertThat(updatedCustomer.getId()).isEqualTo(3);
		assertThat(updatedCustomer.getFullname()).isEqualTo(fullname);
		assertThat(updatedCustomer.getEmail()).isEqualTo(email);
        assertThat(updatedCustomer.getMobile()).isEqualTo(mobile);
	}

	@Test
	@DisplayName(" 5. DELETE /api/customer/v1/{id}")
	@Order(5)
	void deleteCustomerById() {
		long deletedId = client.delete(4L);
		assertThat(deletedId).isEqualTo(4L);
	}

	@Test
	@DisplayName(" 6. DELETE Not Found Customer")
	@Order(6)
	void deleteNotFoundCustomer() {
		try {
			client.delete(100L);
		} catch (APIException e) {
			assertThat(e.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
			assertThat(e.getMessage()).isEqualTo("CRMException : Cannot delete a customer");
		}

	}

	@Test
	@DisplayName(" 7. Update Not Found Customer")
	@Order(7)
	void UpdateNotFoundCustomer() {
		try {
			String fullname = faker.name().fullName();
            String email = faker.internet().emailAddress();
            String mobile = faker.phoneNumber().phoneNumber();
            CustomerDTO customerDTO = new CustomerDTO(fullname,email,mobile);
			client.put(100L, customerDTO);
		} catch (APIException e) {
			assertThat(e.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
			assertThat(e.getMessage()).isEqualTo("CRMException : Cannot update a customer");
		}
	}

	@Test
	@DisplayName(" 8. Create 20 customer")
	@Order(8)
	void addNew20Customer() {
		List<Customer> customers = client.list();
		long numberOfCustomerBeforeAdd = customers.size();
		for (int i = 0; i < 20; i++) {
			String fullname = faker.name().fullName();
            String email = faker.internet().emailAddress();
            String mobile = faker.phoneNumber().phoneNumber();
            CustomerDTO customerDTO = new CustomerDTO(fullname,email,mobile);
			client.post(customerDTO);
		}
		List<Customer> customersAfter = client.list();
		assertThat(customersAfter.size() - numberOfCustomerBeforeAdd).isEqualTo(20);
	}

	@Test
	@DisplayName(" 9. GET /api/customer/v1/slow")
	@Order(9)
	void getSlowCustomer() {
		try {
			callSlowAPI();
		} catch (APIException ex) {
			assertThat(ex.getHttpStatus()).isEqualTo(HttpStatus.REQUEST_TIMEOUT);
			assertThat(ex.getMessage()).isEqualTo("CRMException : Timeout service");
		}
	}

	@Test
	@DisplayName(" 10. Spring Retryable")
	@Order(10)
	void getSlowCustomersSpringRetryable() {

		RetryTemplate template = RetryTemplate.builder().maxAttempts(4).fixedBackoff(1000).build();

		try {
			List<Customer> customers = template.execute(new RetryCallback<List<Customer>, Exception>() {
				@Override
				public List<Customer> doWithRetry(RetryContext context) {
					log.info("Retry attempt " + context.getRetryCount());
					try {
						return client.listSlow();
					} catch (APIException apiEx) {
						throw new RuntimeException("Failed to execute after " + (context.getRetryCount() + 1));
					}
				}
			});

			assertThat(customers.size()).isGreaterThan(1);
			log.info("Call slow API success with " + customers.size() + " records returned");

		} catch (Exception ex) {			
			log.error(ex.getMessage());
		}

	}


	@Retryable(value = RuntimeException.class, maxAttempts = 6, backoff = @Backoff(delay = 1000))
	private void callSlowAPI() {
		client.listSlow();
	}

	@Test
	@DisplayName("10. POST /api/books invalid book")
	@Order(10)
	void postInvalidBook() {
		try {
			CustomerDTO customerDTO = new CustomerDTO("Nguyen","vann@","000999");
			client.post(customerDTO);
		} catch (APIException e) {
			assertThat(e.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
			assertThat(e.getMessage()).isEqualTo("CRMException : New book violates constrains");
		}
	}
}
