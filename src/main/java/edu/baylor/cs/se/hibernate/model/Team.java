package edu.baylor.cs.se.hibernate.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

/**
 * The Team class defines a team entity.
 * It has relation with Contest and Person entities.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Team {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer rank;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private State state;

    // Team-Team relation (Owning side)
    @OneToOne
    @JoinColumn(name = "PARENT_TEAM_ID")
    @JsonIdentityReference(alwaysAsId = true)
    private Team parentTeam;

    // Team-Team relation (Inverse side)
    @OneToOne(mappedBy = "parentTeam")
    @JsonIdentityReference(alwaysAsId = true)
    private Team childTeam;

    // Contest-Team relation (Owning side)
    @ManyToOne
    @JoinColumn(name = "CONTEST_ID")
    @JsonIdentityReference(alwaysAsId = true)
    private Contest contest;

    // Person-Team members relation (Owning side)
    @ManyToMany
    @JoinTable(name = "TEAM_PERSON",
            joinColumns = {@JoinColumn(name = "TEAM_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "PERSON_ID", referencedColumnName = "ID")})
    @JsonIdentityReference(alwaysAsId = true)
    @Size(min = 1, max = 3)
    private Set<Person> members = new HashSet<>();

    // Person-Team coach relation (Owning side)
    @ManyToOne
    @JoinColumn(name = "COACH_ID")
    @JsonIdentityReference(alwaysAsId = true)
    private Person coach;
}
