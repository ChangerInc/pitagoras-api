create database if not exists changer;
use changer;

create table if not exists Usuario (
id binary(16) primary key,
nome varchar(20),
email varchar(100),
senha char(64)
);

create table if not exists Circulo (
id binary(16) primary key,
nomeCirculo varchar(50),
idDono binary(16),  -- Usuario (id)
dataCriacao datetime
);

create table if not exists MembroCirculo (
idMembro binary(16),  -- Usuario (id)
idCirculo binary(16), -- Circulo (id)
dataInclusao datetime,   
primary key (idMembro, idCirculo)
);

insert ignore into Usuario values
('74f5fa64-58a5-11ee-a241-0242ac130002', 'lele', 'lele@gmail.com', 'E1503E61B90E6A7E7D14D82437A4A988E61F07D3CFCE2E5DA71B8D5DBB0CF4A');
