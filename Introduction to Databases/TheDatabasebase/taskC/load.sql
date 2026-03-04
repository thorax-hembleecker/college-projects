load data local infile '/home/example/public_html/dbb/taskC/users.dat' into table example_dbb.users

    fields terminated by ','
    optionally enclosed by '"'
    lines terminated by '\n'
    (username, pw, fname, lname, title);

load data local infile '/home/example/public_html/dbb/taskC/team.dat' into table example_dbb.team
    fields terminated by ','
    optionally enclosed by '"'
    lines terminated by '\n'
    (team_id, user1, user2, user3);

load data local infile '/home/example/public_html/dbb/taskC/db.dat' into table example_dbb.db
    fields terminated by ','
    optionally enclosed by '"'
    lines terminated by '\n'
    (db_id, team_id, db_name, db_description, last_updated);

load data local infile '/home/example/public_html/dbb/taskC/changelog.dat' into table example_dbb.changelog
    fields terminated by ','
    optionally enclosed by '"'
    lines terminated by '\n'
    (change_id, username, db_id, change_description, time_of_change);

load data local infile '/home/example/public_html/dbb/taskC/grades.dat' into table example_dbb.grades
    fields terminated by ','
    optionally enclosed by '"'
    lines terminated by '\n'
    (db_id, grade);

load data local infile '/home/example/public_html/dbb/taskC/permissions.dat' into table example_dbb.permissions
    fields terminated by ','
    optionally enclosed by '"'
    lines terminated by '\n'
    (username, perm_level);