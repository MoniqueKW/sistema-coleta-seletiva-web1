CREATE SCHEMA coleta_seletiva;

CREATE TABLE coleta_seletiva.morador (
	id SERIAL PRIMARY KEY,
	nome VARCHAR(150) NOT NULL,
	email VARCHAR(255) UNIQUE NOT NULL,
	telefone VARCHAR(20)
);

CREATE TABLE coleta_seletiva.material (
	id SERIAL PRIMARY KEY,
	nome VARCHAR(100) UNIQUE NOT NULL,
	descricao TEXT,
	unidade VARCHAR(20) NOT NULL,
	orientacao TEXT,
	situacao VARCHAR(20)
);

CREATE TABLE coleta_seletiva.pontocoleta (
	id SERIAL PRIMARY KEY,
	nome VARCHAR(255) NOT NULL,
	logradouro VARCHAR(150) NOT NULL,
	numero VARCHAR(20) NOT NULL,
	bairro VARCHAR(100) NOT NULL,
	municipio VARCHAR(100) NOT NULL,
	uf CHAR(2) NOT NULL,
	cep VARCHAR(10) NOT NULL,
	complemento VARCHAR(255),
	contato VARCHAR(255),
	horarios VARCHAR(255),
	situacao VARCHAR(20),
	recolhimento_domiciliar BOOLEAN
);

CREATE TABLE coleta_seletiva.endereco (
	id SERIAL PRIMARY KEY,
	id_morador INT REFERENCES coleta_seletiva.morador(id),
	logradouro VARCHAR(150) NOT NULL,
	numero VARCHAR(20) NOT NULL,
	bairro VARCHAR(100) NOT NULL,
	municipio VARCHAR(100) NOT NULL,
	uf CHAR(2) NOT NULL,
	cep VARCHAR(10) NOT NULL,
	complemento VARCHAR(255)
);

CREATE TABLE coleta_seletiva.operador (
	id SERIAL PRIMARY KEY,
	id_ponto INT REFERENCES coleta_seletiva.pontocoleta(id),
	nome VARCHAR(150) NOT NULL,
	email VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE coleta_seletiva.materialaceito (
	id_ponto INT REFERENCES coleta_seletiva.pontocoleta(id),
	id_material INT REFERENCES coleta_seletiva.material(id),
	observacoes TEXT,
	limite_opcional DECIMAL,
	PRIMARY KEY (id_ponto, id_material)
);

CREATE TABLE coleta_seletiva.solicitacao (
	id SERIAL PRIMARY KEY,
	id_ponto INT NOT NULL REFERENCES coleta_seletiva.pontocoleta(id),
	id_morador INT NOT NULL REFERENCES coleta_seletiva.morador(id),
	protocolo_unico VARCHAR(50) UNIQUE NOT NULL,
	copia_logradouro VARCHAR(150) NOT NULL,
	copia_numero VARCHAR(20) NOT NULL,
	copia_bairro VARCHAR(100) NOT NULL,
	copia_municipio VARCHAR(100) NOT NULL,
	copia_uf CHAR(2) NOT NULL,
	copia_cep VARCHAR(10) NOT NULL,
	copia_complemento VARCHAR(255),
	situacao VARCHAR(50) NOT NULL,
	data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	data_agendamento TIMESTAMP,
	data_conclusao TIMESTAMP,
	observacoes TEXT,
	versao INT NOT NULL DEFAULT 0
);

CREATE TABLE coleta_seletiva.itemsolicitacao (
	id_solicitacao INT NOT NULL REFERENCES coleta_seletiva.solicitacao (id),
	id_material INT NOT NULL REFERENCES coleta_seletiva.material(id),
	copia_unidade VARCHAR(20) NOT NULL,
	qtd_estimada DECIMAL NOT NULL CHECK (qtd_estimada>0),
	qtd_efetiva DECIMAL CHECK (qtd_efetiva>=0),
	PRIMARY KEY (id_solicitacao, id_material)
);

CREATE TABLE coleta_seletiva.historico (
	id_historico SERIAL PRIMARY KEY,
	id_solicitacao INT NOT NULL REFERENCES coleta_seletiva.solicitacao(id),
	situacao_anterior VARCHAR(50),
	situacao_nova VARCHAR(50) NOT NULL,
	data TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	responsavel VARCHAR(150) NOT NULL,
	motivo TEXT,
	info_tentativa TEXT
);