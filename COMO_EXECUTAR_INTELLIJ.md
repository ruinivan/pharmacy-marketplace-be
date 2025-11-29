# 🚀 Como Executar o Projeto no IntelliJ IDEA

## 📋 Pré-requisitos

1. **IntelliJ IDEA** (versão 2023.1 ou superior)
2. **Java 25 JDK** instalado
3. **Maven 3.9+** instalado
4. **Docker Desktop** instalado e rodando
5. **Node.js 18+** (para o frontend, opcional)

## 🔧 Configuração Inicial

### 1. Abrir o Projeto

1. Abra o IntelliJ IDEA
2. File → Open
3. Selecione a pasta `pharmacy-marketplace-be`
4. Clique em OK

### 2. Configurar o JDK

1. File → Project Structure (ou `Ctrl+Alt+Shift+S`)
2. Na aba **Project**, selecione **Java 25** como SDK
3. Se não tiver o Java 25:
   - Clique em **Add SDK** → **Download JDK**
   - Selecione versão 25
   - Baixe e configure

### 3. Importar Projeto Maven

1. O IntelliJ deve detectar automaticamente o `pom.xml`
2. Se aparecer uma notificação, clique em **Import Maven Project**
3. Aguarde o download das dependências (pode levar alguns minutos)

### 4. Instalar Plugin Lombok

1. File → Settings → Plugins (ou `Ctrl+Alt+S`)
2. Procure por **Lombok**
3. Instale o plugin
4. Reinicie o IntelliJ se solicitado

### 5. Habilitar Annotation Processing

1. File → Settings → Build, Execution, Deployment → Compiler → Annotation Processors
2. Marque **Enable annotation processing**
3. Clique em OK

## 🐳 Iniciar Containers Docker

### Opção 1: Pelo Terminal do IntelliJ

1. Abra o terminal no IntelliJ (View → Tool Windows → Terminal)
2. Execute:
   ```bash
   docker compose up -d
   ```
3. Aguarde os containers iniciarem (MySQL e MongoDB)

### Opção 2: Pelo Docker Desktop

1. Abra o Docker Desktop
2. Vá em **Containers**
3. Clique em **Run** no `docker-compose.yml` do projeto

## ▶️ Executar a Aplicação Spring Boot

### Método 1: Executar pela Classe Principal

1. Navegue até `src/main/java/pharmacymarketplace/App.java`
2. Clique com botão direito no arquivo
3. Selecione **Run 'App.main()'**
4. Ou pressione `Shift+F10`

### Método 2: Executar via Maven

1. Abra o terminal do IntelliJ
2. Execute:
   ```bash
   mvn spring-boot:run
   ```

### Método 3: Criar Run Configuration

1. Run → Edit Configurations
2. Clique no **+** → **Spring Boot**
3. Configure:
   - **Name**: Spring Boot App
   - **Main class**: `pharmacymarketplace.App`
   - **Module**: `pharmacy-marketplace-be`
4. Clique em OK
5. Execute com `Shift+F10`

## ✅ Verificar se Está Funcionando

1. A aplicação deve iniciar na porta **8080**
2. Você verá logs no console do IntelliJ
3. Acesse: http://localhost:8080/api
4. Se aparecer erro 401, está funcionando (endpoint protegido)

## 🎨 Executar Frontend (Opcional)

1. Abra um novo terminal no IntelliJ
2. Navegue até a pasta frontend:
   ```bash
   cd frontend
   ```
3. Instale dependências (primeira vez):
   ```bash
   npm install
   ```
4. Inicie o frontend:
   ```bash
   npm start
   ```
5. O frontend estará em: http://localhost:3000

## 🐛 Solução de Problemas

### Erro: "Cannot resolve symbol"
- **Solução**: File → Invalidate Caches → Invalidate and Restart

### Erro: "Annotation processing not configured"
- **Solução**: Habilite annotation processing (veja passo 5 acima)

### Erro: "Cannot connect to MySQL/MongoDB"
- **Solução**: 
  1. Verifique se Docker está rodando
  2. Execute `docker compose up -d`
  3. Aguarde alguns segundos para os containers iniciarem

### Erro: "Port 8080 already in use"
- **Solução**: 
  1. Pare outras instâncias da aplicação
  2. Ou mude a porta em `src/main/resources/application.properties`

### Erro: "Lombok not working"
- **Solução**: 
  1. Instale o plugin Lombok
  2. Habilite annotation processing
  3. Reinicie o IntelliJ

## 📝 Dicas Úteis

- Use `Ctrl+Shift+F10` para executar rapidamente
- Use `Shift+F9` para executar em modo Debug
- Use `Ctrl+F2` para parar a execução
- Os logs aparecem no console do IntelliJ

---

**Pronto! Agora você pode executar o projeto pelo IntelliJ IDEA.**

