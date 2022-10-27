package edu.baylor.cs.se.hibernate.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.baylor.cs.se.hibernate.util.DedupingObjectIdResolver;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * The Person class defines a person entity.
 * It has relation with Team and Contest entities.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", resolver = DedupingObjectIdResolver.class)
public class Person {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private LocalDate birthdate;

    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "must contain valid email address")
    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String university;

    // Contest-Person relation (Inverse side)
    @ManyToMany(mappedBy = "managers")
    @JsonIdentityReference(alwaysAsId = true)
    private Set<Contest> managedContests = new HashSet<>();

    // Team-Person members relation (Inverse side)
    @ManyToMany(mappedBy = "members")
    @JsonIdentityReference(alwaysAsId = true)
    private Set<Team> teams = new HashSet<>();

    // Team-Person coach relation (Inverse side)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "coach")
    @JsonIdentityReference(alwaysAsId = true)
    private Set<Team> coachOfTeams = new HashSet<>();
}
