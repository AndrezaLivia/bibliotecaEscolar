package utill;

import java.sql.*;

public class Conexao {
    private static final String HOST = "localhost";
    private static final String PORTA = "3306";
    private static final String USUARIO = "bibliotecar";
    private static final String SENHA = "jhh138";
    private static final String BANCO = "bibliotecaescolar";

    public static Connection checarDB() throws SQLException {
        try (Connection conexao = DriverManager.getConnection(
                "jdbc:mysql://" + HOST + ":" + PORTA, USUARIO, SENHA)) {
            
            criarDatabaseSeNaoExistir(conexao);
        }
        
        try (Connection conexao = conectarDB()) {
            criarTabelasSeNaoExistir(conexao);
            inserirDadosMock(conexao);
            System.out.println("Banco e tabelas prontos para uso!");
        }
        
        return conectarDB();
    }

    private static Connection conectarDB() throws SQLException {
        String url = "jdbc:mysql://" + HOST + ":" + PORTA + "/" + BANCO;
        return DriverManager.getConnection(url, USUARIO, SENHA);
    }

    private static void criarDatabaseSeNaoExistir(Connection conexao) throws SQLException {
        try (ResultSet resultSet = conexao.getMetaData().getCatalogs()) {
            boolean databaseExiste = false;
            
            while (resultSet.next()) {
                String databaseName = resultSet.getString(1);
                if(databaseName.equals(BANCO)) {
                    databaseExiste = true;
                    break;
                }
            }
            
            if(!databaseExiste) {
                try (Statement stmt = conexao.createStatement()) {
                    stmt.executeUpdate("CREATE DATABASE " + BANCO);
                    stmt.executeUpdate("USE " + BANCO);
                    System.out.println("Database created successfully...");
                }
            }
        }
    }

    private static void criarTabelasSeNaoExistir(Connection conexao) throws SQLException {
        DatabaseMetaData meta = conexao.getMetaData();
        try (ResultSet resultado = meta.getTables(null, null, "Alunos", new String[]{"TABLE"})) {
            if (!resultado.next()) {
                try (Statement stmt = conexao.createStatement()) {
                    String sql = """
                            CREATE TABLE Alunos (
                                id_aluno INT AUTO_INCREMENT PRIMARY KEY ,
                                nome_aluno VARCHAR (100) NOT NULL ,
                                matricula VARCHAR (20) UNIQUE ,
                                data_nascimento DATE
                            );
                            """;
                    stmt.executeUpdate(sql);
                    System.out.println("Tabela Alunos criada.");
                }
            } else {
                System.out.println("Tabela Alunos já existe.");
            }
        }

        try (ResultSet resultado = meta.getTables(null, null, "Livros", new String[]{"TABLE"})) {
            if (!resultado.next()) {
                try (Statement stmt = conexao.createStatement()) {
                    String sql = """
                            CREATE TABLE Livros (
                                id_livro INT AUTO_INCREMENT PRIMARY KEY ,
                                titulo VARCHAR (150) NOT NULL ,
                                autor VARCHAR (100) ,
                                ano_publicacao INT ,
                                quantidade_estoque INT DEFAULT 0
                            );
                            """;
                    stmt.executeUpdate(sql);
                    System.out.println("Tabela Livros criada.");
                }
            } else {
                System.out.println("Tabela Livros já existe.");
            }
        }

        try (ResultSet resultado = meta.getTables(null, null, "Emprestimos", new String[]{"TABLE"})) {
            if (!resultado.next()) {
                try (Statement stmt = conexao.createStatement()) {
                    String sql = """
                            CREATE TABLE Emprestimos (
                                id_emprestimo INT AUTO_INCREMENT PRIMARY KEY ,
                                id_aluno INT ,
                                id_livro INT ,
                                data_emprestimo DATE DEFAULT ( CURRENT_DATE ),
                                data_devolucao DATE ,
                                FOREIGN KEY ( id_aluno ) REFERENCES Alunos( id_aluno ),
                                FOREIGN KEY ( id_livro ) REFERENCES Livros( id_livro )
                            );
                            """;
                    stmt.executeUpdate(sql);
                    System.out.println("Tabela Emprestimos criada.");
                }
            } else {
                System.out.println("Tabela Emprestimos já existe.");
            }
        }
    }

    private static void inserirDadosMock(Connection conexao) throws SQLException {
        try (Statement stmt = conexao.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM Alunos");
            rs.next();
            int totalAlunos = rs.getInt(1);

            if (totalAlunos == 0) {
                stmt.executeUpdate("""
                    INSERT INTO Alunos (nome_aluno, matricula, data_nascimento) VALUES
                    ('Maria Silva', '2023001', '2005-03-10'),
                    ('Pedro Costa', '2023002', '2004-11-22'),
                    ('Ana Oliveira', '2023003', '2006-07-01');
                """);
                System.out.println("Dados mock de Alunos inseridos.");
            } else {
                System.out.println("Tabela Alunos já possui dados.");
            }


            rs = stmt.executeQuery("SELECT COUNT(*) FROM Livros");
            rs.next();
            int totalLivros = rs.getInt(1);

            if (totalLivros == 0) {
                stmt.executeUpdate("""
                    INSERT INTO Livros (titulo, autor, ano_publicacao, quantidade_estoque) VALUES
                    ('Java Básico', 'João Java', 2015, 5),
                    ('Banco de Dados', 'Maria DB', 2018, 3),
                    ('Estruturas de Dados', 'Carlos Algoritmo', 2020, 7),
                    ('Introdução a Redes', 'Laura Conexão', 2022, 1);
                """);
                System.out.println("Dados mock de Livros inseridos.");
            } else {
                System.out.println("Tabela Livros já possui dados.");
            }


            rs = stmt.executeQuery("SELECT COUNT(*) FROM Emprestimos");
            rs.next();
            int totalEmprestimos = rs.getInt(1);

            if (totalEmprestimos == 0) {
                stmt.executeUpdate("""
                    INSERT INTO Emprestimos (id_aluno, id_livro, data_emprestimo, data_devolucao) VALUES
                    (1, 2, '2024-05-01', '2024-05-15'),
                    (2, 1, '2024-05-03', '2024-05-17'),
                    (3, 3, '2024-05-05', NULL);
                """);
                System.out.println("Dados mock de Emprestimos inseridos.");
            } else {
                System.out.println("Tabela Emprestimos já possui dados.");
            }
        }
    }
}
