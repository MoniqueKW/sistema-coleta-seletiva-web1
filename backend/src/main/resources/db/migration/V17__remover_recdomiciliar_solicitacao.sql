-- corrigindo que a solicitação podia ser opcional entre ser domiciliar ou não, descumprindo regras de negócio
ALTER TABLE coleta_seletiva.solicitacao
    DROP COLUMN recolhimento_domiciliar;