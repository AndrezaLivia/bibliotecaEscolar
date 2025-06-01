 O projeto de Sistema de Gerenciamento de Biblioteca Escolar teve código desenvolvido em Java e usa um banco de dados MySQL, com o objetivo de fácil de utilização para cadastrar 
 alunos e livros, controlar empréstimos, estoque e gerar relatórios. Segue abaixo, a descrição das funcionalidades implementadas no sistema:
 1. A Base do Sistema:

Conexao.java (no pacote utill): classe de acesso ao banco de dados. Realiza a comunicação com o MySQL.

Foi criada a função checarDB(), que se conecta no banco e verifica se as tabelas (Alunos, Livros, Emprestimos) já existem e foi criada também a função criarDatabaseSeNaoExistir(), caso ele não exista . Se não existirem, ela as criará.

Inserção de Dados de Teste (Mock Data): Para facilitar o desenvolvimento e testes, inserirDadosMock() insere um conjunto de dados de exemplo (alunos, livros e empréstimos, incluindo um 
empréstimo ativo com data_devolucao NULL) se as tabelas estiverem vazias

Classes model (Aluno.java, Livro.java, Emprestimo.java): Objetos que representam os Alunos, Livros e Empréstimos, com todas as suas características (nome, título, datas, etc.). 

Classes dao (AlunoDAO.java, LivroDAO.java, EmprestimoDAO.java): as classes DAO interagem diretamente com o banco de dados.

AlunoDAO.java: implementa as operações básicas de CRUD (Create, Read, Update, Delete) dos alunos (cadastrar, listar, buscar, atualizar, excluir). Na classe, a variável matricula é 
definida como UNIQUE para evitar duplicidade.

LivroDAO.java: implementa as operações básicas de CRUD (Create, Read, Update, Delete) com os dados dos livros.

Foram inseridos os métodos decrementarEstoque() e incrementarEstoque(), fazendo com que a classe LivroDAO tenha métodos específicos para diminuir a quantidade de livros em estoque
quando um é emprestado e aumentar quando é devolvido, garantindo a atualização do estoque em tempo real no banco.

Foi inserido o método getQuantidadeEstoque() para verificar a quantidade de exemplares de livros disponíveis.

EmprestimoDAO.java: classe de controle de empréstimos e devoluções de livros.

Na classe EmprestimoDAO foi inserido o método inserir(Emprestimo emprestimo) com Validação de Estoque, que verifica se o livro tem estoque disponível para empréstimo. Se não tiver, o 
empréstimo não é feito e uma mensagem de erro aparece. Se tiver, o empréstimo é registrado e, automaticamente, a quantidade em estoque do livro é diminuída em 1. 

Na classe EmprestimoDAO foi inserido o método registrarDevolucao(int idEmprestimo) a função foi implementada para devolução de livro. Você informa qual o ID do empréstimo e o sistema
atualizaa data_devolucao no banco de dados para a data de hoje, marcando que o livro foi devolvido. Aumenta a quantidade em estoque do livro em 1, deixando-o disponível para outro empréstimo. 

Na classe EmprestimoDAO foi inserido o método listarEmprestimosAtivos() para trazer somente os empréstimos que ainda não foram devolvidos (ou seja, os livros que estão com os alunos).

2. A Interface do Usuário:

A classe Main serve como o ponto de entrada da aplicação, contendo o método main(). Implementa uma interface de usuário baseada em console, utilizando estruturas do-while para 
loops de menu e switch-case para roteamento de opções. A Main instancia e utiliza as classes DAO (AlunoDAO, LivroDAO, EmprestimoDAO) para executar as operações.Utiliza java.util.Scanner 
para entrada de dados do usuário e System.out.println() para saída e captura SQLExceptions lançadas pelas classes DAO, exibindo mensagens de erro informativas ao usuário sem interromper
a execução do programa. Houve a implementação de "Menu Principal" que dá opções para ir para o "Menu Aluno", "Menu Livros" e a implemetação do "Menu Empréstimos e Devoluções"
que contém as seguintes funções:

"Realizar Empréstimo": Você digita o ID do aluno e do livro, e quantos dias o livro vai ficar emprestado. O sistema verifica o estoque e, se tiver o livro, registra o empréstimo e dá baixa no livro.
"Registrar Devolução": Você informa o ID do empréstimo que está sendo devolvido. O sistema marca a devolução e coloca o livro de volta no estoque
"Listar Todos os Empréstimos": Mostra o histórico completo de todos os empréstimos, devolvidos ou não.
"Listar Empréstimos Ativos": Um relatório rápido para ver quais livros estão atualmente emprestados e quem está com eles.

Por fim, houve a implementação do "Menu Relatórios", que contém a seguintes funções:
"Relatório de Livros Disponíveis": Lista todos os livros que temos na biblioteca e que estão com estoque maior que zero, ou seja, prontos para serem emprestados.
"Relatório de Livros Emprestados (Ativos)": Basicamente, usa a função (listarEmprestimosAtivos) para mostrar quem está com qual livro e a data prevista de devolução.
"Relatório de Todos os Empréstimos": Exibe uma lista completa de todos os empréstimos já feitos, mostrando a data de empréstimo e a data de devolução (se já foi devolvido).

Utilização da estrutura try-catch para tratar exceções, ou erros, que podem ocorrer durante a execução do código
