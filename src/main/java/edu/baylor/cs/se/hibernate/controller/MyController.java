package edu.baylor.cs.se.hibernate.controller;

import edu.baylor.cs.se.hibernate.model.Contest;
import edu.baylor.cs.se.hibernate.model.Person;
import edu.baylor.cs.se.hibernate.model.Team;
import edu.baylor.cs.se.hibernate.service.MyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Defines the API for the app.
 */
@RestController
public class MyController {

    @Autowired
    private MyService myService;

    @PostMapping("/populate")
    public ResponseEntity<String> populate() {
        myService.populate();
        return new ResponseEntity<>("Data populated", HttpStatus.OK);
    }

    @GetMapping("/persons")
    public ResponseEntity<List<Person>> getPersons() {
        return new ResponseEntity<>(myService.getPersons(), HttpStatus.OK);
    }

    @GetMapping("/teams")
    public ResponseEntity<List<Team>> getTeams() {
        return new ResponseEntity<>(myService.getTeams(), HttpStatus.OK);
    }

    @GetMapping("/contests")
    public ResponseEntity<List<Contest>> getContests() {
        return new ResponseEntity<>(myService.getContests(), HttpStatus.OK);
    }

    // ====================== Assignment 4 tasks ======================

    @GetMapping("/teamsByContest")
    public ResponseEntity<List<Team>> getTeamsByContestName(@RequestParam String contestName) {
        return new ResponseEntity<>(myService.getTeamsByContestName(contestName), HttpStatus.OK);
    }

    @GetMapping("/personsGroupByAge")
    public ResponseEntity<List<Map<String, String>>> getPersonsGroupByAge() {
        return new ResponseEntity<>(myService.getPersonsGroupByAge(), HttpStatus.OK);
    }

    @GetMapping("/contestsOccupancy")
    public ResponseEntity<List<Map<String, String>>> getContestOccupancy() {
        return new ResponseEntity<>(myService.getContestOccupancy(), HttpStatus.OK);
    }

    // ====================== Assignment 7-8 tasks ======================

    @PostMapping("/contestsRegistration")
    public ResponseEntity<Team> getRegistrations(@RequestParam Long contestId, @RequestBody Team team) {
        return new ResponseEntity<>(myService.contestRegistrations(contestId, team), HttpStatus.OK);
    }

    @PostMapping("/setEditable")
    public ResponseEntity<Contest> setEditable(@RequestParam Long contestId) {
        return new ResponseEntity<>(myService.setContestWritable(contestId, true), HttpStatus.OK);
    }

    @PostMapping("/setReadOnly")
    public ResponseEntity<Contest> setReadOnly(@RequestParam Long contestId) {
        return new ResponseEntity<>(myService.setContestWritable(contestId, false), HttpStatus.OK);
    }

    @PostMapping("/editTeam")
    public ResponseEntity<Team> editTeam(@RequestBody Team team) {
        return new ResponseEntity<>(myService.updateTeam(team), HttpStatus.OK);
    }

    @PostMapping("/editContest")
    public ResponseEntity<Contest> editContest(@RequestBody Contest contest) {
        return new ResponseEntity<>(myService.updateContest(contest), HttpStatus.OK);
    }

    @PostMapping("/promote")
    public ResponseEntity<Team> promote(@RequestParam Long superContestId, @RequestParam Long teamId) {
        return new ResponseEntity<>(myService.promoteTeam(superContestId, teamId), HttpStatus.OK);
    }
}