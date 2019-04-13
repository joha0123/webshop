# --- !Ups

create table "items" (
  "id" bigint generated by default as identity(start with 1) not null primary key,
  "name" varchar not null,
  "netto_price" double not null
);

# --- !Downs

drop table "items" if exists;