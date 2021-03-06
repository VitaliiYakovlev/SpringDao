-- DROP FOREIGN KEYS CONSTRAINTS
ALTER TABLE IF EXISTS albums DROP CONSTRAINT IF EXISTS fk_albums_genres;
ALTER TABLE IF EXISTS albums DROP CONSTRAINT IF EXISTS fk_albums_musicgroups;

-- DROP ALL TABLES AND SEQUENCE
DROP TABLE IF EXISTS genres CASCADE;
DROP TABLE IF EXISTS albums CASCADE;
DROP TABLE IF EXISTS music_groups CASCADE;
DROP SEQUENCE IF EXISTS base_sequence;

-- CREATE SEQUENCE
CREATE SEQUENCE base_sequence START 1 INCREMENT 1;

-- CREATE TABLES
CREATE TABLE genres(id INT8, name VARCHAR(127) NOT NULL, PRIMARY KEY (id));
CREATE TABLE music_groups(id INT8, name VARCHAR(127) NOT NULL, year_creation INT2 NOT NULL, year_decay INT2, PRIMARY KEY (id));
CREATE TABLE albums(id INT8, name VARCHAR(255) NOT NULL, year_release INT2 NOT NULL, music_group_id INT8 NOT NULL, genre_id INT8 NOT NULL, PRIMARY KEY (id));

-- ADD UNIQUE CONSTRAINTS
ALTER TABLE genres ADD CONSTRAINT uk_genres_name UNIQUE (name);
ALTER TABLE music_groups ADD CONSTRAINT uk_musicgroups_name UNIQUE (name);
ALTER TABLE albums ADD CONSTRAINT uk_albums_name_groupid UNIQUE (name, music_group_id);

-- ADD FOREIGN KEYS CONSTRAINTS
ALTER TABLE albums ADD CONSTRAINT fk_albums_genres FOREIGN KEY (genre_id) REFERENCES genres;
ALTER TABLE albums ADD CONSTRAINT fk_albums_musicgroups FOREIGN KEY (music_group_id) REFERENCES music_groups;