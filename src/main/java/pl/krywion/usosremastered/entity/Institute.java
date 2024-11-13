package pl.krywion.usosremastered.entity;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.krywion.usosremastered.audit.AuditableEntity;

import java.util.List;

@Entity
@Table(name="institutes")
@Getter
@Setter
@NoArgsConstructor
public class Institute extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ManyToOne
    @JoinColumn(name = "faculty_id")
    private Faculty faculty;

    @OneToMany(mappedBy = "institute")
    private List<Department> departments;

    @OneToOne
    @JoinColumn(name = "manager_id")
    private Employee manager;

    @Override
    public String getIdentifier() {
        return id.toString();
    }

    @Override
    public String toString() {
        return "Institute{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
