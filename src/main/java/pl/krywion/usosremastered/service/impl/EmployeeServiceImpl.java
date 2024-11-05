package pl.krywion.usosremastered.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.krywion.usosremastered.config.security.Role;
import pl.krywion.usosremastered.dto.EmployeeDto;
import pl.krywion.usosremastered.dto.RegisterUserDto;
import pl.krywion.usosremastered.dto.response.EmployeeResponseDto;
import pl.krywion.usosremastered.entity.Employee;
import pl.krywion.usosremastered.exception.EmployeeCreationException;
import pl.krywion.usosremastered.repository.EmployeeRepository;
import pl.krywion.usosremastered.service.AuthenticationService;
import pl.krywion.usosremastered.service.EmployeeService;
import pl.krywion.usosremastered.validation.dto.EmployeeDtoValidator;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final AuthenticationService authenticationService;
    private final ModelMapper modelMapper;
    private final EmployeeRepository employeeRepository;
    private final EmployeeDtoValidator employeeValidator;

    public EmployeeServiceImpl(AuthenticationService authenticationService,
                               ModelMapper modelMapper,
                               EmployeeRepository employeeRepository,
                               EmployeeDtoValidator employeeValidator
    ) {
        this.authenticationService = authenticationService;
        this.modelMapper = modelMapper;
        this.employeeRepository = employeeRepository;
        this.employeeValidator = employeeValidator;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public EmployeeResponseDto createEmployee(EmployeeDto employeeDto) {
        try {
            employeeValidator.validate(employeeDto);

            Employee employee = modelMapper.map(employeeDto, Employee.class);
            employee.setHireDate(LocalDate.now());

            RegisterUserDto registerUserDto = new RegisterUserDto();
            registerUserDto.setEmail(employeeDto.getEmail());
            registerUserDto.setRole(Role.EMPLOYEE);

            this.authenticationService.signUp(registerUserDto);

            employeeRepository.save(employee);

            EmployeeResponseDto response = new EmployeeResponseDto();
            response.setEmployeeDto(employeeDto);
            response.setMessage("Employee created successfully");
            response.setSuccess(true);

            return response;
        } catch (Exception e) {
            log.error("Could not create Employee: ", e);
            throw new EmployeeCreationException("Could not create Employee: " + e.getMessage(), e);
        }
    }

    @Override
    public EmployeeDto getEmployeeByEmail(String email) {
        return null;
    }

    @Override
    public List<EmployeeDto> getEmployeesByLastName(String lastName) {
        return null;
    }

    @Override
    public List<EmployeeDto> getAllEmployees() {
        return null;
    }

    @Override
    public EmployeeResponseDto deleteEmployee(Long employeeId) {
        return null;
    }


}
