ALTER TABLE coleta_seletiva.solicitacao
ALTER COLUMN data_criacao TYPE TIMESTAMPTZ,
ALTER COLUMN data_agendamento TYPE TIMESTAMPTZ,
ALTER COLUMN data_conclusao TYPE TIMESTAMPTZ;

ALTER TABLE coleta_seletiva.historico
ALTER COLUMN data TYPE TIMESTAMPTZ;