ALTER TABLE coleta_seletiva.morador
ALTER COLUMN telefone TYPE VARCHAR(20)
USING telefone::VARCHAR(20);