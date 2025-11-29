# 📋 REGRAS DE NEGÓCIO - PHARMACY MARKETPLACE

## 1. AUTENTICAÇÃO E AUTORIZAÇÃO

### 1.1. Roles do Sistema
- **ROLE_CUSTOMER**: Cliente final que compra produtos
- **ROLE_PHARMACY_ADMIN**: Administrador de farmácia
- **ROLE_DELIVERY_PERSONNEL**: Entregador
- **ROLE_ADMIN**: SuperAdmin do sistema

### 1.2. Fluxo de Autenticação
1. Usuário faz login com email e senha
2. Sistema valida credenciais
3. Sistema gera JWT token
4. Token é armazenado no frontend (localStorage)
5. Token é enviado em todas as requisições (Header Authorization)

### 1.3. Autorização por Endpoint
- Endpoints públicos: `/api/products`, `/api/pharmacies`, `/api/brands`, `/api/manufacturers`
- Endpoints protegidos: Requerem autenticação e role específica
- Admin: Acesso total ao sistema
- Pharmacy Admin: Acesso apenas aos dados da sua farmácia
- Customer: Acesso apenas aos seus próprios dados

## 2. GESTÃO DE USUÁRIOS

### 2.1. Criação de Usuário
- Registro via `/api/auth/register`
- Senha é criptografada com BCrypt
- Usuário recebe role `ROLE_CUSTOMER` por padrão
- Email deve ser único no sistema

### 2.2. Perfis de Usuário
- **Customer**: Criado automaticamente ao registrar
- **Pharmacy Admin**: Criado pelo SuperAdmin
- **Delivery Personnel**: Criado pelo SuperAdmin
- **Admin**: Criado via seed ou manualmente

## 3. GESTÃO DE FARMÁCIAS

### 3.1. Criação de Farmácia
- Apenas SuperAdmin pode criar farmácias
- CNPJ deve ser único
- Farmácia deve ter endereço completo
- Farmácia é associada a um usuário (Pharmacy Admin)

### 3.2. Atualização de Farmácia
- Pharmacy Admin pode atualizar dados da sua farmácia
- SuperAdmin pode atualizar qualquer farmácia

## 4. GESTÃO DE PRODUTOS

### 4.1. Criação de Produto
- Pharmacy Admin pode criar produtos
- Produto deve ter: nome, descrição, princípio ativo
- Produto pode ter múltiplas variantes (dosagem, tamanho)
- Produto pode requerer receita médica

### 4.2. Variantes de Produto
- Cada variante tem: SKU, dosagem, tamanho da embalagem
- SKU deve ser único
- Variante é associada a um produto

### 4.3. Categorias, Marcas e Fabricantes
- Criados pelo SuperAdmin
- Produtos são associados a categoria, marca e fabricante

## 5. GESTÃO DE ESTOQUE (INVENTÁRIO)

### 5.1. Adicionar Produto ao Estoque
- Pharmacy Admin adiciona produtos ao estoque da sua farmácia
- Deve informar: produto variante, quantidade, preço
- Preço é específico por farmácia

### 5.2. Atualização de Estoque
- Pharmacy Admin pode atualizar quantidade e preço
- Quantidade não pode ser negativa
- Ao criar pedido, estoque é verificado

## 6. GESTÃO DE PEDIDOS

### 6.1. Criação de Pedido
- Cliente cria pedido com itens do carrinho
- Pedido é associado a uma farmácia
- Sistema verifica disponibilidade em estoque
- Sistema calcula total baseado nos preços do estoque
- Pedido inicia com status `PENDING`

### 6.2. Status do Pedido
1. **PENDING**: Pedido criado, aguardando processamento
2. **AWAITING_PAYMENT**: Aguardando pagamento
3. **AWAITING_PRESCRIPTION**: Aguardando receita médica
4. **PROCESSING**: Em processamento pela farmácia
5. **SHIPPED**: Enviado para entrega
6. **DELIVERED**: Entregue ao cliente
7. **CANCELLED**: Cancelado
8. **REFUNDED**: Reembolsado

### 6.3. Atualização de Status
- Pharmacy Admin pode atualizar status do pedido
- SuperAdmin pode atualizar qualquer pedido
- Cliente pode cancelar pedido pendente

### 6.4. Receita Médica
- Se produto requer receita, pedido deve incluir receita
- Receita tem: número, médico, CRM, data, arquivo

## 7. CARRINHO DE COMPRAS

### 7.1. Adicionar ao Carrinho
- Cliente adiciona produtos ao carrinho
- Carrinho é por farmácia (não pode misturar farmácias)
- Quantidade é validada contra estoque

### 7.2. Atualização do Carrinho
- Cliente pode alterar quantidade
- Cliente pode remover itens
- Sistema recalcula total automaticamente

### 7.3. Finalização
- Carrinho é convertido em pedido
- Estoque é verificado novamente
- Pedido é criado com status `PENDING`

## 8. PROMOÇÕES

### 8.1. Criação de Promoção
- Pharmacy Admin cria promoções para sua farmácia
- Promoção tem: nome, descrição, tipo de desconto, valor, período
- Promoção pode ter regras (valor mínimo, quantidade mínima)
- Promoção pode ter alvos (produto, variante, categoria, todos)

### 8.2. Tipos de Desconto
- **PERCENTAGE**: Desconto percentual
- **FIXED**: Desconto fixo em valor

### 8.3. Aplicação de Promoção
- Sistema verifica promoções ativas
- Aplica desconto conforme regras
- Desconto é calculado no pedido

## 9. FAVORITOS

### 9.1. Adicionar Favorito
- Cliente pode favoritar produtos
- Produto favoritado aparece na lista de favoritos

### 9.2. Remover Favorito
- Cliente pode remover produto dos favoritos

## 10. AVALIAÇÕES (REVIEWS)

### 10.1. Criar Avaliação
- Cliente pode avaliar produtos e farmácias
- Avaliação tem: nota (1-5), comentário
- Avaliação é armazenada no MongoDB

### 10.2. Visualização
- Avaliações são públicas
- Podem ser filtradas por produto ou farmácia

## 11. NOTIFICAÇÕES

### 11.1. Tipos de Notificação
- Mudança de status do pedido
- Promoções disponíveis
- Produtos em estoque

### 11.2. Armazenamento
- Notificações são armazenadas no MongoDB
- Cliente pode visualizar suas notificações

## 12. ENTREGAS

### 12.1. Criação de Entrega
- Entrega é criada quando pedido muda para `SHIPPED`
- Entrega é associada a um entregador
- Entrega tem código de rastreamento

### 12.2. Status da Entrega
1. **PENDING**: Aguardando atribuição
2. **ASSIGNED**: Atribuída a entregador
3. **IN_TRANSIT**: Em trânsito
4. **DELIVERED**: Entregue
5. **FAILED**: Falhou
6. **CANCELLED**: Cancelada

### 12.3. Atualização
- Entregador pode atualizar status
- Entregador pode adicionar notas

## 13. ORDEM DE EXECUÇÃO (LÓGICA)

### 13.1. Fluxo de Compra
1. Cliente navega produtos (público)
2. Cliente adiciona ao carrinho (autenticado)
3. Cliente finaliza compra → Pedido criado
4. Farmácia recebe pedido → Status `PENDING`
5. Farmácia processa → Status `PROCESSING`
6. Farmácia envia → Status `SHIPPED` + Entrega criada
7. Entregador recebe → Status `ASSIGNED`
8. Entregador entrega → Status `DELIVERED`
9. Pedido finalizado → Status `DELIVERED`

### 13.2. Fluxo de Cadastro de Produto
1. SuperAdmin cria categorias, marcas, fabricantes
2. Pharmacy Admin cria produto
3. Pharmacy Admin adiciona variantes
4. Pharmacy Admin adiciona ao estoque (quantidade e preço)

### 13.3. Fluxo de Promoção
1. Pharmacy Admin cria promoção
2. Define regras e alvos
3. Promoção fica ativa no período definido
4. Sistema aplica automaticamente no pedido

## 14. VALIDAÇÕES IMPORTANTES

### 14.1. Estoque
- Quantidade não pode ser negativa
- Pedido não pode ser criado se estoque insuficiente
- Estoque é verificado ao criar pedido

### 14.2. Preços
- Preço deve ser positivo
- Preço é por farmácia (mesmo produto pode ter preços diferentes)

### 14.3. Pedidos
- Pedido deve ter pelo menos um item
- Pedido deve ter farmácia válida
- Pedido deve ter cliente válido

### 14.4. Usuários
- Email deve ser único
- Senha deve ter no mínimo 6 caracteres
- Usuário deve ter pelo menos uma role

## 15. SOFT DELETE

### 15.1. Implementação
- Entidades principais usam soft delete
- Registros não são removidos fisicamente
- Campo `deletedAt` marca como deletado
- Queries ignoram registros deletados automaticamente

## 16. AUDITORIA

### 16.1. Campos de Auditoria
- `createdAt`: Data de criação
- `updatedAt`: Data de atualização
- `deletedAt`: Data de exclusão (soft delete)

## 17. SEGURANÇA

### 17.1. Senhas
- Criptografadas com BCrypt
- Nunca expostas em respostas da API

### 17.2. Tokens JWT
- Expiração: 24 horas
- Secret key configurada no `application.properties`
- Token é validado em todas as requisições protegidas

### 17.3. CORS
- Configurado para permitir `http://localhost:3000`
- Headers necessários são permitidos

## 18. BANCO DE DADOS

### 18.1. MySQL (JPA)
- Armazena dados relacionais
- Entidades: User, Customer, Pharmacy, Product, Order, etc.

### 18.2. MongoDB
- Armazena logs e reviews
- Estrutura flexível para dados não relacionais

## 19. SEED DE DADOS

### 19.1. Dados Iniciais
- Roles do sistema
- Usuários de teste (Admin, Cliente, Farmácia, Entregador)
- Farmácias de exemplo
- Produtos e estoque de exemplo

### 19.2. Execução
- Seed é executado automaticamente na primeira inicialização
- Pode ser desabilitado em `application.properties`: `app.seed.enabled=false`

## 20. BOAS PRÁTICAS APLICADAS

### 20.1. Backend
- DTOs para transferência de dados
- Validação com Bean Validation
- Tratamento centralizado de exceções
- Mapeamento com MapStruct
- Transações para operações críticas
- Soft delete para preservar dados

### 20.2. Frontend
- Componentes reutilizáveis
- Context API para estado global
- Rotas protegidas
- Tratamento de erros
- Loading states
- Validação de formulários

---

**Este documento descreve as regras de negócio e a lógica de funcionamento do sistema Pharmacy Marketplace.**

