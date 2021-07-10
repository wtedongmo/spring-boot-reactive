package com.twb.employee.webflux;

import com.twb.employee.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class EmployeeWebClient {

    Logger logger = LoggerFactory.getLogger(EmployeeWebClient.class);
    WebClient client = WebClient.create("http://localhost:8080/employees/webflux");

    public void consume() {

        logger.info("\nOne Employee");
        Mono<Employee> employeeMono = client.get()
                .uri("/{id}", "1")
                .retrieve()
                .bodyToMono(Employee.class);

        employeeMono.subscribe(System.out::println);

        logger.info("\nAll Employees");
        Flux<Employee> employeeFlux = client.get()
                .uri("")
                .retrieve()
                .bodyToFlux(Employee.class);

        employeeFlux.subscribe(System.out::println);

        logger.info("\nUpdate Employee");
        Employee empl = new Employee("2", "Martin Will");
        employeeMono = client.post()
                .uri("/update")
                .body(Mono.just(empl), Employee.class)
                .retrieve()
                .bodyToMono(Employee.class);

        employeeMono.subscribe(System.out::println);
    }
}
