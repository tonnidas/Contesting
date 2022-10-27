# Contesting

- Run Junit Tests (Disable logs to clearly print the reports)

```
$ mvn test -Dlogging.level.root=OFF -Dspring.jpa.properties.hibernate.show_sql=false
```

- Run App

```
$  mvn spring-boot:run
```

- API
    - `POST /populate`: Populate sample data.
    - `GET /persons`: Show all persons.
    - `GET /teams`: Show all teams.
    - `GET /contests`: Show all contests.

- API (Assignment 4)
  - `GET /teamsByContest?contestName={}`: Get teams for a given contest name.
  - `GET /personsGroupByAge`: Group persons by age.
  - `GET /contestsOccupancy`: Contest capacity and occupancy.

- API (Assignment 7)
  - `POST /contestsRegistration?contestId={}`: Register team to contest. It takes a `Team` object as request body and creates a new team. It also creates students and coaches if their IDs are `NULL`.
  - `POST /setEditable?contestId={}`: Make contest writable.
  - `POST /setReadOnly?contestId={}`: Make contest read only.
  - `POST /editTeam`: It takes a `Team` object and updates the team if corresponding contest is writable.
  - `POST /editContest`: It takes a `Contest` object and updates the contest if it is writeable.
  - `POST /promote?superContestId={}&teamId={}`: Clone a team and register the cloned team to a contest.

- Postman collection: `Jui_TonniAssignment7.postman_collection.json`