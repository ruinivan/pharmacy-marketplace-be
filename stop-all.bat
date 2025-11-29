@echo off
echo ========================================
echo Parando Pharmacy Marketplace
echo ========================================
echo.

echo [1/2] Parando containers Docker...
docker compose down

echo.
echo [2/2] Parando processos na porta 8080 (Backend)...
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8080') do (
    taskkill /F /PID %%a >nul 2>&1
)

echo.
echo [2/2] Parando processos na porta 3000 (Frontend)...
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :3000') do (
    taskkill /F /PID %%a >nul 2>&1
)

echo.
echo Sistema parado com sucesso!
pause

