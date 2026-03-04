-- meant for resetting the database during testing. Please ignore if this ended up in the submission!

drop database if exists example_dbb;

----------------------------------

create database if not exists example_dbb;
use example_dbb;

create table if not exists `users`(
    `username` varchar(20) primary key,
    `pw` varchar(20) not null,
    `fname` varchar(20),
    `lname` varchar(20),
    `title` varchar(10)
);
create table if not exists `team`(
    `team_id` int primary key,
    `user1` varchar(20) null,
    `user2` varchar(20) null,
    `user3` varchar(20) null,
    foreign key (`user1`) references `users`(`username`)
        on delete set null
        on update cascade,
    foreign key (`user2`) references `users`(`username`)
        on delete set null
        on update cascade,
    foreign key (`user3`) references `users`(`username`)
        on delete set null
        on update cascade
);
create table if not exists `db`(
    `db_id` int auto_increment primary key,
    `team_id` int not null,
    `db_name` varchar(36) not null,
    `db_description` varchar(2000) not null,
    `last_updated` datetime,
    foreign key (`team_id`) references `team`(`team_id`)
        on delete cascade
        on update cascade
);
create table if not exists `changelog`(
    `change_id` int auto_increment primary key,
    `username` varchar(20) not null,
    `db_id` int not null,
    `change_description` varchar(2000) not null,
    `time_of_change` datetime not null,
    foreign key (`username`) references `users`(`username`)
        on delete cascade
        on update cascade,
    foreign key (`db_id`) references `db`(`db_id`)
        on delete cascade
        on update cascade
);
create table if not exists `grades`(
    `db_id` int primary key,
    `grade` int,
    foreign key (`db_id`) references `db`(`db_id`)
        on delete cascade
        on update cascade
);
create table if not exists permissions(
    `username` varchar(20) primary key,
    `perm_level` int not null,
    foreign key (`username`) references `users`(`username`)
        on delete cascade
        on update cascade
);

----------------------------------


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