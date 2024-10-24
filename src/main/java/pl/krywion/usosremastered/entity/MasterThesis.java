package pl.krywion.usosremastered.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "master_theses")
public class MasterThesis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name="academic_year", nullable = false)
    private int academicYear;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "promoter_id")
    private Employee promoter;
}
