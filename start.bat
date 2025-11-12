@echo off
rem Script para iniciar o backend e abrir o localhost automaticamente

rem Define o número da porta
set PORT=8081

rem Navega até a pasta da API
cd api-cadastro-cliente

rem Inicia o Spring Boot em uma nova janela
start cmd /k "call mvnw.cmd spring-boot:run"

rem Aguarda alguns segundos para o servidor iniciar
timeout /t 10 /nobreak >nul

rem Abre o navegador padrão no localhost com a porta configurada
start http://localhost:%PORT%
