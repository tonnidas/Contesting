package edu.baylor.cs.se.hibernate.repository;

import edu.baylor.cs.se.hibernate.model.Contest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContestRepository extends JpaRepository<Contest, Long> {
    List<Contest> findByName(String name);

    @Query("SELECT c.name, c.capacity, c.teams.size FROM Contest c")
    List<Object[]> contestCapacityAndOccupancy();
}
