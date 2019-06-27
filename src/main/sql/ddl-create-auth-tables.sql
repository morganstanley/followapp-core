use followapp;

/* Users to role mapping*/  
CREATE TABLE user_to_role (
    user_id BIGINT NOT NULL,
    user_role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, user_role_id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES user (id),
    CONSTRAINT fk_user_role FOREIGN KEY (user_role_id) REFERENCES user_role (id)
);