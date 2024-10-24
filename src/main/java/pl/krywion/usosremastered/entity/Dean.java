package pl.krywion.usosremastered.entity;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("Dean")
public class Dean extends Employee{

    @Column(name = "year_of_election", nullable = false)
    private Integer yearOfElection;

    @OneToOne
    @JoinColumn(name = "faculty_id")
    private Faculty faculty;
}
