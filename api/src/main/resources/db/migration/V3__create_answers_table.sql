CREATE TABLE answers (
    id BIGSERIAL PRIMARY KEY,
    text VARCHAR(500) NOT NULL,
    creation_date TIMESTAMP NOT NULL,
    user_id BIGINT NOT NULL,
    topic_id BIGINT NOT NULL,

    CONSTRAINT fk_answers_user_id FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_answers_topic_id FOREIGN KEY (topic_id) REFERENCES topics(id)
);