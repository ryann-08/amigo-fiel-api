TRUNCATE TABLE atendimentos, veterinarios, tutores RESTART IDENTITY CASCADE;

INSERT INTO tutores (nome, email, telefone, cidade, data_cadastro, ativo) VALUES
('Ryan almeida marques', 'ryan.almeia@email.com', '11987654321', 'São Paulo', '2026-03-01T10:15:00', true),
('Bruno Oliveira', 'bruno.oli@email.com', '19912345678', 'Campinas', '2026-03-02T11:00:00', true),
('Carla Mendes', 'carla.mendes@email.com', '13988887777', 'Santos', '2026-03-03T14:20:00', true),
('Daniel Santos', 'daniel.santos@email.com', '11977776666', 'São Paulo', '2026-03-04T09:30:00', true),
('Elaine Costa', 'elaine.costa@email.com', '19966665555', 'Campinas', '2026-03-05T16:45:00', true),
('Felipe Rocha', 'felipe.rocha@email.com', '13955554444', 'Santos', '2026-03-06T08:10:00', true),
('Gabriela Lima', 'gabriela.lima@email.com', '11944443333', 'São Paulo', '2026-03-07T13:00:00', true),
('Lucas Martins', 'lucas.martins@email.com', '19933332222', 'Campinas', '2026-03-08T15:50:00', true);

INSERT INTO veterinarios (nome, crmv, especialidade, ativo) VALUES
('Dr. Carlos Lima', 'SP-12345', 'CLINICA_GERAL', true),
('Dra. Beatriz Alves', 'SP-23456', 'CIRURGIA', true),
('Dr. Marcos Souza', 'SP-34567', 'DERMATOLOGIA', true),
('Dra. Juliana Dias', 'SP-45678', 'EXOTICOS', true),
('Dr. Roberto Dias', 'SP-56789', 'CLINICA_GERAL', true),
('Dra. Fernanda Lima', 'SP-67890', 'CIRURGIA', false);

INSERT INTO atendimentos (tutor_id, veterinario_id, data_hora, tipo, valor, observacao, status) VALUES
(1, 1, '2026-03-10T14:30:00', 'CONSULTA', 150.00, 'Animal apático desde ontem', 'CONCLUIDO'),
(2, 2, '2026-03-11T09:00:00', 'CONSULTA', 800.00, 'Castração agendada', 'AGENDADO'),
(3, 3, '2026-03-12T10:30:00', 'EXAME', 200.00, 'Coleta de sangue para alergia', 'CONCLUIDO'),
(4, 4, '2026-03-13T11:00:00', 'CONSULTA', 220.00, 'Check-up de porquinho-da-índia', 'AGENDADO'),
(5, 5, '2026-03-14T15:00:00', 'CONSULTA', 120.00, 'Vacina antirrábica anual', 'CONCLUIDO'),
(6, 1, '2026-03-15T16:30:00', 'RETORNO', 80.00, 'Avaliação de cicatrização', 'CONCLUIDO'),
(7, 2, '2026-03-16T08:30:00', 'CONSULTA', 1200.00, 'Remoção de tártaro', 'CANCELADO'),
(8, 3, '2026-03-17T13:00:00', 'CONSULTA', 160.00, 'Coceira intensa nas orelhas', 'CONCLUIDO'),
(1, 4, '2026-03-18T14:00:00', 'CONSULTA', 250.00, 'Revisão de dieta de papagaio', 'AGENDADO'),
(2, 5, '2026-03-19T10:00:00', 'CONSULTA', 110.00, 'V10 aplicada', 'CONCLUIDO'),
(3, 1, '2026-03-20T11:30:00', 'CONSULTA', 150.00, 'Vômitos frequentes', 'CONCLUIDO'),
(4, 2, '2026-03-21T09:30:00', 'EXAME', 180.00, 'Ultrassonografia abdominal', 'AGENDADO'),
(5, 3, '2026-03-22T15:30:00', 'CONSULTA', 160.00, 'Queda de pelos', 'CANCELADO'),
(6, 4, '2026-03-23T16:00:00', 'EXAME', 210.00, 'Exame de fezes de coelho', 'CONCLUIDO'),
(7, 5, '2026-04-01T10:00:00', 'RETORNO', 90.00, 'Retorno de rotina', 'AGENDADO'),
(8, 1, '2026-04-02T11:00:00', 'CONSULTA', 150.00, 'Apatia leve', 'AGENDADO'),
(1, 2, '2026-04-03T14:00:00', 'CONSULTA', 950.00, 'Retirada de corpo estranho', 'CONCLUIDO'),
(2, 3, '2026-04-04T09:00:00', 'EXAME', 190.00, 'Raspagem de pele', 'CONCLUIDO'),
(3, 4, '2026-04-05T15:00:00', 'CONSULTA', 230.00, 'Consulta de hamster', 'CANCELADO'),
(4, 5, '2026-04-06T16:00:00', 'CONSULTA', 130.00, 'Vacina tosse dos canis', 'CONCLUIDO'),
(5, 1, '2026-04-07T08:30:00', 'RETORNO', 80.00, 'Retorno pós-vacina', 'AGENDADO'),
(6, 2, '2026-04-08T10:30:00', 'CONSULTA', 170.00, 'Dor na pata traseira', 'AGENDADO'),
(7, 3, '2026-04-09T13:30:00', 'CONSULTA', 160.00, 'Tratamento de dermatite', 'CONCLUIDO'),
(8, 4, '2026-04-10T14:30:00', 'EXAME', 200.00, 'Exame de sangue exóticos', 'CONCLUIDO'),
(1, 5, '2026-04-11T09:30:00', 'CONSULTA', 150.00, 'Check-up semestral', 'AGENDADO'),
(2, 1, '2026-04-12T11:00:00', 'CONSULTA', 110.00, 'Reforço antirrábica', 'AGENDADO'),
(3, 2, '2026-04-13T15:00:00', 'CONSULTA', 750.00, 'Cirurgia de emergência', 'CONCLUIDO'),
(4, 3, '2026-04-14T16:30:00', 'CONSULTA', 160.00, 'Alergia alimentar', 'CONCLUIDO'),
(5, 4, '2026-04-15T10:00:00', 'CONSULTA', 240.00, 'Consulta de furão', 'AGENDADO'),
(6, 5, '2026-05-01T14:00:00', 'RETORNO', 90.00, 'Retorno geral', 'CONCLUIDO');