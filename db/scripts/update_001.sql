CREATE TABLE items (
 id SERIAL PRIMARY KEY,
 description TEXT,
 created TEXT,
 done BOOLEAN default false
);
