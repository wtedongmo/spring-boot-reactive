package com.twb.employee.reactive;

import com.twb.employee.Employee;
import com.twb.employee.EmployeeRepository;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
public class EmployeeHandler {

    private EmployeeRepository employeeRepository;

    public EmployeeHandler(EmployeeRepository employeeRepository){
        this.employeeRepository = employeeRepository;
    }

    public Mono<ServerResponse> addEmployee(ServerRequest request){
        return request.bodyToMono(Employee.class)
                .flatMap(empl -> employeeRepository.addEmployee(empl) )
                .flatMap(empl -> ServerResponse.created(URI.create("/employees/reactive/add2"+ empl.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(empl)));
    }

    public Mono<ServerResponse> updateEmployee(ServerRequest request){

        return request.bodyToMono(Employee.class)
                .flatMap(empl -> employeeRepository.updateEmployee(empl) )
                .flatMap(empl -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(empl)));
    }

    public Mono<ServerResponse>  getEmployeeById(ServerRequest request) {
        return  ok().body(
                employeeRepository.findEmployeeById(request.pathVariable("id")), Employee.class);
    }
}
