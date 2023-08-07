CREATE ROLE quarkus WITH LOGIN PASSWORD 'quarkus';
CREATE DATABASE creditcard_security_webauthn;
GRANT ALL PRIVILEGES ON DATABASE creditcard_security_webauthn TO quarkus;
\c creditcard_security_webauthn

