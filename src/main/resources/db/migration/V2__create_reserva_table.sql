CREATE TABLE reserva (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome_cliente VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    data_reserva TIMESTAMP NOT NULL,
    data_viagem DATE NOT NULL,
    numero_pessoas INT NOT NULL,
    status VARCHAR(20) NOT NULL,
    destino_id BIGINT NOT NULL,
    FOREIGN KEY (destino_id) REFERENCES destino(id)
);