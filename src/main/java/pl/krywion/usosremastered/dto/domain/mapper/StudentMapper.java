package pl.krywion.usosremastered.dto.domain.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.krywion.usosremastered.dto.domain.StudentDto;
import pl.krywion.usosremastered.entity.Student;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StudentMapper {

    public StudentDto toDto(Student student) {
        StudentDto studentDto = new StudentDto();
        studentDto.setAlbumNumber(student.getAlbumNumber());
        studentDto.setFirstName(student.getFirstName());
        studentDto.setLastName(student.getLastName());
        studentDto.setEmail(student.getEmail());

        if(student.getStudyPlan() != null) {
            studentDto.setStudyPlanId(student.getStudyPlan().getId());
        }

        if(student.getMasterThesis() != null) {
            studentDto.setMasterThesisIds(Collections.singletonList(student.getMasterThesis().getId()));
        }

        return studentDto;
    }

    public List<StudentDto> toDtoList(List<Student> students) {
        if(students == null) {
            return Collections.emptyList();
        }

        return students.stream()
                .map(this::toDto)
                .toList();
    }

    public Student toEntity(StudentDto studentDto) {
        Student student = new Student();
        student.setFirstName(studentDto.getFirstName());
        student.setLastName(studentDto.getLastName());

        return student;
    }
}
