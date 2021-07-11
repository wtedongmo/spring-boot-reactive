package com.twb.employee.reactive;

import static org.springframework.web.reactive.function.BodyExtractors.toMono;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import com.twb.employee.Employee;
import com.twb.employee.EmployeeRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Configuration
public class EmployeeFunctionalConfig {

    @Bean
    EmployeeRepository employeeRepository2() {
        return new EmployeeRepository();
    }

    @Bean
    RouterFunction<ServerResponse> getAllEmployeesRoute() {
        return route(GET("/employees/reactive"),
                req -> ok().body(
                        employeeRepository2().findAllEmployees(), Employee.class));
    }

    @Bean
    RouterFunction<ServerResponse> getEmployeeByIdRoute() {
        return route(GET("/employees/reactive/{id}"),
                req -> ok().body(
                        employeeRepository2().findEmployeeById(req.pathVariable("id")), Employee.class));
    }

    // Return nothing
    @Bean
    RouterFunction<ServerResponse> updateEmployeeRoute() {
        return route(POST("/employees/reactive/update"),
                req -> req.body(toMono(Employee.class))
                        .doOnNext(employeeRepository2()::updateEmployee)
                        .flatMap(empl -> ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(BodyInserters.fromValue(empl))));
//                        .then(ok().build()));
    }

    // Return nothing
    @Bean
    RouterFunction<ServerResponse> addEmployeeRoute() {
        return route(POST("/employees/reactive/add"),
                req -> req.body(toMono(Employee.class))
                        .doOnNext(employeeRepository2()::addEmployee)
                        .then(ok().build()));
    }

    //With return object
    @Bean
    public RouterFunction<ServerResponse> root(EmployeeHandler employeeHandler) {
        return route()
                .POST("/employees/reactive/handler/add", employeeHandler::addEmployee)
                .POST("/employees/reactive/handler/update", employeeHandler::updateEmployee)
                .GET("/employees/reactive/handler/{id}", employeeHandler::getEmployeeById)
//                .POST("/saveWriter", RequestPredicates.contentType(MediaType.APPLICATION_JSON), bookHandler::saveWriter)
                .build();
    }

    @Bean
    RouterFunction<ServerResponse> composedRoutes() {
        return
                route(GET("/employees/reactive"),
                        req -> ok().body(
                                employeeRepository2().findAllEmployees(), Employee.class))

                        .and(route(GET("/employees/reactive/{id}"),
                                req -> ok().body(
                                        employeeRepository2().findEmployeeById(req.pathVariable("id")), Employee.class)))

                        .and(route(POST("/employees/reactive/update"),
                                req -> req.body(toMono(Employee.class))
                                        .doOnNext(employeeRepository2()::updateEmployee)
                                        .then(ok().build())));
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.csrf()
                .disable()
                .authorizeExchange()
                .anyExchange()
                .permitAll();
        return http.build();
    }
}
