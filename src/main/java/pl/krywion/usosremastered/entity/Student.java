package pl.krywion.usosremastered.entity;

import jakarta.persistence.*;
import lombok.*;
import pl.krywion.usosremastered.audit.AuditableEntity;

import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name="students")
@Getter
@Setter
public class Student extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "album_number", nullable = false, unique = true)
    private Long albumNumber;

    @OneToOne
    @JoinColumn(name="user_id")
    private User user;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @ManyToMany
    @JoinTable(
            name = "students_study_plans",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "study_plan_id")
    )
    private Set<StudyPlan> studyPlans = new HashSet<>();

    @OneToOne
    @JoinColumn(name="master_thesis_id")
    private MasterThesis masterThesis;

    public Student() {
        // empty constructor for:
        // - tests
        // - mappers
    }

    public String getEmail() {
        return user.getEmail();
    }

    @Override
    public String getIdentifier() {
        return albumNumber.toString();
    }

    @Override
    public String toString() {
        return "Student{" +
                "albumNumber=" + albumNumber +
                ", user=" + user +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", studyPlan=" + studyPlans +
                ", masterThesis=" + masterThesis +
                '}';
    }
}
