package pl.krywion.usosremastered.entity;

import jakarta.persistence.*;
import pl.krywion.usosremastered.audit.AuditableEntity;

@Entity
@Table(name="exercise_groups")
public class ExerciseGroup extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "group_number", nullable = false)
    private Integer groupNumber;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Override
    public String getIdentifier() {
        return id.toString();
    }
}
