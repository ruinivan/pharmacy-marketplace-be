@echo off
echo ========================================
echo Iniciando Backend - Pharmacy Marketplace
echo ========================================
echo.

REM Verificar se o Docker está rodando
docker ps >nul 2>&1
if errorlevel 1 (
    echo [ERRO] Docker nao esta rodando!
    echo Por favor, inicie o Docker Desktop e tente novamente.
    pause
    exit /b 1
)

echo [1/3] Verificando containers Docker...
docker compose ps

echo.
echo [2/3] Iniciando containers (MySQL e MongoDB)...
docker compose up -d

echo.
echo Aguardando containers ficarem prontos...
timeout /t 10 /nobreak >nul

echo.
echo [3/3] Iniciando Spring Boot Backend...
echo.
mvn spring-boot:run

pause
