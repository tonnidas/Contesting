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
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * The Contest class defines a contest entity.
 * It has relation with Team and Person entities.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Contest {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private Integer capacity;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Boolean registrationAllowed;

    @Column(nullable = false)
    private LocalDate registrationFrom;

    @Column(nullable = false)
    private LocalDate registrationTo;

    @Column(nullable = false)
    private Boolean isWritable = false;

    // Team-Contest relation (Inverse side)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "contest")
    @JsonIdentityReference(alwaysAsId = true)
    @Size(min = 1)
    private Set<Team> teams = new HashSet<>();

    // Contest-Contest relation (Inverse side)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parentContest")
    @JsonIdentityReference(alwaysAsId = true)
    private Set<Contest> subContests = new HashSet<>();

    // Contest-Contest relation (Owning side)
    @ManyToOne
    @JoinColumn(name = "PARENT_CONTEST_ID")
    @JsonIdentityReference(alwaysAsId = true)
    private Contest parentContest;

    // Person-Contest relation (Owner side)
    @ManyToMany
    @JoinTable(name = "PERSON_CONTEST",
            joinColumns = {@JoinColumn(name = "CONTEST_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "PERSON_ID", referencedColumnName = "ID")})
    @JsonIdentityReference(alwaysAsId = true)
    @Size(min = 1)
    private Set<Person> managers = new HashSet<>();

    public void addTeam(Team team) {
        this.teams.add(team);
        team.setContest(this);
    }
}
