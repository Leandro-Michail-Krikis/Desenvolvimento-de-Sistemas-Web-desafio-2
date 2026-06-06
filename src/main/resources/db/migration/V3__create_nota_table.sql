ALTER TABLE destino ADD COLUMN nota_media DOUBLE PRECISION DEFAULT 0.0;
ALTER TABLE destino ADD COLUMN total_notas INTEGER DEFAULT 0;

CREATE TABLE nota (
    id BIGSERIAL PRIMARY KEY,
    valor INTEGER NOT NULL,
    data_avaliacao TIMESTAMP NOT NULL,
    destino_id BIGINT NOT NULL,
    FOREIGN KEY (destino_id) REFERENCES destino(id)
);

