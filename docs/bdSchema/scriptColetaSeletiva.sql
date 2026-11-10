-- Sistema de Coleta Seletiva - estrutura do banco (somente referência).
-- Não executar este script antes de subir a aplicação: o Flyway criaria o schema de novo e daria erro.

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

CREATE SCHEMA coleta_seletiva;

SET default_tablespace = '';

SET default_table_access_method = heap;

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

CREATE SEQUENCE coleta_seletiva.endereco_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE coleta_seletiva.endereco_id_seq OWNED BY coleta_seletiva.endereco.id;

CREATE TABLE coleta_seletiva.historico (
    id_historico integer NOT NULL,
    id_solicitacao integer NOT NULL,
    situacao_anterior character varying(50),
    situacao_nova character varying(50) NOT NULL,
    data timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    responsavel character varying(150) NOT NULL,
    motivo text,
    info_tentativa text,
    id_operador integer
);

CREATE SEQUENCE coleta_seletiva.historico_id_historico_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE coleta_seletiva.historico_id_historico_seq OWNED BY coleta_seletiva.historico.id_historico;

CREATE TABLE coleta_seletiva.itemsolicitacao (
    id_solicitacao integer NOT NULL,
    id_material integer NOT NULL,
    copia_unidade character varying(20) NOT NULL,
    qtd_estimada numeric NOT NULL,
    qtd_efetiva numeric,
    CONSTRAINT itemsolicitacao_qtd_efetiva_check CHECK ((qtd_efetiva >= (0)::numeric)),
    CONSTRAINT itemsolicitacao_qtd_estimada_check CHECK ((qtd_estimada > (0)::numeric))
);

CREATE TABLE coleta_seletiva.material (
    id integer NOT NULL,
    nome character varying(100) NOT NULL,
    descricao text,
    unidade character varying(20) NOT NULL,
    orientacao text,
    situacao character varying(20) NOT NULL
);

CREATE SEQUENCE coleta_seletiva.material_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE coleta_seletiva.material_id_seq OWNED BY coleta_seletiva.material.id;

CREATE TABLE coleta_seletiva.materialaceito (
    id_ponto integer NOT NULL,
    id_material integer NOT NULL,
    observacoes text,
    limite_opcional numeric
);

CREATE TABLE coleta_seletiva.morador (
    id integer NOT NULL,
    nome character varying(150) NOT NULL,
    email character varying(255) NOT NULL,
    telefone character varying(20)
);

CREATE SEQUENCE coleta_seletiva.morador_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE coleta_seletiva.morador_id_seq OWNED BY coleta_seletiva.morador.id;

CREATE TABLE coleta_seletiva.operador (
    id integer NOT NULL,
    id_ponto integer,
    nome character varying(150) NOT NULL,
    email character varying(255) NOT NULL
);

CREATE SEQUENCE coleta_seletiva.operador_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE coleta_seletiva.operador_id_seq OWNED BY coleta_seletiva.operador.id;

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
    contato character varying(20),
    horarios character varying(255),
    situacao character varying(20) NOT NULL,
    recolhimento_domiciliar boolean NOT NULL
);

CREATE SEQUENCE coleta_seletiva.pontocoleta_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE coleta_seletiva.pontocoleta_id_seq OWNED BY coleta_seletiva.pontocoleta.id;

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
    data_criacao timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    data_agendamento timestamp with time zone,
    data_conclusao timestamp with time zone,
    observacoes text,
    versao integer DEFAULT 0 NOT NULL,
    recolhimento_domiciliar boolean DEFAULT false NOT NULL
);

CREATE SEQUENCE coleta_seletiva.solicitacao_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE coleta_seletiva.solicitacao_id_seq OWNED BY coleta_seletiva.solicitacao.id;

ALTER TABLE ONLY coleta_seletiva.endereco ALTER COLUMN id SET DEFAULT nextval('coleta_seletiva.endereco_id_seq'::regclass);

ALTER TABLE ONLY coleta_seletiva.historico ALTER COLUMN id_historico SET DEFAULT nextval('coleta_seletiva.historico_id_historico_seq'::regclass);

ALTER TABLE ONLY coleta_seletiva.material ALTER COLUMN id SET DEFAULT nextval('coleta_seletiva.material_id_seq'::regclass);

ALTER TABLE ONLY coleta_seletiva.morador ALTER COLUMN id SET DEFAULT nextval('coleta_seletiva.morador_id_seq'::regclass);

ALTER TABLE ONLY coleta_seletiva.operador ALTER COLUMN id SET DEFAULT nextval('coleta_seletiva.operador_id_seq'::regclass);

ALTER TABLE ONLY coleta_seletiva.pontocoleta ALTER COLUMN id SET DEFAULT nextval('coleta_seletiva.pontocoleta_id_seq'::regclass);

ALTER TABLE ONLY coleta_seletiva.solicitacao ALTER COLUMN id SET DEFAULT nextval('coleta_seletiva.solicitacao_id_seq'::regclass);

ALTER TABLE ONLY coleta_seletiva.endereco
    ADD CONSTRAINT endereco_pkey PRIMARY KEY (id);

ALTER TABLE ONLY coleta_seletiva.historico
    ADD CONSTRAINT historico_pkey PRIMARY KEY (id_historico);

ALTER TABLE ONLY coleta_seletiva.itemsolicitacao
    ADD CONSTRAINT itemsolicitacao_pkey PRIMARY KEY (id_solicitacao, id_material);

ALTER TABLE ONLY coleta_seletiva.material
    ADD CONSTRAINT material_nome_key UNIQUE (nome);

ALTER TABLE ONLY coleta_seletiva.material
    ADD CONSTRAINT material_pkey PRIMARY KEY (id);

ALTER TABLE ONLY coleta_seletiva.materialaceito
    ADD CONSTRAINT materialaceito_pkey PRIMARY KEY (id_ponto, id_material);

ALTER TABLE ONLY coleta_seletiva.morador
    ADD CONSTRAINT morador_email_key UNIQUE (email);

ALTER TABLE ONLY coleta_seletiva.morador
    ADD CONSTRAINT morador_pkey PRIMARY KEY (id);

ALTER TABLE ONLY coleta_seletiva.operador
    ADD CONSTRAINT operador_email_key UNIQUE (email);

ALTER TABLE ONLY coleta_seletiva.operador
    ADD CONSTRAINT operador_pkey PRIMARY KEY (id);

ALTER TABLE ONLY coleta_seletiva.pontocoleta
    ADD CONSTRAINT pontocoleta_pkey PRIMARY KEY (id);

ALTER TABLE ONLY coleta_seletiva.solicitacao
    ADD CONSTRAINT solicitacao_pkey PRIMARY KEY (id);

ALTER TABLE ONLY coleta_seletiva.solicitacao
    ADD CONSTRAINT solicitacao_protocolo_unico_key UNIQUE (protocolo_unico);

ALTER TABLE ONLY coleta_seletiva.endereco
    ADD CONSTRAINT endereco_id_morador_fkey FOREIGN KEY (id_morador) REFERENCES coleta_seletiva.morador(id);

ALTER TABLE ONLY coleta_seletiva.historico
    ADD CONSTRAINT fk_historico_operador FOREIGN KEY (id_operador) REFERENCES coleta_seletiva.operador(id);

ALTER TABLE ONLY coleta_seletiva.historico
    ADD CONSTRAINT historico_id_solicitacao_fkey FOREIGN KEY (id_solicitacao) REFERENCES coleta_seletiva.solicitacao(id);

ALTER TABLE ONLY coleta_seletiva.itemsolicitacao
    ADD CONSTRAINT itemsolicitacao_id_material_fkey FOREIGN KEY (id_material) REFERENCES coleta_seletiva.material(id);

ALTER TABLE ONLY coleta_seletiva.itemsolicitacao
    ADD CONSTRAINT itemsolicitacao_id_solicitacao_fkey FOREIGN KEY (id_solicitacao) REFERENCES coleta_seletiva.solicitacao(id);

ALTER TABLE ONLY coleta_seletiva.materialaceito
    ADD CONSTRAINT materialaceito_id_material_fkey FOREIGN KEY (id_material) REFERENCES coleta_seletiva.material(id);

ALTER TABLE ONLY coleta_seletiva.materialaceito
    ADD CONSTRAINT materialaceito_id_ponto_fkey FOREIGN KEY (id_ponto) REFERENCES coleta_seletiva.pontocoleta(id);

ALTER TABLE ONLY coleta_seletiva.operador
    ADD CONSTRAINT operador_id_ponto_fkey FOREIGN KEY (id_ponto) REFERENCES coleta_seletiva.pontocoleta(id);

ALTER TABLE ONLY coleta_seletiva.solicitacao
    ADD CONSTRAINT solicitacao_id_morador_fkey FOREIGN KEY (id_morador) REFERENCES coleta_seletiva.morador(id);

ALTER TABLE ONLY coleta_seletiva.solicitacao
    ADD CONSTRAINT solicitacao_id_ponto_fkey FOREIGN KEY (id_ponto) REFERENCES coleta_seletiva.pontocoleta(id);
