package pl.krywion.usosremastered.entity;

import jakarta.persistence.*;
import lombok.Getter;
import pl.krywion.usosremastered.audit.AuditableEntity;

import java.util.List;

@Entity
@Getter
@Table(name="courses")
public class Course extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "capacity_limit", nullable = false)
    private int capacityLimit;

    @Column(name="ects", nullable = false)
    private Integer ects;

    @OneToMany(mappedBy = "course")
    private List<ExerciseGroup> exerciseGroups;

    @ManyToMany(mappedBy = "courses")
    private List<Employee> employees;

    @Override
    public String getIdentifier() {
        return id.toString();
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", capacityLimit=" + capacityLimit +
                ", ects=" + ects +
                '}';
    }
}
