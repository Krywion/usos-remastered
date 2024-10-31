package pl.krywion.usosremastered.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name="students")
@Setter
@Getter
@ToString
public class Student {

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

    @ManyToOne
    @JoinColumn(name="study_plan_id")
    private StudyPlan studyPlan;

    @OneToOne
    @JoinColumn(name="master_thesis_id")
    private MasterThesis masterThesis;
}
