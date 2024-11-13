package pl.krywion.usosremastered.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.krywion.usosremastered.audit.AuditableEntity;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="employees")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "employee_type", discriminatorType = DiscriminatorType.STRING)
public class Employee extends AuditableEntity {

    @Id
    @Column(name = "pesel", nullable = false, unique = true)
    private String pesel;

    @OneToOne
    @JoinColumn(name="user_id")
    private User user;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name="hire_date", nullable = false)
    private LocalDate hireDate;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToMany(mappedBy = "promoter")
    private List<MasterThesis> masterTheses;

    @ManyToMany
    @JoinTable(
            name = "employee_courses",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> courses;

    public String getEmail() {
        return user.getEmail();
    }

    @Override
    public String getIdentifier() {
        return pesel;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "pesel='" + pesel + '\'' +
                ", user=" + user +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", hireDate=" + hireDate +
                ", department=" + department +
                '}';
    }
}
