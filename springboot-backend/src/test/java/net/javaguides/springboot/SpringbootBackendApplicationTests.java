package net.javaguides.springboot;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;

import com.mindtree.employeemanagerapp.model.Employee;
import com.mindtree.employeemanagerapp.repository.EmployeeRepository;
import com.mindtree.employeemanagerapp.service.EmployeeService;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;


@SpringBootTest
@ContextConfiguration(classes=com.mindtree.employeemanagerapp.SpringbootBackendApplicationTests.class)
@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SpringbootBackendApplicationTests {

    @Autowired
    private EmployeeRepository employeeRepository;
    
    @BeforeEach
    void init(@Mock EmployeeRepository employeeRepository) {
        EmployeeService = new DefaultEmployeeService(employeeRepository, employeeRepository);
          
        Mockito.lenient().when(employeeRepository.getEmployeeMinAge()).thenReturn(10);
            
        when(employeeRepository.getEmployeeNameMinLength()).thenReturn(4);
            
        Mockito.lenient()
            .when(employeeRepository.isEmployeenameAlreadyExists(any(String.class)))
                .thenReturn(false);
    }

    // JUnit test for saveEmployee
    @Test
    @Order(1)
    @Rollback(value = false)
    public void saveEmployeeTest(){

        Employee employee = new Employee("ria","marni","ria@mindtree.com");
        employeeRepository.save(employee);
        Assertions.assertThat(employee.getId()).isGreaterThan(0);
    }

    @Test
    @Order(2)
    public void getEmployeeTest(){

        Employee employee = employeeRepository.findByEmailId("Test").get();
        Assertions.assertThat(employee.getEmailId()).isEqualTo("Test");

    }

    @Test
    @Order(3)
    public void getListOfEmployeesTest(){

        List<Employee> employees = employeeRepository.findAll();
        Assertions.assertThat(employees.size()).isGreaterThan(0);

    }

    @Test
    @Order(4)
    @Rollback(value = false)
    public void updateEmployeeTest(){

        Employee employee = employeeRepository.findById(1L).get();
        employee.setEmailId("rishi@gmail.com");
        Employee employeeUpdated =  employeeRepository.save(employee);
        Assertions.assertThat(employeeUpdated.getEmailId()).isEqualTo("rishi@gmail.com");

    }

    @Test
    @Order(5)
    @Rollback(value = false)
    public void deleteEmployeeTest(){

        Employee employee = employeeRepository.findById(1L).get();
        employeeRepository.delete(employee);
        Employee employee1 = null;
        Optional<Employee> optionalEmployee = employeeRepository.findByEmailId("rishi@gmail.com");
        if(optionalEmployee.isPresent()){
            employee1 = optionalEmployee.get();
        }
        Assertions.assertThat(employee1).isNull();
    }

}