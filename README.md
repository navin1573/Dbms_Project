# MusicManager – Java Swing + MySQL Project

## Overview
MusicManager is a Java Swing-based desktop application that connects to a MySQL database to manage a collection of music records. Users can add, view, update, delete, and search songs by name, artist, and genre.

## Features
- Add music entries
- View all records in a table
- Edit or delete selected records
- Search music by name

## Technologies Used
- Java Swing (for GUI)
- JDBC (for database connectivity)
- MySQL (as the database)

## Database Setup
Database: `music`  
Table: `misc`

```sql
CREATE TABLE misc (
  id INT PRIMARY KEY AUTO_INCREMENT,
  music_name VARCHAR(100),
  artist VARCHAR(100),
  genre VARCHAR(50)
);

How to Run

1. Start MySQL and create the music database with the misc table.


2. Update the database URL, username, and password in the code:

DriverManager.getConnection("jdbc:mysql://localhost:3306/music", "root", "dbms");


3. Compile and run the MusicManager.java file using any Java IDE or terminal.





