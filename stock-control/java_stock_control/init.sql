CREATE DATABASE stock;
GRANT ALL PRIVILEGES ON DATABASE stock TO stock_admin;
\connect stock;
CREATE EXTENSION IF NOT EXISTS pgcrypto;
CREATE SCHEMA IF NOT EXISTS public;

CREATE TABLE public.users (
	id SERIAL,
	"name" varchar NOT NULL,
	password varchar NOT NULL,
	CONSTRAINT users_pk PRIMARY KEY (id)
);

CREATE TABLE public.providers (
	id SERIAL,
	"name" varchar NOT NULL,
	cuit varchar NULL,
	CONSTRAINT providers_pk PRIMARY KEY (id)
);

CREATE TABLE public.products (
	id SERIAL,
	code varchar NOT NULL,
	"name" varchar NOT NULL,
	amount int NULL,
	expiry_date varchar NOT NULL,
	"date" varchar NOT NULL,
	user_id bigint NOT NULL,
	provider_id bigint NOT NULL,
	CONSTRAINT products_pk PRIMARY KEY (id),
	CONSTRAINT users_fk FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT providers_fk FOREIGN KEY (provider_id) REFERENCES public.providers(id) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO public.users ("name", password) VALUES ('admin', crypt('admin', gen_salt('bf')));