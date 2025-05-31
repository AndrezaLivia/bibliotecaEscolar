package dao;

import model.Emprestimo;
import model.Livro;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class EmprestimoDAO {
    private Connection conn;
    private LivroDAO livroDAO; 
    public EmprestimoDAO(Connection conn) {
        this.conn = conn;
        this.livroDAO = new LivroDAO(conn); 
    }

    public boolean inserir(Emprestimo emprestimo) throws SQLException {
        
        int estoqueAtual = livroDAO.getQuantidadeEstoque(emprestimo.getIdLivro());
        if (estoqueAtual <= 0) {
            System.out.println("Erro: Livro com ID " + emprestimo.getIdLivro() + " sem estoque disponível.");
            return false;
        }

        String sql = "INSERT INTO Emprestimos (id_aluno, id_livro, data_emprestimo, data_devolucao) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, emprestimo.getIdAluno());
            stmt.setInt(2, emprestimo.getIdLivro());
            stmt.setDate(3, Date.valueOf(LocalDate.now()));
            stmt.setDate(4, emprestimo.getDataDevolucao());
            stmt.executeUpdate();

            livroDAO.decrementarEstoque(emprestimo.getIdLivro());
            System.out.println("Empréstimo registrado com sucesso e estoque atualizado!");
            return true; 
        }
    }

    public Emprestimo buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Emprestimos WHERE id_emprestimo = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapearEmprestimo(rs);
            }
        }
        return null;
    }

    public List<Emprestimo> listarTodos() throws SQLException {
        List<Emprestimo> lista = new ArrayList<>();
        String sql = "SELECT * FROM Emprestimos"; 
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                lista.add(mapearEmprestimo(rs));
            }
        }
        return lista;
    }
  
    public List<Emprestimo> listarEmprestimosAtivos() throws SQLException {
        List<Emprestimo> lista = new ArrayList<>();
        String sql = "SELECT * FROM Emprestimos WHERE data_devolucao IS NULL OR data_devolucao > CURRENT_DATE()";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                lista.add(mapearEmprestimo(rs));
            }
        }
        return lista;
    }

    public void atualizar(Emprestimo emprestimo) throws SQLException {
        String sql = "UPDATE Emprestimos SET id_aluno = ?, id_livro = ?, data_emprestimo = ?, data_devolucao = ? WHERE id_emprestimo = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, emprestimo.getIdAluno());
            stmt.setInt(2, emprestimo.getIdLivro());
            stmt.setDate(3, emprestimo.getDataEmprestimo());
            stmt.setDate(4, emprestimo.getDataDevolucao());
            stmt.setInt(5, emprestimo.getId());
            stmt.executeUpdate();
        }
    }

    public boolean registrarDevolucao(int idEmprestimo) throws SQLException {
        Emprestimo emprestimo = buscarPorId(idEmprestimo);
        if (emprestimo == null) {
            System.out.println("Empréstimo com ID " + idEmprestimo + " não encontrado.");
            return false;
        }

        if (emprestimo.getDataDevolucao() != null && emprestimo.getDataDevolucao().before(Date.valueOf(LocalDate.now().plusDays(1)))) {
             System.out.println("Empréstimo com ID " + idEmprestimo + " já foi devolvido.");
             return false;
        }

        String sql = "UPDATE Emprestimos SET data_devolucao = ? WHERE id_emprestimo = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(LocalDate.now())); // Data de devolução atual
            stmt.setInt(2, idEmprestimo);
            stmt.executeUpdate();

            livroDAO.incrementarEstoque(emprestimo.getIdLivro());
            System.out.println("Devolução registrada com sucesso e estoque atualizado!");
            return true;
        }
    }

    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM Emprestimos WHERE id_emprestimo = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private Emprestimo mapearEmprestimo(ResultSet rs) throws SQLException {
        Emprestimo e = new Emprestimo();
        e.setId(rs.getInt("id_emprestimo"));
        e.setIdAluno(rs.getInt("id_aluno"));
        e.setIdLivro(rs.getInt("id_livro"));
        e.setDataEmprestimo(rs.getDate("data_emprestimo"));
        e.setDataDevolucao(rs.getDate("data_devolucao"));
        return e;
    }
}
