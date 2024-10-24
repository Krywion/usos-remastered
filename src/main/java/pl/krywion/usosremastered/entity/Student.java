package pl.krywion.usosremastered.entity;

import jakarta.persistence.*;


@Entity
@Table(name="students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "album_number", nullable = false, unique = true)
    private String albumNumber;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @ManyToOne
    @JoinColumn(name="study_plan_id")
    private StudyPlan studyPlan;

    @OneToOne
    @JoinColumn(name="master_thesis_id")
    private MasterThesis masterThesis;
}
