package pl.krywion.usosremastered.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Entity
@Table(name = "faculties")
@Getter
@Setter
@NoArgsConstructor
public class Faculty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name="postal_code", nullable = false)
    private String postalCode;

    @Column(name="establishment_year", nullable = false)
    private Integer establishmentYear;

    @OneToMany(mappedBy = "faculty")
    private List<Institute> institutes;

    @OneToOne
    @JoinColumn(name = "dean_id")
    private Dean dean;
}
