--
-- PostgreSQL database dump
--

-- Dumped from database version 18.6
-- Dumped by pg_dump version 18.6

-- Started on 2026-10-30 20:21:59

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 5 (class 2615 OID 33153)
-- Name: coleta_seletiva; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA coleta_seletiva;


SET default_table_access_method = heap;

--
-- TOC entry 228 (class 1259 OID 33198)
-- Name: endereco; Type: TABLE; Schema: coleta_seletiva; Owner: -
--

CREATE TABLE coleta_seletiva.endereco (
    id integer NOT NULL,
    id_morador integer NOT NULL,
    logradouro character varying(150) NOT NULL,
    numero character varying(20) NOT NULL,
    bairro character varying(100) NOT NULL,
    municipio character varying(100) NOT NULL,
    uf character varying(2) NOT NULL,
    cep character varying(10) NOT NULL,
    complemento character varying(255)
);


--
-- TOC entry 227 (class 1259 OID 33197)
-- Name: endereco_id_seq; Type: SEQUENCE; Schema: coleta_seletiva; Owner: -
--

CREATE SEQUENCE coleta_seletiva.endereco_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 5097 (class 0 OID 0)
-- Dependencies: 227
-- Name: endereco_id_seq; Type: SEQUENCE OWNED BY; Schema: coleta_seletiva; Owner: -
--

ALTER SEQUENCE coleta_seletiva.endereco_id_seq OWNED BY coleta_seletiva.endereco.id;


--
-- TOC entry 236 (class 1259 OID 33314)
-- Name: historico; Type: TABLE; Schema: coleta_seletiva; Owner: -
--

CREATE TABLE coleta_seletiva.historico (
    id_historico integer NOT NULL,
    id_solicitacao integer NOT NULL,
    situacao_anterior character varying(50),
    situacao_nova character varying(50) NOT NULL,
    data timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    responsavel character varying(150) NOT NULL,
    motivo text,
    info_tentativa text
);


--
-- TOC entry 235 (class 1259 OID 33313)
-- Name: historico_id_historico_seq; Type: SEQUENCE; Schema: coleta_seletiva; Owner: -
--

CREATE SEQUENCE coleta_seletiva.historico_id_historico_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 5098 (class 0 OID 0)
-- Dependencies: 235
-- Name: historico_id_historico_seq; Type: SEQUENCE OWNED BY; Schema: coleta_seletiva; Owner: -
--

ALTER SEQUENCE coleta_seletiva.historico_id_historico_seq OWNED BY coleta_seletiva.historico.id_historico;


--
-- TOC entry 234 (class 1259 OID 33290)
-- Name: itemsolicitacao; Type: TABLE; Schema: coleta_seletiva; Owner: -
--

CREATE TABLE coleta_seletiva.itemsolicitacao (
    id_solicitacao integer NOT NULL,
    id_material integer NOT NULL,
    copia_unidade character varying(20) NOT NULL,
    qtd_estimada numeric NOT NULL,
    qtd_efetiva numeric,
    CONSTRAINT itemsolicitacao_qtd_efetiva_check CHECK ((qtd_efetiva >= (0)::numeric)),
    CONSTRAINT itemsolicitacao_qtd_estimada_check CHECK ((qtd_estimada > (0)::numeric))
);


--
-- TOC entry 224 (class 1259 OID 33167)
-- Name: material; Type: TABLE; Schema: coleta_seletiva; Owner: -
--

CREATE TABLE coleta_seletiva.material (
    id integer NOT NULL,
    nome character varying(100) NOT NULL,
    descricao text,
    unidade character varying(20) NOT NULL,
    orientacao text,
    situacao character varying(20) NOT NULL
);


--
-- TOC entry 223 (class 1259 OID 33166)
-- Name: material_id_seq; Type: SEQUENCE; Schema: coleta_seletiva; Owner: -
--

CREATE SEQUENCE coleta_seletiva.material_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 5099 (class 0 OID 0)
-- Dependencies: 223
-- Name: material_id_seq; Type: SEQUENCE OWNED BY; Schema: coleta_seletiva; Owner: -
--

ALTER SEQUENCE coleta_seletiva.material_id_seq OWNED BY coleta_seletiva.material.id;


--
-- TOC entry 231 (class 1259 OID 33235)
-- Name: materialaceito; Type: TABLE; Schema: coleta_seletiva; Owner: -
--

CREATE TABLE coleta_seletiva.materialaceito (
    id_ponto integer NOT NULL,
    id_material integer NOT NULL,
    observacoes text,
    limite_opcional numeric
);


--
-- TOC entry 222 (class 1259 OID 33155)
-- Name: morador; Type: TABLE; Schema: coleta_seletiva; Owner: -
--

CREATE TABLE coleta_seletiva.morador (
    id integer NOT NULL,
    nome character varying(150) NOT NULL,
    email character varying(255) NOT NULL,
    telefone character varying(20)
);


--
-- TOC entry 221 (class 1259 OID 33154)
-- Name: morador_id_seq; Type: SEQUENCE; Schema: coleta_seletiva; Owner: -
--

CREATE SEQUENCE coleta_seletiva.morador_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 5100 (class 0 OID 0)
-- Dependencies: 221
-- Name: morador_id_seq; Type: SEQUENCE OWNED BY; Schema: coleta_seletiva; Owner: -
--

ALTER SEQUENCE coleta_seletiva.morador_id_seq OWNED BY coleta_seletiva.morador.id;


--
-- TOC entry 230 (class 1259 OID 33219)
-- Name: operador; Type: TABLE; Schema: coleta_seletiva; Owner: -
--

CREATE TABLE coleta_seletiva.operador (
    id integer NOT NULL,
    id_ponto integer,
    nome character varying(150) NOT NULL,
    email character varying(255) NOT NULL
);


--
-- TOC entry 229 (class 1259 OID 33218)
-- Name: operador_id_seq; Type: SEQUENCE; Schema: coleta_seletiva; Owner: -
--

CREATE SEQUENCE coleta_seletiva.operador_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 5101 (class 0 OID 0)
-- Dependencies: 229
-- Name: operador_id_seq; Type: SEQUENCE OWNED BY; Schema: coleta_seletiva; Owner: -
--

ALTER SEQUENCE coleta_seletiva.operador_id_seq OWNED BY coleta_seletiva.operador.id;


--
-- TOC entry 226 (class 1259 OID 33181)
-- Name: pontocoleta; Type: TABLE; Schema: coleta_seletiva; Owner: -
--

CREATE TABLE coleta_seletiva.pontocoleta (
    id integer NOT NULL,
    nome character varying(255) NOT NULL,
    logradouro character varying(150) NOT NULL,
    numero character varying(20) NOT NULL,
    bairro character varying(100) NOT NULL,
    municipio character varying(100) NOT NULL,
    uf character varying(2) NOT NULL,
    cep character varying(10) NOT NULL,
    complemento character varying(255),
    contato character varying(255),
    horarios character varying(255),
    situacao character varying(20) NOT NULL,
    recolhimento_domiciliar boolean NOT NULL
);


--
-- TOC entry 225 (class 1259 OID 33180)
-- Name: pontocoleta_id_seq; Type: SEQUENCE; Schema: coleta_seletiva; Owner: -
--

CREATE SEQUENCE coleta_seletiva.pontocoleta_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 5102 (class 0 OID 0)
-- Dependencies: 225
-- Name: pontocoleta_id_seq; Type: SEQUENCE OWNED BY; Schema: coleta_seletiva; Owner: -
--

ALTER SEQUENCE coleta_seletiva.pontocoleta_id_seq OWNED BY coleta_seletiva.pontocoleta.id;


--
-- TOC entry 233 (class 1259 OID 33255)
-- Name: solicitacao; Type: TABLE; Schema: coleta_seletiva; Owner: -
--

CREATE TABLE coleta_seletiva.solicitacao (
    id integer NOT NULL,
    id_ponto integer NOT NULL,
    id_morador integer NOT NULL,
    protocolo_unico character varying(50) NOT NULL,
    copia_logradouro character varying(150) NOT NULL,
    copia_numero character varying(20) NOT NULL,
    copia_bairro character varying(100) NOT NULL,
    copia_municipio character varying(100) NOT NULL,
    copia_uf character varying(2) NOT NULL,
    copia_cep character varying(10) NOT NULL,
    copia_complemento character varying(255),
    situacao character varying(50) NOT NULL,
    data_criacao timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    data_agendamento timestamp without time zone,
    data_conclusao timestamp without time zone,
    observacoes text,
    versao integer DEFAULT 0 NOT NULL
);


--
-- TOC entry 232 (class 1259 OID 33254)
-- Name: solicitacao_id_seq; Type: SEQUENCE; Schema: coleta_seletiva; Owner: -
--

CREATE SEQUENCE coleta_seletiva.solicitacao_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 5103 (class 0 OID 0)
-- Dependencies: 232
-- Name: solicitacao_id_seq; Type: SEQUENCE OWNED BY; Schema: coleta_seletiva; Owner: -
--

ALTER SEQUENCE coleta_seletiva.solicitacao_id_seq OWNED BY coleta_seletiva.solicitacao.id;


--
-- TOC entry 4901 (class 2604 OID 33201)
-- Name: endereco id; Type: DEFAULT; Schema: coleta_seletiva; Owner: -
--

ALTER TABLE ONLY coleta_seletiva.endereco ALTER COLUMN id SET DEFAULT nextval('coleta_seletiva.endereco_id_seq'::regclass);


--
-- TOC entry 4906 (class 2604 OID 33317)
-- Name: historico id_historico; Type: DEFAULT; Schema: coleta_seletiva; Owner: -
--

ALTER TABLE ONLY coleta_seletiva.historico ALTER COLUMN id_historico SET DEFAULT nextval('coleta_seletiva.historico_id_historico_seq'::regclass);


--
-- TOC entry 4899 (class 2604 OID 33170)
-- Name: material id; Type: DEFAULT; Schema: coleta_seletiva; Owner: -
--

ALTER TABLE ONLY coleta_seletiva.material ALTER COLUMN id SET DEFAULT nextval('coleta_seletiva.material_id_seq'::regclass);


--
-- TOC entry 4898 (class 2604 OID 33158)
-- Name: morador id; Type: DEFAULT; Schema: coleta_seletiva; Owner: -
--

ALTER TABLE ONLY coleta_seletiva.morador ALTER COLUMN id SET DEFAULT nextval('coleta_seletiva.morador_id_seq'::regclass);


--
-- TOC entry 4902 (class 2604 OID 33222)
-- Name: operador id; Type: DEFAULT; Schema: coleta_seletiva; Owner: -
--

ALTER TABLE ONLY coleta_seletiva.operador ALTER COLUMN id SET DEFAULT nextval('coleta_seletiva.operador_id_seq'::regclass);


--
-- TOC entry 4900 (class 2604 OID 33184)
-- Name: pontocoleta id; Type: DEFAULT; Schema: coleta_seletiva; Owner: -
--

ALTER TABLE ONLY coleta_seletiva.pontocoleta ALTER COLUMN id SET DEFAULT nextval('coleta_seletiva.pontocoleta_id_seq'::regclass);


--
-- TOC entry 4903 (class 2604 OID 33258)
-- Name: solicitacao id; Type: DEFAULT; Schema: coleta_seletiva; Owner: -
--

ALTER TABLE ONLY coleta_seletiva.solicitacao ALTER COLUMN id SET DEFAULT nextval('coleta_seletiva.solicitacao_id_seq'::regclass);


--
-- TOC entry 4921 (class 2606 OID 33212)
-- Name: endereco endereco_pkey; Type: CONSTRAINT; Schema: coleta_seletiva; Owner: -
--

ALTER TABLE ONLY coleta_seletiva.endereco
    ADD CONSTRAINT endereco_pkey PRIMARY KEY (id);


--
-- TOC entry 4935 (class 2606 OID 33327)
-- Name: historico historico_pkey; Type: CONSTRAINT; Schema: coleta_seletiva; Owner: -
--

ALTER TABLE ONLY coleta_seletiva.historico
    ADD CONSTRAINT historico_pkey PRIMARY KEY (id_historico);


--
-- TOC entry 4933 (class 2606 OID 33302)
-- Name: itemsolicitacao itemsolicitacao_pkey; Type: CONSTRAINT; Schema: coleta_seletiva; Owner: -
--

ALTER TABLE ONLY coleta_seletiva.itemsolicitacao
    ADD CONSTRAINT itemsolicitacao_pkey PRIMARY KEY (id_solicitacao, id_material);


--
-- TOC entry 4915 (class 2606 OID 33179)
-- Name: material material_nome_key; Type: CONSTRAINT; Schema: coleta_seletiva; Owner: -
--

ALTER TABLE ONLY coleta_seletiva.material
    ADD CONSTRAINT material_nome_key UNIQUE (nome);


--
-- TOC entry 4917 (class 2606 OID 33177)
-- Name: material material_pkey; Type: CONSTRAINT; Schema: coleta_seletiva; Owner: -
--

ALTER TABLE ONLY coleta_seletiva.material
    ADD CONSTRAINT material_pkey PRIMARY KEY (id);


--
-- TOC entry 4927 (class 2606 OID 33243)
-- Name: materialaceito materialaceito_pkey; Type: CONSTRAINT; Schema: coleta_seletiva; Owner: -
--

ALTER TABLE ONLY coleta_seletiva.materialaceito
    ADD CONSTRAINT materialaceito_pkey PRIMARY KEY (id_ponto, id_material);


--
-- TOC entry 4911 (class 2606 OID 33165)
-- Name: morador morador_email_key; Type: CONSTRAINT; Schema: coleta_seletiva; Owner: -
--

ALTER TABLE ONLY coleta_seletiva.morador
    ADD CONSTRAINT morador_email_key UNIQUE (email);


--
-- TOC entry 4913 (class 2606 OID 33163)
-- Name: morador morador_pkey; Type: CONSTRAINT; Schema: coleta_seletiva; Owner: -
--

ALTER TABLE ONLY coleta_seletiva.morador
    ADD CONSTRAINT morador_pkey PRIMARY KEY (id);


--
-- TOC entry 4923 (class 2606 OID 33229)
-- Name: operador operador_email_key; Type: CONSTRAINT; Schema: coleta_seletiva; Owner: -
--

ALTER TABLE ONLY coleta_seletiva.operador
    ADD CONSTRAINT operador_email_key UNIQUE (email);


--
-- TOC entry 4925 (class 2606 OID 33227)
-- Name: operador operador_pkey; Type: CONSTRAINT; Schema: coleta_seletiva; Owner: -
--

ALTER TABLE ONLY coleta_seletiva.operador
    ADD CONSTRAINT operador_pkey PRIMARY KEY (id);


--
-- TOC entry 4919 (class 2606 OID 33196)
-- Name: pontocoleta pontocoleta_pkey; Type: CONSTRAINT; Schema: coleta_seletiva; Owner: -
--

ALTER TABLE ONLY coleta_seletiva.pontocoleta
    ADD CONSTRAINT pontocoleta_pkey PRIMARY KEY (id);


--
-- TOC entry 4929 (class 2606 OID 33277)
-- Name: solicitacao solicitacao_pkey; Type: CONSTRAINT; Schema: coleta_seletiva; Owner: -
--

ALTER TABLE ONLY coleta_seletiva.solicitacao
    ADD CONSTRAINT solicitacao_pkey PRIMARY KEY (id);


--
-- TOC entry 4931 (class 2606 OID 33279)
-- Name: solicitacao solicitacao_protocolo_unico_key; Type: CONSTRAINT; Schema: coleta_seletiva; Owner: -
--

ALTER TABLE ONLY coleta_seletiva.solicitacao
    ADD CONSTRAINT solicitacao_protocolo_unico_key UNIQUE (protocolo_unico);


--
-- TOC entry 4936 (class 2606 OID 33213)
-- Name: endereco endereco_id_morador_fkey; Type: FK CONSTRAINT; Schema: coleta_seletiva; Owner: -
--

ALTER TABLE ONLY coleta_seletiva.endereco
    ADD CONSTRAINT endereco_id_morador_fkey FOREIGN KEY (id_morador) REFERENCES coleta_seletiva.morador(id);


--
-- TOC entry 4944 (class 2606 OID 33328)
-- Name: historico historico_id_solicitacao_fkey; Type: FK CONSTRAINT; Schema: coleta_seletiva; Owner: -
--

ALTER TABLE ONLY coleta_seletiva.historico
    ADD CONSTRAINT historico_id_solicitacao_fkey FOREIGN KEY (id_solicitacao) REFERENCES coleta_seletiva.solicitacao(id);


--
-- TOC entry 4942 (class 2606 OID 33308)
-- Name: itemsolicitacao itemsolicitacao_id_material_fkey; Type: FK CONSTRAINT; Schema: coleta_seletiva; Owner: -
--

ALTER TABLE ONLY coleta_seletiva.itemsolicitacao
    ADD CONSTRAINT itemsolicitacao_id_material_fkey FOREIGN KEY (id_material) REFERENCES coleta_seletiva.material(id);


--
-- TOC entry 4943 (class 2606 OID 33303)
-- Name: itemsolicitacao itemsolicitacao_id_solicitacao_fkey; Type: FK CONSTRAINT; Schema: coleta_seletiva; Owner: -
--

ALTER TABLE ONLY coleta_seletiva.itemsolicitacao
    ADD CONSTRAINT itemsolicitacao_id_solicitacao_fkey FOREIGN KEY (id_solicitacao) REFERENCES coleta_seletiva.solicitacao(id);


--
-- TOC entry 4938 (class 2606 OID 33249)
-- Name: materialaceito materialaceito_id_material_fkey; Type: FK CONSTRAINT; Schema: coleta_seletiva; Owner: -
--

ALTER TABLE ONLY coleta_seletiva.materialaceito
    ADD CONSTRAINT materialaceito_id_material_fkey FOREIGN KEY (id_material) REFERENCES coleta_seletiva.material(id);


--
-- TOC entry 4939 (class 2606 OID 33244)
-- Name: materialaceito materialaceito_id_ponto_fkey; Type: FK CONSTRAINT; Schema: coleta_seletiva; Owner: -
--

ALTER TABLE ONLY coleta_seletiva.materialaceito
    ADD CONSTRAINT materialaceito_id_ponto_fkey FOREIGN KEY (id_ponto) REFERENCES coleta_seletiva.pontocoleta(id);


--
-- TOC entry 4937 (class 2606 OID 33230)
-- Name: operador operador_id_ponto_fkey; Type: FK CONSTRAINT; Schema: coleta_seletiva; Owner: -
--

ALTER TABLE ONLY coleta_seletiva.operador
    ADD CONSTRAINT operador_id_ponto_fkey FOREIGN KEY (id_ponto) REFERENCES coleta_seletiva.pontocoleta(id);


--
-- TOC entry 4940 (class 2606 OID 33285)
-- Name: solicitacao solicitacao_id_morador_fkey; Type: FK CONSTRAINT; Schema: coleta_seletiva; Owner: -
--

ALTER TABLE ONLY coleta_seletiva.solicitacao
    ADD CONSTRAINT solicitacao_id_morador_fkey FOREIGN KEY (id_morador) REFERENCES coleta_seletiva.morador(id);


--
-- TOC entry 4941 (class 2606 OID 33280)
-- Name: solicitacao solicitacao_id_ponto_fkey; Type: FK CONSTRAINT; Schema: coleta_seletiva; Owner: -
--

ALTER TABLE ONLY coleta_seletiva.solicitacao
    ADD CONSTRAINT solicitacao_id_ponto_fkey FOREIGN KEY (id_ponto) REFERENCES coleta_seletiva.pontocoleta(id);


-- Completed on 2026-10-30 20:21:59

--
-- PostgreSQL database dump complete
--

