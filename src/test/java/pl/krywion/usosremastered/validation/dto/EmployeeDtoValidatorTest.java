package pl.krywion.usosremastered.validation.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.krywion.usosremastered.BaseTest;
import pl.krywion.usosremastered.dto.domain.EmployeeDto;
import pl.krywion.usosremastered.entity.Department;
import pl.krywion.usosremastered.entity.Employee;
import pl.krywion.usosremastered.entity.User;
import pl.krywion.usosremastered.exception.EntityValidationException;
import pl.krywion.usosremastered.exception.ResourceNotFoundException;
import pl.krywion.usosremastered.repository.DepartmentRepository;
import pl.krywion.usosremastered.repository.EmployeeRepository;
import pl.krywion.usosremastered.repository.UserRepository;
import pl.krywion.usosremastered.validation.validators.EmailValidator;
import pl.krywion.usosremastered.validation.validators.PeselValidator;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Employee DTO Validator Tests")
class EmployeeDtoValidatorTest extends BaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmailValidator emailValidator;

    @Mock
    private PeselValidator peselValidator;

    @InjectMocks
    private EmployeeDtoValidator validator;

    @Nested
    @DisplayName("Create validation tests")
    class CreateValidationTests {

        @Test
        @DisplayName("Should validate correct employee DTO")
        void shouldValidateCorrectEmployeeDto() {
            // given
            EmployeeDto dto = createValidEmployeeDto();
            when(emailValidator.isInvalid(anyString())).thenReturn(false);
            when(peselValidator.isInvalid(anyString())).thenReturn(false);
            when(userRepository.existsByEmail(anyString())).thenReturn(false);
            when(departmentRepository.findById(TEST_DEPARTMENT_ID)).thenReturn(Optional.of(new Department()));

            // when/then
            assertDoesNotThrow(() -> validator.validate(dto));
        }

        @Test
        @DisplayName("Should throw exception when first name is null")
        void shouldThrowException_WhenFirstNameIsNull() {
            // given
            EmployeeDto dto = createValidEmployeeDto();
            dto.setFirstName(null);

            // when
            EntityValidationException exception = assertThrows(
                    EntityValidationException.class,
                    () -> validator.validate(dto)
            );

            // then
            assertTrue(exception.getValidationErrors().contains("First name is required"));
        }

        @Test
        @DisplayName("Should throw exception when last name is null")
        void shouldThrowException_WhenLastNameIsNull() {
            // given
            EmployeeDto dto = createValidEmployeeDto();
            dto.setLastName(null);

            // when
            EntityValidationException exception = assertThrows(
                    EntityValidationException.class,
                    () -> validator.validate(dto)
            );

            // then
            assertTrue(exception.getValidationErrors().contains("Last name is required"));
        }

        @Test
        @DisplayName("Should throw exception when email is invalid")
        void shouldThrowException_WhenEmailIsInvalid() {
            // given
            EmployeeDto dto = createValidEmployeeDto();
            when(emailValidator.isInvalid(anyString())).thenReturn(true);
            when(emailValidator.getErrorMessage()).thenReturn("Invalid email format");

            // when
            EntityValidationException exception = assertThrows(
                    EntityValidationException.class,
                    () -> validator.validate(dto)
            );

            // then
            assertTrue(exception.getValidationErrors().contains("Invalid email format"));
        }

        @Test
        @DisplayName("Should throw exception when PESEL is invalid")
        void shouldThrowException_WhenPeselIsInvalid() {
            // given
            EmployeeDto dto = createValidEmployeeDto();
            when(peselValidator.isInvalid(anyString())).thenReturn(true);
            when(peselValidator.getErrorMessage()).thenReturn("Invalid PESEL number");

            // when
            EntityValidationException exception = assertThrows(
                    EntityValidationException.class,
                    () -> validator.validate(dto)
            );

            // then
            assertTrue(exception.getValidationErrors().contains("Invalid PESEL number"));
        }

        @Test
        @DisplayName("Should throw exception when email already exists")
        void shouldThrowException_WhenEmailAlreadyExists() {
            // given
            EmployeeDto dto = createValidEmployeeDto();
            when(emailValidator.isInvalid(anyString())).thenReturn(false);
            when(userRepository.existsByEmail(anyString())).thenReturn(true);

            // when
            EntityValidationException exception = assertThrows(
                    EntityValidationException.class,
                    () -> validator.validate(dto)
            );

            // then
            assertTrue(exception.getValidationErrors().contains("User with this email already exists"));
        }

        @Test
        @DisplayName("Should throw exception when department not found")
        void shouldThrowException_WhenDepartmentNotFound() {
            // given
            EmployeeDto dto = createValidEmployeeDto();
            when(emailValidator.isInvalid(anyString())).thenReturn(false);
            when(userRepository.existsByEmail(anyString())).thenReturn(false);
            when(departmentRepository.findById(TEST_DEPARTMENT_ID)).thenReturn(Optional.empty());

            // when
            EntityValidationException exception = assertThrows(
                    EntityValidationException.class,
                    () -> validator.validate(dto)
            );

            // then
            assertTrue(exception.getValidationErrors().contains("Department not found with id: " + TEST_DEPARTMENT_ID));
        }
    }

    @Nested
    @DisplayName("Update validation tests")
    class UpdateValidationTests {

        @Test
        @DisplayName("Should validate update when email is not changed")
        void shouldValidateUpdate_WhenEmailIsNotChanged() {
            // given
            String pesel = TEST_PESEL;
            EmployeeDto dto = createValidEmployeeDto();
            Employee existingEmployee = createExistingEmployee();

            when(employeeRepository.findByPesel(pesel)).thenReturn(Optional.of(existingEmployee));
            when(emailValidator.isInvalid(anyString())).thenReturn(false);
            when(departmentRepository.findById(TEST_DEPARTMENT_ID)).thenReturn(Optional.of(new Department()));

            // when/then
            assertDoesNotThrow(() -> validator.validateForUpdate(dto, pesel));
        }

        @Test
        @DisplayName("Should throw exception when updating non-existent employee")
        void shouldThrowException_WhenUpdatingNonExistentEmployee() {
            // given
            String pesel = TEST_PESEL;
            EmployeeDto dto = createValidEmployeeDto();
            when(employeeRepository.findByPesel(pesel)).thenReturn(Optional.empty());

            // when/then
            assertThrows(ResourceNotFoundException.class,
                    () -> validator.validateForUpdate(dto, pesel));
        }

        @Test
        @DisplayName("Should throw exception when PESEL is modified")
        void shouldThrowException_WhenPeselIsModified() {
            // given
            EmployeeDto dto = createValidEmployeeDto();
            dto.setPesel("98765432109");
            Employee existingEmployee = createExistingEmployee();

            when(employeeRepository.findByPesel(anyString())).thenReturn(Optional.of(existingEmployee));

            // when
            EntityValidationException exception = assertThrows(
                    EntityValidationException.class,
                    () -> validator.validateForUpdate(dto, TEST_PESEL)
            );

            // then
            assertTrue(exception.getValidationErrors().contains("PESEL cannot be modified"));
        }
    }

    private EmployeeDto createValidEmployeeDto() {
        EmployeeDto dto = new EmployeeDto();
        dto.setPesel(TEST_PESEL);
        dto.setFirstName(TEST_FIRST_NAME);
        dto.setLastName(TEST_LAST_NAME);
        dto.setEmail(TEST_EMAIL);
        dto.setDepartmentId(TEST_DEPARTMENT_ID);
        dto.setCourseIds(Collections.emptyList());
        return dto;
    }

    private Employee createExistingEmployee() {
        Employee employee = new Employee();
        employee.setPesel(TEST_PESEL);
        employee.setFirstName(TEST_FIRST_NAME);
        employee.setLastName(TEST_LAST_NAME);

        User user = new User();
        user.setEmail(TEST_EMAIL);
        employee.setUser(user);

        Department department = new Department();
        department.setId(TEST_DEPARTMENT_ID);
        employee.setDepartment(department);

        return employee;
    }
}