# 💊 Pharmacy Marketplace

Sistema completo de marketplace de farmácias desenvolvido para demonstração.

## 📋 Sobre o Projeto

Sistema web full-stack que permite:
- **Clientes** comprarem produtos farmacêuticos
- **Farmácias** gerenciarem produtos, estoque e pedidos
- **Entregadores** gerenciarem entregas
- **SuperAdmin** gerenciar todo o sistema

## 🚀 Início Rápido

### Execução Automática (Recomendado)

**No PowerShell (Windows):**
```powershell
.\start-all.bat
```

**No CMD (Prompt de Comando):**
```cmd
start-all.bat
```

O script irá:
1. ✅ Verificar se Docker está rodando
2. ✅ Iniciar containers Docker (MySQL e MongoDB)
3. ✅ Aguardar containers ficarem prontos
4. ✅ Iniciar backend Spring Boot (porta 8080)
5. ✅ Instalar dependências do frontend (se necessário)
6. ✅ Iniciar frontend React (porta 3000)

### Execução Manual

1. **Iniciar Docker Desktop** (deve estar rodando)

2. **Iniciar containers:**
   ```bash
   docker compose up -d
   ```

3. **Iniciar backend:**
   ```bash
   mvn spring-boot:run
   ```

4. **Iniciar frontend:**
   ```bash
   cd frontend
   npm install  # Primeira vez apenas
   npm start
   ```

## 🎯 Acessos

- **Frontend:** http://localhost:3000
- **Backend API:** http://localhost:8080/api
- **MySQL:** localhost:3306
- **MongoDB:** localhost:27017

## 🔐 Credenciais de Teste

- **Cliente:** `cliente@test.com` / `cliente123`
- **Farmácia:** `farmacia@test.com` / `farmacia123`
- **SuperAdmin:** `admin@pharmacy.com` / `admin123`
- **Entregador:** `entregador@test.com` / `entregador123`

## 🛠️ Tecnologias

### Backend
- **Java 25**
- **Spring Boot 3.5.7**
- **Spring Security** (JWT)
- **Spring Data JPA**
- **Maven**
- **MySQL 8.0** (dados relacionais)
- **MongoDB 7.0** (logs e reviews)
- **MapStruct** (mapeamento DTOs)
- **Docker** (containers)

### Frontend
- **React 18**
- **TypeScript**
- **Axios** (requisições HTTP)
- **React Router DOM** (roteamento)
- **Context API** (estado global)

## 📋 Pré-requisitos

- **Java 25** - [Download](https://www.oracle.com/java/technologies/downloads/)
- **Maven 3.9+** - [Download](https://maven.apache.org/download.cgi)
- **Node.js 18+** - [Download](https://nodejs.org/)
- **Docker Desktop** - [Download](https://www.docker.com/products/docker-desktop/)

## 📝 Scripts Disponíveis

- `start-all.bat` - Inicia tudo (Docker + Backend + Frontend)
- `start-backend.bat` - Inicia apenas o backend
- `start-frontend.bat` - Inicia apenas o frontend
- `stop-all.bat` - Para todos os serviços

## 🛑 Parar o Sistema

```powershell
.\stop-all.bat
```

## 🔧 Estrutura do Projeto

```
pharmacy-marketplace-be/
├── src/                    # Código fonte do backend
│   └── main/
│       ├── java/
│       │   └── pharmacymarketplace/
│       │       ├── auth/          # Autenticação e autorização
│       │       ├── user/          # Gestão de usuários
│       │       ├── pharmacy/      # Gestão de farmácias
│       │       ├── product/       # Gestão de produtos
│       │       ├── inventory/     # Gestão de estoque
│       │       ├── order/         # Gestão de pedidos
│       │       ├── cart/          # Carrinho de compras
│       │       ├── promotion/     # Promoções
│       │       ├── delivery/      # Entregas
│       │       ├── favorite/      # Favoritos
│       │       ├── review/        # Avaliações
│       │       └── notification/  # Notificações
│       └── resources/
│           └── application.properties
├── frontend/               # Código fonte do frontend
│   ├── src/
│   │   ├── components/    # Componentes React
│   │   ├── pages/         # Páginas
│   │   ├── contexts/      # Context API
│   │   └── services/      # Serviços API
│   └── package.json
├── docker-compose.yml      # Configuração Docker
├── pom.xml                 # Dependências Maven
└── README.md
```

## 📚 Documentação Adicional

- **Regras de Negócio:** Consulte `REGRAS_NEGOCIO.md` para entender a lógica completa do sistema

## ⚠️ Problemas Comuns

### Porta 8080 em uso
```powershell
.\stop-all.bat
# Aguardar alguns segundos
.\start-all.bat
```

### Porta 3000 em uso
```powershell
.\stop-all.bat
```

### Docker não está rodando
- Abrir Docker Desktop
- Aguardar até que o ícone fique verde/ativo
- Verificar: `docker ps` (não deve dar erro)

### Backend não conecta ao MySQL
- Verificar se MySQL está rodando: `docker ps | findstr mysql`
- Aguardar mais tempo (MySQL pode levar até 30 segundos para inicializar)
- Verificar logs: `docker compose logs mysql`

## ✅ Funcionalidades Implementadas

### Cliente
- ✅ Navegação de produtos
- ✅ Carrinho de compras
- ✅ Favoritos
- ✅ Pedidos
- ✅ Avaliações
- ✅ Notificações

### Farmácia
- ✅ Gestão de produtos
- ✅ Gestão de estoque
- ✅ Gestão de pedidos
- ✅ Promoções

### Entregador
- ✅ Visualização de entregas
- ✅ Atualização de status

### SuperAdmin
- ✅ Gestão de usuários
- ✅ Gestão de farmácias
- ✅ Gestão de produtos
- ✅ Visualização de todos os pedidos

## 🔒 Segurança

- ✅ Autenticação JWT
- ✅ Autorização por roles
- ✅ Senhas criptografadas (BCrypt)
- ✅ Validação de dados
- ✅ Tratamento de exceções
- ✅ CORS configurado

## 📄 Licença

Este projeto foi desenvolvido para fins de demonstração.

---

**Desenvolvido seguindo as melhores práticas de desenvolvimento de software.**
