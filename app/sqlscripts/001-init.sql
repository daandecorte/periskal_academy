create table if not exists test (
    id int auto_increment primary key,
    description varchar(255) not null
);

insert into test (description) values ("dit is een test");