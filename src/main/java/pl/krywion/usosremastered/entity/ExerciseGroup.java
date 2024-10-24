package pl.krywion.usosremastered.entity;

import jakarta.persistence.*;

@Entity
@Table(name="exercise_groups")
public class ExerciseGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "group_number", nullable = false)
    private Integer groupNumber;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
}
