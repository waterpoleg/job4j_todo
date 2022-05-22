CREATE TABLE users (
  id SERIAL PRIMARY KEY,
  name TEXT,
  email VARCHAR UNIQUE,
  password TEXT
);

CREATE TABLE items (
 id SERIAL PRIMARY KEY,
 name TEXT,
 description TEXT,
 created TIMESTAMP,
 done BOOLEAN default false,
 user_id int not null references users(id)
);
