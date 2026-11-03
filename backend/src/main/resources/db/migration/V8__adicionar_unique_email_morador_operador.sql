ALTER TABLE coleta_seletiva.morador
ADD CONSTRAINT uk_morador_email UNIQUE (email);

ALTER TABLE coleta_seletiva.operador
ADD CONSTRAINT uk_operador_email UNIQUE (email);