@echo off
rem Script para iniciar o backend e servir os arquivos estáticos
rem Navega até a pasta da API e executa o Spring Boot usando o wrapper do Maven
cd api-cadastro-cliente
call mvnw.cmd spring-boot:run
