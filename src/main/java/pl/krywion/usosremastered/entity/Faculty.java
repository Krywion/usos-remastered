package pl.krywion.usosremastered.entity;

import jakarta.persistence.*;

import java.util.List;


@Entity
@Table(name = "faculties")
public class Faculty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name="postal_code", nullable = false)
    private String postalCode;

    @Column(name="estamblishment_year", nullable = false)
    private int estamblishmentYear;

    @OneToMany(mappedBy = "faculty")
    private List<Institute> institutes;
}
