package pl.krywion.usosremastered.validation.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.krywion.usosremastered.BaseTest;
import pl.krywion.usosremastered.dto.domain.StudentDto;
import pl.krywion.usosremastered.entity.Student;
import pl.krywion.usosremastered.entity.User;
import pl.krywion.usosremastered.exception.EntityValidationException;
import pl.krywion.usosremastered.repository.StudentRepository;
import pl.krywion.usosremastered.repository.UserRepository;
import pl.krywion.usosremastered.validation.validators.EmailValidator;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Student Dto Validator Tests")
class StudentDtoValidatorTest extends BaseTest {

    private StudentRepository studentRepository;

    private EmailValidator emailValidator;

    private UserRepository userRepository;

    private StudentDtoValidator studentDtoValidator;

    @BeforeEach
    void setUp() {
        studentRepository = mock(StudentRepository.class);
        emailValidator = mock(EmailValidator.class);
        userRepository = mock(UserRepository.class);
        studentDtoValidator = new StudentDtoValidator(userRepository, studentRepository, emailValidator);
    }

    @Test
    @DisplayName("Should validate correct student DTO")
    void shouldValidateCorrectStudentDto() {
        // given
        StudentDto studentDto = createValidStudentDto();

        when(emailValidator.isInvalid(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);

        // when/then
        assertDoesNotThrow(() -> studentDtoValidator.validate(studentDto));
    }

    @Test
    @DisplayName("Should throw exception when first name is null")
    void shouldThrowException_WhenFirstNameIsNull() {
        // given
        StudentDto studentDto = createValidStudentDto();
        studentDto.setFirstName(null);

        // when
        EntityValidationException exception = assertThrows(
                EntityValidationException.class,
                () -> studentDtoValidator.validate(studentDto)
        );

        // then
        assertTrue(exception.getValidationErrors().contains("First name is required"));
    }

    @Test
    @DisplayName("Should throw exception when last name is null")
    void shouldThrowException_WhenLastNameIsNull() {
        // given
        StudentDto studentDto = createValidStudentDto();
        studentDto.setLastName(null);

        // when
        EntityValidationException exception = assertThrows(
                EntityValidationException.class,
                () -> studentDtoValidator.validate(studentDto)
        );

        // then
        assertTrue(exception.getValidationErrors().contains("Last name is required"));
    }

    @Test
    @DisplayName("Should throw exception when first name is empty")
    void shouldThrowException_WhenFirstNameIsEmpty() {
        // given
        StudentDto studentDto = createValidStudentDto();
        studentDto.setFirstName("");

        // when
        EntityValidationException exception = assertThrows(
                EntityValidationException.class,
                () -> studentDtoValidator.validate(studentDto)
        );

        // then
        assertTrue(exception.getValidationErrors().contains("First name is required"));
    }

    @Test
    @DisplayName("Should throw exception when last name is empty")
    void shouldThrowException_WhenLastNameIsEmpty() {
        // given
        StudentDto studentDto = createValidStudentDto();
        studentDto.setLastName("");

        // when
        EntityValidationException exception = assertThrows(
                EntityValidationException.class,
                () -> studentDtoValidator.validate(studentDto)
        );

        // then
        assertTrue(exception.getValidationErrors().contains("Last name is required"));
    }

    @Test
    @DisplayName("Should throw exception when email is null")
    void shouldThrowException_WhenEmailIsNull() {
        // given
        StudentDto studentDto = createValidStudentDto();
        studentDto.setEmail(null);

        // when
        EntityValidationException exception = assertThrows(
                EntityValidationException.class,
                () -> studentDtoValidator.validate(studentDto)
        );

        // then
        assertTrue(exception.getValidationErrors().contains("Email is required"));
    }

    @Test
    @DisplayName("Should throw exception when email is invalid")
    void shouldThrowException_WhenEmailIsInvalid() {
        // given
        StudentDto studentDto = createValidStudentDto();
        studentDto.setEmail("invalid-email");
        when(emailValidator.isInvalid(anyString())).thenReturn(true);
        when(emailValidator.getErrorMessage()).thenReturn("Invalid email format");

        // when
        EntityValidationException exception = assertThrows(
                EntityValidationException.class,
                () -> studentDtoValidator.validate(studentDto)
        );

        // then
        assertTrue(exception.getValidationErrors().contains("Invalid email format"));
    }

    @Test
    @DisplayName("Should throw exception when email already exists")
    void shouldThrowException_WhenEmailAlreadyExists() {
        // given
        StudentDto studentDto = createValidStudentDto();
        when(emailValidator.isInvalid(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        // when
        EntityValidationException exception = assertThrows(
                EntityValidationException.class,
                () -> studentDtoValidator.validate(studentDto)
        );

        // then
        assertTrue(exception.getValidationErrors().contains("Email already exists"));
    }

    @Test
    @DisplayName("Should validate update when email is not changed")
    void shouldValidateUpdate_WhenEmailIsNotChanged() {
        // given
        Long albumNumber = TEST_ALBUM_NUMBER;
        StudentDto dto = createValidStudentDto();
        Student existingStudent = createExistingStudent();

        when(studentRepository.findById(albumNumber)).thenReturn(Optional.of(existingStudent));
        when(emailValidator.isInvalid(anyString())).thenReturn(false);

        // when/then
        assertDoesNotThrow(() -> studentDtoValidator.validateForUpdate(dto, albumNumber));
    }

    private StudentDto createValidStudentDto() {
        StudentDto studentDto = new StudentDto();
        studentDto.setFirstName(TEST_FIRST_NAME);
        studentDto.setLastName(TEST_LAST_NAME);
        studentDto.setEmail(TEST_EMAIL);
        return studentDto;
    }

    private Student createExistingStudent() {
        Student student = new Student();
        student.setAlbumNumber(TEST_ALBUM_NUMBER);
        student.setFirstName(TEST_FIRST_NAME);
        student.setLastName(TEST_LAST_NAME);

        User user = new User();
        user.setEmail(TEST_EMAIL);
        student.setUser(user);
        return student;
    }
}
