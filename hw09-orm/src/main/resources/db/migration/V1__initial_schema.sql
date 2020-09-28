create table user
(
    id   bigint(20) NOT NULL AUTO_INCREMENT,
    name varchar(255),
    age  int(3)
);

insert into user (name, age)
values ('Sergey', 28);

create table account
(
    no   bigint(20) NOT NULL AUTO_INCREMENT,
    type varchar(255),
    rest number
);

insert into account (type, rest)
values ('debit', 23.45);