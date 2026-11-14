#!/usr/bin/env bash
set -euo pipefail

create_database() {
    local database="$1"
    local username="$2"
    local password="$3"

    psql --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" \
        --set=ON_ERROR_STOP=1 \
        --set=db="$database" \
        --set=role="$username" \
        --set=password="$password" <<'SQL'
CREATE ROLE :"role" LOGIN PASSWORD :'password'
    NOSUPERUSER NOCREATEDB NOCREATEROLE NOREPLICATION;
CREATE DATABASE :"db" OWNER :"role";
REVOKE ALL ON DATABASE :"db" FROM PUBLIC;
GRANT CONNECT ON DATABASE :"db" TO :"role";
SQL
}

create_database "$TRANSFER_DB_NAME" "$TRANSFER_DB_USER" "$TRANSFER_DB_PASSWORD"
create_database "$FINANCE_DB_NAME" "$FINANCE_DB_USER" "$FINANCE_DB_PASSWORD"
create_database "$RISK_DB_NAME" "$RISK_DB_USER" "$RISK_DB_PASSWORD"
create_database "$NOTIFICATION_DB_NAME" "$NOTIFICATION_DB_USER" "$NOTIFICATION_DB_PASSWORD"
