package edu.baylor.cs.se.hibernate.repository;

import edu.baylor.cs.se.hibernate.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
}
