package pl.krywion.usosremastered.entity;

import jakarta.persistence.*;

@Entity
@Table(name="institutes")
public class Institute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ManyToOne
    @JoinColumn(name = "faculty_id")
    private Faculty faculty;

    @OneToOne
    @JoinColumn(name = "manager_id")
    private Employee manager;

}
