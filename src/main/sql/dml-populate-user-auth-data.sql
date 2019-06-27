use followapp;

/* Populate user_role Table */
INSERT INTO user_role(role)
VALUES ('USER');
 
INSERT INTO user_role(role)
VALUES ('ADMIN');
 
INSERT INTO user_role(role)
VALUES ('DBA');

/**
 * clear text password is admin
 */
INSERT INTO user (username, password, first_name, last_name, email, enabled) 
VALUES ('admin', '$2a$10$BcfqCEgF.wGgWEVk712x2OyGO5J01Jyw9FIwIbZoRpM7LOp1jgsYu', 'admin', 'admin', 'admin@followapp.com', 1 ) ;
 
INSERT INTO user_to_role
VALUES (1, 2 ) ;


/* Populate one Admin user which will further create other users for the application using GUI */
/* Use BCryptPasswordEncoder to generated encrypted password
INSERT INTO user(username, password, first_name, last_name, email, enabled)
VALUES ('followapp','$2a$10$4eqIF5s/ewJwHK1p8lqlFOEm2QIA0S8g6./Lok.pQxqcxaBZYChRm', 'Sid','Rathod','siddharthrathod26193@gmail.com', true);
 
 
/* Assign roles to the user */
INSERT INTO user_to_role (user_id, user_role_id)
  SELECT user.id, profile.id FROM user user, user_role profile
  where user.username='followapp' and profile.role='ADMIN';

INSERT INTO user_to_role (user_id, user_role_id)
  SELECT user.id, profile.id FROM user user, user_role profile
  where user.username='followapp' and profile.role='USER';
  
select username,password,enabled from user where username='followapp';
select username, role from user u,user_role r, user_to_role ur where username='followapp' and u.id=ur.user_id and ur.user_role_id=r.id