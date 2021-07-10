package com.twb.employee.reactive;

import com.twb.employee.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class EmployeeReactiveWebClient {

    Logger logger = LoggerFactory.getLogger(EmployeeReactiveWebClient.class);
    WebClient client = WebClient.create("http://localhost:8080/employees/reactive");

    public void consume() {

        logger.info("\nReact One Employee");
        Mono<Employee> employeeMono = client.get()
                .uri("/{id}", "1")
                .retrieve()
                .bodyToMono(Employee.class);

        employeeMono.subscribe(System.out::println);

        logger.info("\nReact All Employees");
        Flux<Employee> employeeFlux = client.get()
                .uri("")
                .retrieve()
                .bodyToFlux(Employee.class);

        employeeFlux.subscribe(System.out::println);

        logger.info("\nReact Update Employee");
        Employee empl = new Employee("4", "Max Will");
        employeeMono = client.post()
                .uri("/update")
                .body(Mono.just(empl), Employee.class)
                .retrieve()
                .bodyToMono(Employee.class);

        employeeMono.subscribe(System.out::println);

        logger.info("\nReact Add Employee");
        empl = new Employee("4", "Empl Insert");
        employeeMono = client.post()
                .uri("/add")
                .body(Mono.just(empl), Employee.class)
                .retrieve()
                .bodyToMono(Employee.class);

        employeeMono.subscribe(System.out::println);
    }
}
