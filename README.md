# MyFood - Sistema de Delivery
O MyFood é um sistema de gerenciamento de pedidos e entregas de comida, inspirado em plataformas como o iFood. Este projeto foi desenvolvido como parte da disciplina de *Programação 2*, com foco total em **Programação Orientada a Objetos (POO)** e a **linguagem de programação Java**.

## Funcionalidades (User Stories)
O sistema foi construído de forma incremental, atendendo aos seguintes requisitos:

### Milestone 1:
- **US1 - Gestão de Usuários**: Criação de contas para Clientes e Donos de Empresa, com validações de e-mail único e formato de CPF. ✅
- **US2 - Gestão de Empresas**: Cadastro de restaurantes vinculados a proprietários. ✅
- **US3 - Catálogo de Produtos**: Criação, edição e listagem de produtos por empresa. ✅
- **US4 - Fluxo de Pedidos**: Sistema de carrinho de compras que permite adicionar/remover produtos, calcular o valor total e gerenciar estados. ✅

### Milestone 2 (Em breve):
- **US5 - Criação de Mercados** ⚙️
- **US6 - Criação de Farmácia** ⚙️
- **US7 - Criação de Entregador** ⚙️
- **US8 - Sistema de Entregas** ⚙️

## Arquitetura e Padrões de Projeto
O projeto segue uma arquitetura separada em camadas para garantir a manutenção e a escalabilidade:
- **Padrão Facade (Fachada)**: A classe Facade centraliza todas as chamadas do sistema, isolando a complexidade dos gerenciadores para os testes de aceitação.
- **Managers (Controladores)**: Lógica de negócio distribuída em classes especialistas (UsuarioManager, EmpresaManager, etc.) para respeitar o *Princípio da Responsabilidade Única*.
- **Models (Entidades)**: Uso de *Herança* (classe Usuario como base para Cliente e DonoEmpresa) e *Encapsulamento* para proteção de dados.

## Estrutura do Projeto
```plaintext
src/
├── models/         # classes de dados (Usuario, Empresa, Produto, Pedido)
├── managers/       # lógica de negócio e validações
├── Facade.java     # fachada do sistema
└── Main.java       # ponto de entrada e execução dos testes
tests/              # scripts de teste (.txt) para o EasyAccept (fornecidos pelo docente)
easyaccept.jar      # biblioteca de testes de aceitação (fornecido pelo docente)
```

## Como executar
### Pré-requisitos:
- JDK 8 ou superior instalado;
- Arquivo `easyaccept.jar` na raiz do projeto.

### Compilação:
```bash
# criar pasta para os arquivos compilados
mkdir bin

# compilar todos os pacotes
javac -cp "lib\easyaccept.jar" -d bin src\*.java src\models\*.java src\managers\*.java
```

### Execução dos Testes
Para rodar a bateria completa de testes através da classe `Main`:
```bash
java -cp "bin:lib\easyaccept.jar" Main
```
*OBS: No Windows, utilize `;` em vez de `:` no comando acima.*

## Qualidade e Testes
O projeto foi validado utilizando a ferramenta EasyAccept, garantindo que 100% das regras de negócio descritas nos guias de teste fossem atendidas, incluindo o tratamento correto de exceções e formatos de saída.

## Autor e Contato
| Nome | Contato |
| ---- | ------- |
| Laura Mainero | lblrm@ic.ufal.br |

