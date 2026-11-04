ALTER TABLE coleta_seletiva.historico
ADD COLUMN id_operador INTEGER;

ALTER TABLE coleta_seletiva.historico
ADD CONSTRAINT fk_historico_operador
FOREIGN KEY (id_operador)
REFERENCES coleta_seletiva.operador(id);