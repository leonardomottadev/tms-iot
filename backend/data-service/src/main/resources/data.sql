INSERT INTO users (name, email, password, role)
VALUES (
    'leonardo',
    'leonardo@ucl.br',
    '$2a$12$YkxVWD.FVI0heCdvIgwWWetnIUzAbyCWYwUV7X6fAIXhQ.TVXPkzG',
    'ADMIN'
)
ON CONFLICT (email) DO NOTHING;
