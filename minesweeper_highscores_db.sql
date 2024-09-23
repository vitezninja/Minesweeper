create database if not exists minesweeper_highscores_db;
use minesweeper_highscores_db;
create table minesweeper_highscores_easy (
name varchar(255),
time int
);
create table minesweeper_highscores_medium (
name varchar(255),
time int
);
create table minesweeper_highscores_hard (
name varchar(255),
time int
) 
