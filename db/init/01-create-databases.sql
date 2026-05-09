-- Create a separate DB for Keycloak
SELECT 'CREATE DATABASE keycloak'
WHERE NOT EXISTS (SELECT
                  FROM pg_database
                  WHERE datname = 'keycloak')
\gexec

-- Create catalog DB for the app (Liquibase will manage its schema)
SELECT 'CREATE DATABASE catalog'
WHERE NOT EXISTS (SELECT
                  FROM pg_database
                  WHERE datname = 'catalog')
\gexec

-- Create inventory DB for the app (Liquibase will manage its schema)
SELECT 'CREATE DATABASE inventory'
WHERE NOT EXISTS (SELECT
                  FROM pg_database
                  WHERE datname = 'inventory')
\gexec

-- Create analytics DB for the app (Liquibase will manage its schema)
SELECT 'CREATE DATABASE analytics'
WHERE NOT EXISTS (SELECT
                  FROM pg_database
                  WHERE datname = 'analytics')
\gexec