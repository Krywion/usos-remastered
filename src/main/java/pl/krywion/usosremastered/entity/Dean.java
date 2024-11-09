package pl.krywion.usosremastered.entity;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("Dean")
public class Dean extends Employee{

    @Column(name = "year_of_election")
    private Integer yearOfElection;

}
