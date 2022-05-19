CREATE TABLE items (
 id SERIAL PRIMARY KEY,
 name TEXT,
 description TEXT,
 created TIMESTAMP,
 done BOOLEAN default false,
 user_id int not null referecces usdrs(id)
);
