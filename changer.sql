create database if not exists changer;
use changer;

create table if not exists Usuario (
id int primary key auto_increment,
nome varchar(20),
email varchar(100),
senha char(64)
);

insert ignore into Usuario values
(1, 'lele', 'lele@gmail.com', 'E1503E61B90E6A7E7D14D82437A4A988E61F07D3CFCE2E5DA71B8D5DBB0CF4A');