package edu.baylor.cs.se.hibernate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.baylor.cs.se.hibernate.model.Team;
import edu.baylor.cs.se.hibernate.repository.ContestRepository;
import edu.baylor.cs.se.hibernate.repository.PersonRepository;
import edu.baylor.cs.se.hibernate.repository.TeamRepository;
import edu.baylor.cs.se.hibernate.service.MyService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ContestTest {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private ContestRepository contestRepository;

    private MyService myService;

    public void init() {
        myService = new MyService(personRepository, teamRepository, contestRepository);
        myService.populate();
    }

    @Test
    public void teamsByContestTest() throws JsonProcessingException {
        init();

        List<Team> teams = myService.getTeamsByContestName("ICPC Regional");
        String teamsJsonString = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(teams);
        System.out.println("***************** Teams for Contest: ICPC Regional *****************");
        System.out.println(teamsJsonString);
        System.out.println("=====================================================================");
        assert (teams.size() == 3);

        teams = myService.getTeamsByContestName("ICPC Final");
        teamsJsonString = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(teams);
        System.out.println("****************** Teams for Contest: ICPC Final ******************");
        System.out.println(teamsJsonString);
        System.out.println("===================================================================");
        assert (teams.size() == 1);
    }

    @Test
    public void personsGroupByAgeTest() throws JsonProcessingException {
        init();

        List<Map<String, String>> ageGroups = myService.getPersonsGroupByAge();
        assert (ageGroups.size() == 3);

        String ageGroupsJsonString = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(ageGroups);

        System.out.println("***************** Persons Group By Age *****************");
        System.out.println(ageGroupsJsonString);
        System.out.println("========================================================");
    }

    @Test
    public void contestOccupancyTest() throws JsonProcessingException {
        init();

        List<Map<String, String>> contestOccupancy = myService.getContestOccupancy();
        assert (contestOccupancy.size() == 3);

        String ageGroupJsonString = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(contestOccupancy);

        System.out.println("***************** Contest Occupancy *****************");
        System.out.println(ageGroupJsonString);
        System.out.println("=====================================================");
    }
}
