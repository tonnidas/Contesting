package edu.baylor.cs.se.hibernate.repository;

import edu.baylor.cs.se.hibernate.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    @Query("SELECT DATEDIFF(YEAR, p.birthdate, CURRENT_DATE()) as age, COUNT(p.id) FROM Person p GROUP BY age")
    List<Object[]> countPersonsByAge();
}
