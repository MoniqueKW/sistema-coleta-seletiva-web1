ALTER TABLE coleta_seletiva.endereco
ALTER COLUMN uf TYPE VARCHAR(2)
USING TRIM(uf)::VARCHAR(2);

ALTER TABLE coleta_seletiva.solicitacao
ALTER COLUMN copia_uf TYPE VARCHAR(2)
USING TRIM(copia_uf)::VARCHAR(2);

ALTER TABLE coleta_seletiva.pontocoleta
ALTER COLUMN uf TYPE VARCHAR(2)
USING TRIM(uf)::VARCHAR(2);
