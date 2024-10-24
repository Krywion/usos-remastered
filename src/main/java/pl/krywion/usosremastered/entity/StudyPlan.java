package pl.krywion.usosremastered.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "study_plans")
public class StudyPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "studyPlan")
    private List<Student> students;
}
