@echo off
chcp 65001 >nul
echo ========================================
echo Iniciando Pharmacy Marketplace Completo
echo ========================================
echo.

REM Verificar se o Docker está rodando
echo [Verificando Docker...]
docker ps >nul 2>&1
if errorlevel 1 (
    echo [ERRO] Docker não está rodando!
    echo Por favor, inicie o Docker Desktop e tente novamente.
    pause
    exit /b 1
)
echo [OK] Docker está rodando.
echo.

REM Parar processos existentes nas portas 8080 e 3000
echo [Limpando portas 8080 e 3000...]
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8080') do (
    taskkill /F /PID %%a >nul 2>&1
)
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :3000') do (
    taskkill /F /PID %%a >nul 2>&1
)
timeout /t 2 /nobreak >nul
echo [OK] Portas limpas.
echo.

REM Parar e remover containers Docker existentes antes de iniciar
echo [Parando e removendo containers Docker existentes...]
docker compose down >nul 2>&1
docker rm -f pharmacy_marketplace_mysql pharmacy_marketplace_mongodb >nul 2>&1
timeout /t 2 /nobreak >nul

REM Verificar se a porta 3306 está em uso
echo [Verificando portas...]
netstat -ano | findstr :3306 >nul 2>&1
if not errorlevel 1 (
    echo [AVISO] Porta 3306 está em uso!
    echo Verificando se é o container Docker...
    docker ps | findstr :3306 >nul 2>&1
    if errorlevel 1 (
        echo [AVISO] Porta 3306 está em uso por MySQL local!
        echo Tentando parar MySQL local automaticamente...
        call parar-mysql-local.bat
        timeout /t 2 /nobreak >nul
        
        REM Verificar novamente
        netstat -ano | findstr :3306 >nul 2>&1
        if not errorlevel 1 (
            echo.
            echo [ERRO] Não foi possível liberar a porta 3306 automaticamente!
            echo.
            echo SOLUÇÕES:
            echo 1. Execute o PowerShell como Administrador e execute:
            echo    Stop-Service MySQL80
            echo.
            echo 2. Ou pare os processos MySQL manualmente:
            echo    taskkill /F /IM mysqld.exe
            echo.
            echo 3. Depois execute novamente: .\start-all.bat
            echo.
            pause
            exit /b 1
        )
        echo [OK] Porta 3306 liberada.
    )
)

REM Iniciar containers Docker
echo [1/5] Iniciando containers Docker (MySQL e MongoDB)...
docker compose up -d
if errorlevel 1 (
    echo [ERRO] Falha ao iniciar containers Docker!
    echo Verifique se as portas 3306 e 27017 estão disponíveis.
    echo.
    echo Solução: Pare qualquer MySQL local ou processo usando essas portas.
    pause
    exit /b 1
)
echo [OK] Containers iniciados.
echo.

REM Aguardar containers ficarem prontos
echo [2/5] Aguardando containers ficarem prontos...
echo Aguardando MySQL inicializar (pode levar até 30 segundos)...
timeout /t 15 /nobreak >nul

REM Verificar se MySQL está respondendo
:check_mysql
docker exec pharmacy_marketplace_mysql mysqladmin ping -h localhost --silent >nul 2>&1
if errorlevel 1 (
    echo Aguardando MySQL inicializar...
    timeout /t 5 /nobreak >nul
    goto check_mysql
)
echo [OK] MySQL está pronto na porta 3306.
echo.

REM Compilar o projeto
echo [3/5] Compilando projeto backend...
call mvn clean compile -DskipTests
if errorlevel 1 (
    echo [ERRO] Falha na compilação do backend!
    echo Verifique os erros acima e tente novamente.
    pause
    exit /b 1
)
echo [OK] Backend compilado com sucesso.
echo.

REM Iniciar backend
echo [4/5] Iniciando backend Spring Boot...
start "Backend - Pharmacy Marketplace" cmd /k "mvn spring-boot:run"
echo Aguardando backend iniciar (pode levar até 30 segundos)...
timeout /t 20 /nobreak >nul

REM Verificar se backend está rodando
:check_backend
netstat -ano | findstr :8080 >nul 2>&1
if errorlevel 1 (
    echo Aguardando backend iniciar...
    timeout /t 5 /nobreak >nul
    goto check_backend
)
echo [OK] Backend está rodando na porta 8080.
echo.

REM Verificar e instalar dependências do frontend
echo [5/5] Verificando dependências do frontend...
cd frontend
if not exist "node_modules" (
    echo Instalando dependências do frontend (pode levar alguns minutos)...
    call npm install
    if errorlevel 1 (
        echo [ERRO] Falha ao instalar dependências do frontend!
        cd ..
        pause
        exit /b 1
    )
    echo [OK] Dependências instaladas com sucesso.
) else (
    echo [OK] Dependências já instaladas.
)
cd ..
echo.

REM Iniciar frontend em janela separada
echo [6/6] Iniciando frontend React...
start "Frontend - Pharmacy Marketplace" cmd /k "cd /d %~dp0frontend && npm start"
echo Aguardando frontend iniciar (pode levar até 30 segundos)...
timeout /t 15 /nobreak >nul

REM Verificar se frontend está rodando
:check_frontend
netstat -ano | findstr :3000 >nul 2>&1
if errorlevel 1 (
    echo Aguardando frontend iniciar...
    timeout /t 5 /nobreak >nul
    goto check_frontend
)
echo [OK] Frontend está rodando na porta 3000.
echo.

REM Exibir informações finais
echo ========================================
echo Sistema iniciado com sucesso!
echo ========================================
echo Backend: http://localhost:8080
echo Frontend: http://localhost:3000
echo API: http://localhost:8080/api
echo ========================================
echo.
echo Credenciais de teste:
echo - Cliente: cliente@test.com / cliente123
echo - Farmácia: farmacia@test.com / farmacia123
echo - SuperAdmin: admin@pharmacy.com / admin123
echo - Entregador: entregador@test.com / entregador123
echo ========================================
echo.
echo Para parar o sistema, execute: stop-all.bat
echo.
echo Pressione qualquer tecla para fechar esta janela...
echo (O sistema continuará rodando nas janelas abertas)
pause >nul
