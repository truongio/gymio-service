CREATE TABLE workout
(
  id                    UUID PRIMARY KEY,
  user_id               UUID,
  data                  JSONB,
  timestamp             TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);