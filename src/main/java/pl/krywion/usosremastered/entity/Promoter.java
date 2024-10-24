package pl.krywion.usosremastered.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
@DiscriminatorValue("promoter")
public class Promoter extends Employee {

    @OneToMany(mappedBy = "promoter")
    private List<MasterThesis> masterTheses;
}
