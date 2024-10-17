package com.baeldung.dbteardown;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@ActiveProfiles("hsql")
@DirtiesContext
public class SqlScriptIntegrationTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void givenCustomer_whenSaved_thenReturnSameCustomer() throws Exception {
        Customer customer = new Customer("John", "john@domain.com");

        Customer savedCustomer = customerRepository.save(customer);

        Assertions.assertEquals(customer, savedCustomer);
    }

    @Test
    @Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void givenExistingCustomer_whenFoundByName_thenReturnCustomer() throws Exception {
        Optional<Customer> customer = customerRepository.findByName("John");

        Assertions.assertTrue(customer.isPresent(), "Customer should be present");
        Assertions.assertEquals("John", customer.get()
            .getName(), "Customer name should match");
    }

    @Test
    void givenNonExistingCustomer_whenFoundByName_thenReturnNull() throws Exception {
        Optional<Customer> customer = customerRepository.findByName("Doe");

        Assertions.assertFalse(customer.isPresent(), "Customer should not be found");
    }
}
