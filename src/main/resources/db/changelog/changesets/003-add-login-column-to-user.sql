ALTER TABLE IF EXISTS person
ADD COLUMN login varchar(256) unique not null;