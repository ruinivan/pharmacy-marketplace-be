@echo off
echo ========================================
echo Iniciando Frontend - Pharmacy Marketplace
echo ========================================
echo.

cd frontend

echo [1/2] Verificando dependencias...
if not exist "node_modules" (
    echo Instalando dependencias...
    call npm install
)

echo.
echo [2/2] Iniciando servidor de desenvolvimento...
echo.
echo Frontend estara disponivel em: http://localhost:3000
echo.
call npm start

pause

