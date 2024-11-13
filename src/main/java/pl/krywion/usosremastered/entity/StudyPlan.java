package pl.krywion.usosremastered.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.krywion.usosremastered.audit.AuditableEntity;

@Entity
@Table(name = "study_plans")
@Getter
@Setter
public class StudyPlan extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Override
    public String getIdentifier() {
        return id.toString();
    }

    @Override
    public String toString() {
        return "StudyPlan{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
