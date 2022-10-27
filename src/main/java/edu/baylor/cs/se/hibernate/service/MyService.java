package edu.baylor.cs.se.hibernate.service;

import edu.baylor.cs.se.hibernate.model.Contest;
import edu.baylor.cs.se.hibernate.model.Person;
import edu.baylor.cs.se.hibernate.model.State;
import edu.baylor.cs.se.hibernate.model.Team;
import edu.baylor.cs.se.hibernate.repository.ContestRepository;
import edu.baylor.cs.se.hibernate.repository.PersonRepository;
import edu.baylor.cs.se.hibernate.repository.TeamRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

/**
 * The SuperRepository class interacts with the EntityManager to populate initial data and execute queries for the tasks.
 */
@Service
@AllArgsConstructor
@NoArgsConstructor
@Transactional
public class MyService {

    @Autowired
    PersonRepository personRepository;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    ContestRepository contestRepository;

    /**
     * Populate the initial data.
     */
    public void populate() {
        // contestants - team1
        Person student1 = createPerson(LocalDate.of(1999, 1, 1), "student1", "student1@example.com", "university1");
        Person student2 = createPerson(LocalDate.of(1999, 1, 2), "student2", "student2@example.com", "university2");
        Person student3 = createPerson(LocalDate.of(1999, 1, 3), "student3", "student3@example.com", "university3");

        // contestants - team2
        Person student4 = createPerson(LocalDate.of(1999, 2, 1), "student4", "student4@example.com", "university1");
        Person student5 = createPerson(LocalDate.of(1999, 2, 2), "student5", "student5@example.com", "university2");
        Person student6 = createPerson(LocalDate.of(1999, 2, 3), "student6", "student6@example.com", "university3");

        // contestants - team3
        Person student7 = createPerson(LocalDate.of(1999, 3, 1), "student7", "student7@example.com", "university1");
        Person student8 = createPerson(LocalDate.of(1999, 3, 2), "student8", "student8@example.com", "university2");
        Person student9 = createPerson(LocalDate.of(1999, 3, 3), "student9", "student9@example.com", "university3");

        // contestants - team4
        Person student10 = createPerson(LocalDate.of(1999, 3, 1), "student10", "student10@example.com", "university1");
        Person student11 = createPerson(LocalDate.of(1999, 3, 2), "student11", "student11@example.com", "university2");
        Person student12 = createPerson(LocalDate.of(1999, 3, 3), "student12", "student12@example.com", "university3");

        // contest managers
        Person manager1 = createPerson(LocalDate.of(1985, 1, 1), "manager1", "manager1@example.com", "university1");
        Person manager2 = createPerson(LocalDate.of(1985, 1, 2), "manager2", "manager2@example.com", "university2");
        Person manager3 = createPerson(LocalDate.of(1985, 1, 3), "manager3", "manager3@example.com", "university3");

        // team coaches
        Person coach1 = createPerson(LocalDate.of(1990, 10, 1), "coach1", "coach1@example.com", "university1");
        Person coach2 = createPerson(LocalDate.of(1990, 10, 2), "coach2", "coach2@example.com", "university2");
        Person coach3 = createPerson(LocalDate.of(1990, 10, 3), "coach3", "coach3@example.com", "university3");
        Person coach4 = createPerson(LocalDate.of(1990, 10, 4), "coach4", "coach4@example.com", "university4");

        // create teams
        Team team1 = createTeam("Team1", 1, student1, student2, student3, coach1);
        Team team2 = createTeam("Team2", 2, student4, student5, student6, coach2);
        Team team3 = createTeam("Team3", 10, student7, student8, student9, coach3);
        Team team4 = createTeam("Team4", 5, student10, student11, student12, coach4);

        // create subContest
        Contest subContest = createContest("ICPC Regional", 200,
                LocalDate.of(2022, 1, 10),
                LocalDate.of(2022, 1, 1),
                LocalDate.of(2022, 1, 9),
                manager1);

        // add teams to subContest
        subContest.addTeam(team1);
        subContest.addTeam(team2);
        subContest.addTeam(team3);

        // create mainContest
        Contest mainContest = createContest("ICPC Final", 50,
                LocalDate.of(2022, 6, 11),
                LocalDate.of(2022, 6, 1),
                LocalDate.of(2022, 6, 9),
                manager2);

        // make a clone of team1 into team4 and add it to mainContest
        Team team1Cone = cloneTeam(team1);
        mainContest.addTeam(team1Cone);

        // add subContest to mainContest
        subContest.setParentContest(mainContest);

        // add another contest with one capacity
        Contest smallContest = createContest("Mini Contest", 1,
                LocalDate.of(2022, 6, 11),
                LocalDate.of(2022, 6, 1),
                LocalDate.of(2022, 6, 9),
                manager3);

        smallContest.addTeam(team4);
    }

    // ====================== Assignment 4 tasks ======================

    /**
     * Select all teams in the given contest
     */
    public List<Team> getTeamsByContestName(String contestName) {
        List<Contest> contests = contestRepository.findByName(contestName);
        if (contests.size() == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No contest found for the given name");
        }
        Set<Team> teams = contests.get(0).getTeams();
        return new ArrayList<>(teams);
    }

    /**
     * How many students there are grouped by age
     */
    public List<Map<String, String>> getPersonsGroupByAge() {
        List<Object[]> results = personRepository.countPersonsByAge();
        return convertToMap(results, Arrays.asList("Age", "Count"));
    }

    /**
     * Calculate the current contest occupancy and compare it with the capacity of the contest
     */
    public List<Map<String, String>> getContestOccupancy() {
        List<Object[]> results = contestRepository.contestCapacityAndOccupancy();
        return convertToMap(results, Arrays.asList("Name", "Capacity", "Occupied"));
    }

    // ====================== Assignment 7/8 tasks ======================

    public Team contestRegistrations(Long contestId, Team team) {
        // set team state to PENDING and rank to zero
        team.setState(State.PENDING);
        team.setRank(0);

        Contest contest = getContestById(contestId);

        // check contest writeable
        if (!contest.getIsWritable()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Contest is not writable");
        }

        // Check members and coach size
        if (team.getMembers().size() != 3) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Team does not have 3 members");
        }

        if (team.getCoach() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Team does not have a coach");
        }

        // Fetch or create members
        Set<Person> members = new HashSet<>();
        for (Person m : team.getMembers()) {
            members.add(getOrCreatePerson(m));
        }
        team.setMembers(members);

        // Fetch or create coach
        Person coach = getOrCreatePerson(team.getCoach());
        team.setCoach(coach);

        // Since we are using set for team members, it should be already distinct.
        // Here, we are performing additional check for distinct emails.
        // There will be total 4 distinct emails for coach and members.
        Set<String> emails = new HashSet<>();
        emails.add(coach.getEmail());
        for (Person m : members) {
            emails.add(m.getEmail());
        }
        if (emails.size() != 4) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Team members/coach are not distinct");
        }

        // check contest capacity
        if (contest.getCapacity() == contest.getTeams().size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No more capacity in the contest");
        }

        // check member's age less than 24 years
        for (Person m : members) {
            int age = Period.between(m.getBirthdate(), LocalDate.now()).getYears();
            if (age >= 24) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Team member is not younger than 24 years");
            }
        }

        // check team members are not in a different team in the contest
        for (Person m : members) {
            for (Team t : m.getTeams()) {
                if (t.getContest() != null && t.getContest().getId().equals(contestId)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Team member is already in the contest");
                }
            }
        }

        team.setContest(contest);
        teamRepository.save(team);

        return team;
    }

    public Contest setContestWritable(Long contestId, Boolean isWritable) {
        Contest contest = getContestById(contestId);
        contest.setIsWritable(isWritable);
        return contestRepository.save(contest);
    }

    public Team updateTeam(Team newTeam) {
        Team oldTeam = getTeamById(newTeam.getId());
        if (oldTeam.getContest().getIsWritable()) {
            if (newTeam.getName() != null) {
                oldTeam.setName(newTeam.getName());
            }
            if (newTeam.getRank() != null) {
                oldTeam.setRank(newTeam.getRank());
            }
            if (newTeam.getState() != null) {
                oldTeam.setState(newTeam.getState());
            }
            return teamRepository.save(oldTeam);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Contest of this team is not writable");
        }
    }

    public Contest updateContest(Contest newContest) {
        Contest oldContest = getContestById(newContest.getId());
        if (oldContest.getIsWritable()) {
            if (newContest.getName() != null) {
                oldContest.setName(newContest.getName());
            }
            if (newContest.getCapacity() != null) {
                oldContest.setCapacity(newContest.getCapacity());
            }
            if (newContest.getDate() != null) {
                oldContest.setDate(newContest.getDate());
            }
            return contestRepository.save(oldContest);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Contest is not writable");
        }
    }

    public Team promoteTeam(Long superContestId, Long teamId) {
        Team team = getTeamById(teamId);

        if (team.getRank() < 1 || team.getRank() > 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Team rank should be between 1 and 5");
        }

        // clone team and register to superContest
        Team clonedTeam = cloneTeam(team);
        contestRegistrations(superContestId, clonedTeam);

        return clonedTeam;
    }

    // ====================== Utility ======================

    /**
     * Creates a Person object.
     */
    private Person createPerson(LocalDate birthdate, String name, String email, String university) {
        Person person = new Person();
        person.setBirthdate(birthdate);
        person.setName(name);
        person.setEmail(email);
        person.setUniversity(university);
        return personRepository.save(person);
    }

    /**
     * Creates a Team object with three members and a coach.
     */
    private Team createTeam(String name, Integer rank, Person member1, Person member2, Person member3, Person coach) {
        Team team = new Team();
        team.setName(name);
        team.setRank(rank);
        team.setState(State.ACCEPTED);
        team.getMembers().add(member1);
        team.getMembers().add(member2);
        team.getMembers().add(member3);
        team.setCoach(coach);
        return teamRepository.save(team);
    }

    /**
     * Creates a Contest object with a contest manager.
     */
    private Contest createContest(String name, Integer capacity, LocalDate date, LocalDate registrationFrom, LocalDate registrationTo, Person manager) {
        Contest contest = new Contest();
        contest.setName(name);
        contest.setCapacity(capacity);
        contest.setRegistrationAllowed(true);
        contest.setDate(date);
        contest.setRegistrationFrom(registrationFrom);
        contest.setRegistrationTo(registrationTo);
        contest.getManagers().add(manager);
        return contestRepository.save(contest);
    }

    /**
     * List of all persons.
     */
    public List<Person> getPersons() {
        return personRepository.findAll();
    }

    /**
     * List of all teams.
     */
    public List<Team> getTeams() {
        return teamRepository.findAll();
    }

    /**
     * List of all contests.
     */
    public List<Contest> getContests() {
        return contestRepository.findAll();
    }

    /**
     * Get person by ID, throws error if not found.
     */
    public Person getPersonById(Long id) {
        Optional<Person> optionalPerson = personRepository.findById(id);
        if (!optionalPerson.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Person found for the given id");
        }
        return optionalPerson.get();
    }

    /**
     * Get team by ID, throws error if not found.
     */
    public Team getTeamById(Long id) {
        Optional<Team> optionalTeam = teamRepository.findById(id);
        if (!optionalTeam.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Team found for the given teamID");
        }
        return optionalTeam.get();
    }

    /**
     * Get contest by ID, throws error if not found.
     */
    public Contest getContestById(Long id) {
        Optional<Contest> optionalContest = contestRepository.findById(id);
        if (!optionalContest.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Contest found for the given contestID");
        }
        return optionalContest.get();
    }

    /**
     * Make a clone of the team with same name, sate, rank, members and coach.
     */
    private Team cloneTeam(Team parentTeam) {
        Team clonedTeam = new Team();

        clonedTeam.setName(parentTeam.getName() + "-clone");
        clonedTeam.setRank(parentTeam.getRank());
        clonedTeam.setState(parentTeam.getState());
        clonedTeam.setCoach(parentTeam.getCoach());

        // clonedTeam.setMembers(parentTeam.getMembers());
        for (Person member : parentTeam.getMembers()) {
            clonedTeam.getMembers().add(member);
        }

        clonedTeam.setParentTeam(parentTeam);
        parentTeam.setChildTeam(clonedTeam);

        return teamRepository.save(clonedTeam);
    }

    /**
     * Creates person if id is null, else fetch the person using id.
     */
    public Person getOrCreatePerson(Person person) {
        if (person.getId() == null) {
            return personRepository.save(person);
        } else {
            return getPersonById(person.getId());
        }
    }


    /**
     * Map query results with column names
     */
    public List<Map<String, String>> convertToMap(List<Object[]> results, List<String> columns) {
        List<Map<String, String>> resultsMap = new ArrayList<>();

        for (Object[] entry : results) {
            Map<String, String> entryMap = new HashMap<>();
            for (int i = 0; i < columns.size(); i++) {
                entryMap.put(columns.get(i), entry[i].toString());
            }
            resultsMap.add(entryMap);
        }

        return resultsMap;
    }
}
