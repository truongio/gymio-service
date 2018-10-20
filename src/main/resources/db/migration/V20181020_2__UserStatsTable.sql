CREATE TABLE user_stats
(
  id                    UUID PRIMARY KEY,
  data                  JSONB,
  timestamp             TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);