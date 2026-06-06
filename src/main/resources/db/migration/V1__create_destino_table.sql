CREATE TABLE destino (
    id          BIGSERIAL PRIMARY KEY,
    nome        VARCHAR(255) NOT NULL,
    descricao   TEXT,
    pais        VARCHAR(100) NOT NULL,
    data_criacao    TIMESTAMP NOT NULL,
    data_atualizacao TIMESTAMP
);

INSERT INTO destino (nome, descricao, pais, data_criacao, data_atualizacao) VALUES
('Paris', 'A cidade luz, famosa pela Torre Eiffel, museus e sua refinada gastronomia.', 'França', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Rio de Janeiro', 'Cidade maravilhosa com praias deslumbrantes, o Cristo Redentor e o Pão de Açúcar.', 'Brasil', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Tóquio', 'Capital do Japão que mistura tradição milenar com tecnologia de ponta.', 'Japão', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Nova York', 'A cidade que nunca dorme, lar da Estátua da Liberdade e da Times Square.', 'Estados Unidos', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Lisboa', 'Capital de Portugal, conhecida pelos pastéis de nata, o fado e os históricos bondes.', 'Portugal', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Buenos Aires', 'Capital da Argentina, berço do tango e com uma vibrante vida cultural e noturna.', 'Argentina', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Roma', 'A cidade eterna, repleta de história com o Coliseu, o Vaticano e a Fontana di Trevi.', 'Itália', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

