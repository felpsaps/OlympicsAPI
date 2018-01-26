--
-- PostgreSQL database dump
--

-- Dumped from database version 10.1
-- Dumped by pg_dump version 10.0

-- Started on 2018-01-25 00:46:27

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 2848 (class 1262 OID 25169)
-- Name: olympics; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE olympics WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'Portuguese_Brazil.1252' LC_CTYPE = 'Portuguese_Brazil.1252';


ALTER DATABASE olympics OWNER TO postgres;

\connect olympics

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 1 (class 3079 OID 12924)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2850 (class 0 OID 0)
-- Dependencies: 1
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 205 (class 1259 OID 25220)
-- Name: competiton; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE competiton (
    id integer NOT NULL,
    inidate timestamp with time zone,
    enddate timestamp with time zone,
    country1_id integer,
    country2_id integer,
    sport_id integer,
    local_id integer,
    step_id integer
);


ALTER TABLE competiton OWNER TO postgres;

--
-- TOC entry 204 (class 1259 OID 25218)
-- Name: competiton_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE competiton_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE competiton_id_seq OWNER TO postgres;

--
-- TOC entry 2851 (class 0 OID 0)
-- Dependencies: 204
-- Name: competiton_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE competiton_id_seq OWNED BY competiton.id;


--
-- TOC entry 201 (class 1259 OID 25194)
-- Name: country; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE country (
    id integer NOT NULL,
    country text
);


ALTER TABLE country OWNER TO postgres;

--
-- TOC entry 200 (class 1259 OID 25192)
-- Name: country_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE country_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE country_id_seq OWNER TO postgres;

--
-- TOC entry 2852 (class 0 OID 0)
-- Dependencies: 200
-- Name: country_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE country_id_seq OWNED BY country.id;


--
-- TOC entry 197 (class 1259 OID 25172)
-- Name: local; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE local (
    id integer NOT NULL,
    local text
);


ALTER TABLE local OWNER TO postgres;

--
-- TOC entry 196 (class 1259 OID 25170)
-- Name: local_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE local_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE local_id_seq OWNER TO postgres;

--
-- TOC entry 2853 (class 0 OID 0)
-- Dependencies: 196
-- Name: local_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE local_id_seq OWNED BY local.id;


--
-- TOC entry 199 (class 1259 OID 25183)
-- Name: sport; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE sport (
    id integer NOT NULL,
    sport text
);


ALTER TABLE sport OWNER TO postgres;

--
-- TOC entry 198 (class 1259 OID 25181)
-- Name: sport_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE sport_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE sport_id_seq OWNER TO postgres;

--
-- TOC entry 2854 (class 0 OID 0)
-- Dependencies: 198
-- Name: sport_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE sport_id_seq OWNED BY sport.id;


--
-- TOC entry 203 (class 1259 OID 25205)
-- Name: step; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE step (
    id integer NOT NULL,
    step text
);


ALTER TABLE step OWNER TO postgres;

--
-- TOC entry 202 (class 1259 OID 25203)
-- Name: step_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE step_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE step_id_seq OWNER TO postgres;

--
-- TOC entry 2855 (class 0 OID 0)
-- Dependencies: 202
-- Name: step_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE step_id_seq OWNED BY step.id;


--
-- TOC entry 2702 (class 2604 OID 25223)
-- Name: competiton id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY competiton ALTER COLUMN id SET DEFAULT nextval('competiton_id_seq'::regclass);


--
-- TOC entry 2700 (class 2604 OID 25197)
-- Name: country id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY country ALTER COLUMN id SET DEFAULT nextval('country_id_seq'::regclass);


--
-- TOC entry 2698 (class 2604 OID 25175)
-- Name: local id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY local ALTER COLUMN id SET DEFAULT nextval('local_id_seq'::regclass);


--
-- TOC entry 2699 (class 2604 OID 25186)
-- Name: sport id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sport ALTER COLUMN id SET DEFAULT nextval('sport_id_seq'::regclass);


--
-- TOC entry 2701 (class 2604 OID 25208)
-- Name: step id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY step ALTER COLUMN id SET DEFAULT nextval('step_id_seq'::regclass);


--
-- TOC entry 2843 (class 0 OID 25220)
-- Dependencies: 205
-- Data for Name: competiton; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2839 (class 0 OID 25194)
-- Dependencies: 201
-- Data for Name: country; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO country (id, country) VALUES (1, 'Brasil');
INSERT INTO country (id, country) VALUES (2, 'EUA');


--
-- TOC entry 2835 (class 0 OID 25172)
-- Dependencies: 197
-- Data for Name: local; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO local (id, local) VALUES (1, 'Local 1');


--
-- TOC entry 2837 (class 0 OID 25183)
-- Dependencies: 199
-- Data for Name: sport; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO sport (id, sport) VALUES (1, 'Futebol');
INSERT INTO sport (id, sport) VALUES (2, 'Basquete');


--
-- TOC entry 2841 (class 0 OID 25205)
-- Dependencies: 203
-- Data for Name: step; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO step (id, step) VALUES (1, 'Eliminatórias');
INSERT INTO step (id, step) VALUES (2, 'Oitavas de Final');
INSERT INTO step (id, step) VALUES (3, 'Quartas de Final');
INSERT INTO step (id, step) VALUES (4, 'Semi Final');
INSERT INTO step (id, step) VALUES (5, 'Final');


--
-- TOC entry 2856 (class 0 OID 0)
-- Dependencies: 204
-- Name: competiton_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('competiton_id_seq', 1, false);


--
-- TOC entry 2857 (class 0 OID 0)
-- Dependencies: 200
-- Name: country_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('country_id_seq', 2, true);


--
-- TOC entry 2858 (class 0 OID 0)
-- Dependencies: 196
-- Name: local_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('local_id_seq', 1, true);


--
-- TOC entry 2859 (class 0 OID 0)
-- Dependencies: 198
-- Name: sport_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('sport_id_seq', 2, true);


--
-- TOC entry 2860 (class 0 OID 0)
-- Dependencies: 202
-- Name: step_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('step_id_seq', 5, true);


--
-- TOC entry 2712 (class 2606 OID 25225)
-- Name: competiton competiton_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY competiton
    ADD CONSTRAINT competiton_pkey PRIMARY KEY (id);


--
-- TOC entry 2708 (class 2606 OID 25202)
-- Name: country country_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY country
    ADD CONSTRAINT country_pkey PRIMARY KEY (id);


--
-- TOC entry 2704 (class 2606 OID 25180)
-- Name: local local_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY local
    ADD CONSTRAINT local_pkey PRIMARY KEY (id);


--
-- TOC entry 2706 (class 2606 OID 25191)
-- Name: sport sport_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sport
    ADD CONSTRAINT sport_pkey PRIMARY KEY (id);


--
-- TOC entry 2710 (class 2606 OID 25213)
-- Name: step step_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY step
    ADD CONSTRAINT step_pkey PRIMARY KEY (id);


-- Completed on 2018-01-25 00:46:28

--
-- PostgreSQL database dump complete
--

