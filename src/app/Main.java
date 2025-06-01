package app;

import dao.*;
import model.*;
import utill.Conexao;

import java.sql.Connection;
import java.sql.Date; 
import java.sql.SQLException;
import java.time.LocalDate; 
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try(Connection conn = Conexao.checarDB();){
            Scanner scanner = new Scanner(System.in);
            AlunoDAO alunoDAO = new AlunoDAO(conn);
            LivroDAO livroDAO = new LivroDAO(conn);
            EmprestimoDAO emprestimoDAO = new EmprestimoDAO(conn);

            int opcao;

            do {
                System.out.println("\n=== MENU PRINCIPAL ===");
                System.out.println("1 - Menu Aluno");
                System.out.println("2 - Menu Livros");
                System.out.println("3 - Menu Empréstimos e Devoluções");
                System.out.println("4 - Menu Relatórios");
                System.out.println("0 - Sair");
                System.out.print("Escolha: ");
                opcao = scanner.nextInt();
                scanner.nextLine();

                switch (opcao) {
                    case 1:
                        int opcaoMenuAluno;
                        do {
                            System.out.println("\n=== MENU ALUNOS ===");
                            System.out.println("1 - Cadastrar aluno");
                            System.out.println("2 - Listar alunos");
                            System.out.println("3 - Buscar aluno por ID");
                            System.out.println("4 - Atualizar aluno");
                            System.out.println("5 - Deletar aluno");
                            System.out.println("0 - Voltar ao Menu Principal");
                            System.out.print("Escolha: ");
                            opcaoMenuAluno = scanner.nextInt();
                            scanner.nextLine();

                            try {
                                switch (opcaoMenuAluno) {
                                    case 1:
                                        System.out.print("Nome do Aluno: ");
                                        String nomeAluno = scanner.nextLine();
                                        System.out.print("Matrícula: ");
                                        String matriculaAluno = scanner.nextLine();
                                        System.out.print("Data de Nascimento (AAAA-MM-DD): ");
                                        String dataNascimentoAluno = scanner.nextLine();
                                        Aluno novoAluno = new Aluno(nomeAluno, matriculaAluno, dataNascimentoAluno);
                                        alunoDAO.inserir(novoAluno);
                                        System.out.println("Aluno cadastrado com sucesso!");
                                        break;
                                    case 2:
                                        List<Aluno> alunos = alunoDAO.listarTodos();
                                        if (alunos.isEmpty()) {
                                            System.out.println("Nenhum aluno cadastrado.");
                                        } else {
                                            System.out.println("\n--- Lista de Alunos ---");
                                            for (Aluno a : alunos) {
                                                System.out.println(a);
                                            }
                                        }
                                        break;
                                    case 3:
                                        System.out.print("ID do aluno a buscar: ");
                                        int idBuscar = scanner.nextInt();
                                        scanner.nextLine();
                                        Aluno alunoEncontrado = alunoDAO.buscarPorId(idBuscar);
                                        if (alunoEncontrado != null) {
                                            System.out.println("Aluno encontrado: " + alunoEncontrado);
                                        } else {
                                            System.out.println("Aluno não encontrado.");
                                        }
                                        break;
                                    case 4:
                                        System.out.print("ID do aluno a atualizar: ");
                                        int idAtualizar = scanner.nextInt();
                                        scanner.nextLine();
                                        Aluno alunoExistente = alunoDAO.buscarPorId(idAtualizar);
                                        if (alunoExistente != null) {
                                            System.out.print("Novo Nome (" + alunoExistente.getNome() + "): ");
                                            String novoNome = scanner.nextLine();
                                            System.out.print("Nova Matrícula (" + alunoExistente.getMatricula() + "): ");
                                            String novaMatricula = scanner.nextLine();
                                            System.out.print("Nova Data de Nascimento (AAAA-MM-DD) (" + alunoExistente.getDataNascimento() + "): ");
                                            String novaDataNascimento = scanner.nextLine();

                                            alunoExistente.setNome(novoNome.isEmpty() ? alunoExistente.getNome() : novoNome);
                                            alunoExistente.setMatricula(novaMatricula.isEmpty() ? alunoExistente.getMatricula() : novaMatricula);
                                            alunoExistente.setDataNascimento(novaDataNascimento.isEmpty() ? alunoExistente.getDataNascimento() : novaDataNascimento);

                                            alunoDAO.atualizar(alunoExistente);
                                            System.out.println("Aluno atualizado com sucesso.");
                                        } else {
                                            System.out.println("Aluno não encontrado.");
                                        }
                                        break;
                                    case 5:
                                        System.out.print("ID do aluno a excluir: ");
                                        int idDelete = scanner.nextInt();
                                        scanner.nextLine();
                                        alunoDAO.deletar(idDelete);
                                        System.out.println("Aluno excluído.");
                                        break;
                                    case 0:
                                        System.out.println("Voltando ao Menu Principal...");
                                        break;
                                    default:
                                        System.out.println("Opção inválida!");
                                }
                            } catch (SQLException e) {
                                System.out.println("Erro no banco de dados: " + e.getMessage());
                            }
                        } while (opcaoMenuAluno != 0);
                        break;

                    case 2:
                        int opcaoMenuLivro;
                        do {
                            System.out.println("\n=== MENU LIVROS ===");
                            System.out.println("1 - Cadastrar livro");
                            System.out.println("2 - Listar livros");
                            System.out.println("3 - Buscar livro por ID");
                            System.out.println("4 - Atualizar livro");
                            System.out.println("5 - Deletar livro");
                            System.out.println("0 - Voltar ao Menu Principal");
                            System.out.print("Escolha: ");
                            opcaoMenuLivro = scanner.nextInt();
                            scanner.nextLine();

                            try {
                                switch (opcaoMenuLivro) {
                                    case 1:
                                        System.out.print("Título: ");
                                        String titulo = scanner.nextLine();
                                        System.out.print("Autor: ");
                                        String autor = scanner.nextLine();
                                        System.out.print("Ano de Publicação: ");
                                        int ano = scanner.nextInt();
                                        System.out.print("Quantidade em Estoque: ");
                                        int quantidade = scanner.nextInt();
                                        scanner.nextLine();
                                        Livro novoLivro = new Livro(titulo, autor, ano, quantidade);
                                        livroDAO.cadastrarLivro(novoLivro);
                                        break;
                                    case 2:
                                        List<Livro> livros = livroDAO.listarLivros();
                                        if (livros.isEmpty()) {
                                            System.out.println("Nenhum livro cadastrado.");
                                        } else {
                                            System.out.println("\n--- Lista de Livros ---");
                                            for (Livro l : livros) {
                                                System.out.println(l);
                                            }
                                        }
                                        break;
                                    case 3:
                                        System.out.print("ID do livro a buscar: ");
                                        int idBuscarLivro = scanner.nextInt();
                                        scanner.nextLine();
                                        Livro livroEncontrado = livroDAO.buscarPorId(idBuscarLivro);
                                        if (livroEncontrado != null) {
                                            System.out.println("Livro encontrado: " + livroEncontrado);
                                        } else {
                                            System.out.println("Livro não encontrado.");
                                        }
                                        break;
                                    case 4:
                                        System.out.print("ID do livro a atualizar: ");
                                        int idAtualizarLivro = scanner.nextInt();
                                        scanner.nextLine();
                                        Livro livroExistente = livroDAO.buscarPorId(idAtualizarLivro);
                                        if (livroExistente != null) {
                                            System.out.print("Novo Título (" + livroExistente.getTitulo() + "): ");
                                            String novoTitulo = scanner.nextLine();
                                            System.out.print("Novo Autor (" + livroExistente.getAutor() + "): ");
                                            String novoAutor = scanner.nextLine();
                                            System.out.print("Novo Ano de Publicação (" + livroExistente.getAno() + "): ");
                                            String novoAnoStr = scanner.nextLine();
                                            System.out.print("Nova Quantidade em Estoque (" + livroExistente.getQuantidade() + "): ");
                                            String novaQuantidadeStr = scanner.nextLine();

                                            livroExistente.setTitulo(novoTitulo.isEmpty() ? livroExistente.getTitulo() : novoTitulo);
                                            livroExistente.setAutor(novoAutor.isEmpty() ? livroExistente.getAutor() : novoAutor);
                                            livroExistente.setAno(novoAnoStr.isEmpty() ? livroExistente.getAno() : Integer.parseInt(novoAnoStr));
                                            livroExistente.setQuantidade(novaQuantidadeStr.isEmpty() ? livroExistente.getQuantidade() : Integer.parseInt(novaQuantidadeStr));

                                            livroDAO.atualizarLivro(livroExistente);
                                            System.out.println("Livro atualizado com sucesso.");
                                        } else {
                                            System.out.println("Livro não encontrado.");
                                        }
                                        break;
                                    case 5:
                                        System.out.print("ID do livro a excluir: ");
                                        int idDeleteLivro = scanner.nextInt();
                                        livroDAO.excluirLivro(idDeleteLivro);
                                        System.out.println("Livro excluído.");
                                        break;
                                    case 0:
                                        System.out.println("Voltando ao Menu Principal...");
                                        break;
                                    default:
                                        System.out.println("Opção inválida!");
                                }
                            } catch (SQLException e) {
                                System.out.println("Erro no banco de dados: " + e.getMessage());
                            }

                        } while (opcaoMenuLivro != 0);
                        break;

                    case 3:
                        int opcaoMenuEmprestimo;
                        do {
                            System.out.println("\n=== MENU EMPRÉSTIMOS E DEVOLUÇÕES ===");
                            System.out.println("1 - Realizar Empréstimo");
                            System.out.println("2 - Registrar Devolução");
                            System.out.println("3 - Listar Todos os Empréstimos");
                            System.out.println("4 - Listar Empréstimos Ativos");
                            System.out.println("0 - Voltar ao Menu Principal");
                            System.out.print("Escolha: ");
                            opcaoMenuEmprestimo = scanner.nextInt();
                            scanner.nextLine();

                            try {
                                switch (opcaoMenuEmprestimo) {
                                    case 1:
                                        System.out.print("ID do Aluno: ");
                                        int idAlunoEmprestimo = scanner.nextInt();
                                        System.out.print("ID do Livro: ");
                                        int idLivroEmprestimo = scanner.nextInt();
                                        System.out.print("Prazo de devolução (dias, ex: 7 para uma semana): ");
                                        int diasDevolucao = scanner.nextInt();
                                        scanner.nextLine();

                                        Aluno alunoEmprestimo = alunoDAO.buscarPorId(idAlunoEmprestimo);
                                        Livro livroEmprestimo = livroDAO.buscarPorId(idLivroEmprestimo);

                                        if (alunoEmprestimo == null) {
                                            System.out.println("Erro: Aluno com ID " + idAlunoEmprestimo + " não encontrado.");
                                        } else if (livroEmprestimo == null) {
                                            System.out.println("Erro: Livro com ID " + idLivroEmprestimo + " não encontrado.");
                                        } else {
                                            Emprestimo novoEmprestimo = new Emprestimo(idAlunoEmprestimo, idLivroEmprestimo, diasDevolucao);
                                            emprestimoDAO.inserir(novoEmprestimo);
                                        }
                                        break;

                                    case 2:
                                        System.out.print("ID do Empréstimo a ser devolvido: ");
                                        int idEmprestimoDevolucao = scanner.nextInt();
                                        scanner.nextLine();
                                        emprestimoDAO.registrarDevolucao(idEmprestimoDevolucao);
                                
                                        break;

                                    case 3:
                                        List<Emprestimo> todosEmprestimos = emprestimoDAO.listarTodos();
                                        if (todosEmprestimos.isEmpty()) {
                                            System.out.println("Nenhum empréstimo registrado.");
                                        } else {
                                            System.out.println("\n--- Lista de Todos os Empréstimos ---");
                                            for (Emprestimo e : todosEmprestimos) {
                                                System.out.println(e);
                                            }
                                        }
                                        break;

                                    case 4:
                                        List<Emprestimo> emprestimosAtivos = emprestimoDAO.listarEmprestimosAtivos();
                                        if (emprestimosAtivos.isEmpty()) {
                                            System.out.println("Nenhum empréstimo ativo encontrado.");
                                        } else {
                                            System.out.println("\n--- Lista de Empréstimos Ativos ---");
                                            for (Emprestimo e : emprestimosAtivos) {
                                                System.out.println(e);
                                            }
                                        }
                                        break;

                                    case 0:
                                        System.out.println("Voltando ao Menu Principal...");
                                        break;
                                    default:
                                        System.out.println("Opção inválida!");
                                }
                            } catch (SQLException e) {
                                System.out.println("Erro no banco de dados: " + e.getMessage());
                            }
                        } while (opcaoMenuEmprestimo != 0);
                        break;

                    case 4:
                        int opcaoMenuRelatorios;
                        do {
                            System.out.println("\n=== MENU RELATÓRIOS ===");
                            System.out.println("1 - Relatório de Livros Disponíveis");
                            System.out.println("2 - Relatório de Livros Emprestados (Ativos)");
                            System.out.println("3 - Relatório de Todos os Empréstimos (Histórico)");
                            System.out.println("0 - Voltar ao Menu Principal");
                            System.out.print("Escolha: ");
                            opcaoMenuRelatorios = scanner.nextInt();
                            scanner.nextLine();

                            try {
                                switch (opcaoMenuRelatorios) {
                                    case 1:
                                        System.out.println("\n--- Relatório de Livros Disponíveis ---");
                                        List<Livro> livrosDisponiveis = livroDAO.listarLivros();
                                        boolean encontrouDisponivel = false;
                                        for (Livro l : livrosDisponiveis) {
                                            if (l.getQuantidade() > 0) {
                                                System.out.println(l.getTitulo() + " (Estoque: " + l.getQuantidade() + ")");
                                                encontrouDisponivel = true;
                                            }
                                        }
                                        if (!encontrouDisponivel) {
                                            System.out.println("Nenhum livro disponível no momento.");
                                        }
                                        break;

                                    case 2:
                                        System.out.println("\n--- Relatório de Livros Emprestados (Ativos) ---");
                                        List<Emprestimo> emprestimosAtivos = emprestimoDAO.listarEmprestimosAtivos();
                                        if (emprestimosAtivos.isEmpty()) {
                                            System.out.println("Nenhum livro atualmente emprestado.");
                                        } else {
                                            for (Emprestimo e : emprestimosAtivos) {
                                                Aluno a = alunoDAO.buscarPorId(e.getIdAluno());
                                                Livro l = livroDAO.buscarPorId(e.getIdLivro());
                                                if (a != null && l != null) {
                                                    System.out.println("Empréstimo ID: " + e.getId() +
                                                                       " | Aluno: " + a.getNome() +
                                                                       " | Livro: " + l.getTitulo() +
                                                                       " | Data Empréstimo: " + e.getDataEmprestimo() +
                                                                       " | Devolução Prevista: " + e.getDataDevolucao());
                                                }
                                            }
                                        }
                                        break;

                                    case 3:
                                        System.out.println("\n--- Relatório de Todos os Empréstimos (Histórico) ---");
                                        List<Emprestimo> todosEmprestimosHistorico = emprestimoDAO.listarTodos();
                                        if (todosEmprestimosHistorico.isEmpty()) {
                                            System.out.println("Nenhum empréstimo registrado no histórico.");
                                        } else {
                                            for (Emprestimo e : todosEmprestimosHistorico) {
                                                Aluno a = alunoDAO.buscarPorId(e.getIdAluno());
                                                Livro l = livroDAO.buscarPorId(e.getIdLivro());
                                                if (a != null && l != null) {
                                                    String dataDevolucaoReal = (e.getDataDevolucao() != null && e.getDataDevolucao().before(Date.valueOf(LocalDate.now().plusDays(1)))) ? e.getDataDevolucao().toString() : "PENDENTE / PREVISTO";
                                                    System.out.println("Empréstimo ID: " + e.getId() +
                                                                       " | Aluno: " + a.getNome() +
                                                                       " | Livro: " + l.getTitulo() +
                                                                       " | Empréstimo: " + e.getDataEmprestimo() +
                                                                       " | Devolvido: " + dataDevolucaoReal);
                                                }
                                            }
                                        }
                                        break;

                                    case 0:
                                        System.out.println("Voltando ao Menu Principal...");
                                        break;
                                    default:
                                        System.out.println("Opção inválida!");
                                }
                            } catch (SQLException e) {
                                System.out.println("Erro no banco de dados: " + e.getMessage());
                            }
                        } while (opcaoMenuRelatorios != 0);
                        break;


                    case 0:
                        System.out.println("Encerrando o sistema...");
                        break;

                    default:
                        System.out.println("Opção inválida!");
                }

            } while (opcao != 0);

            scanner.close();

        } catch (SQLException e) {
            System.out.println("Erro crítico ao iniciar o banco de dados: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

