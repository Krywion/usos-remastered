package pl.krywion.usosremastered.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.krywion.usosremastered.audit.AuditableEntity;

@Entity
@Table(name = "master_theses")
@Getter
@Setter
public class MasterThesis extends AuditableEntity {

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
    private Promoter promoter;

    @Override
    public String getIdentifier() {
        return id.toString();
    }

    @Override
    public String toString() {
        return "MasterThesis{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", academicYear=" + academicYear +
                '}';
    }
}
