package model;

import java.sql.Date;
import java.time.LocalDate;

public class Emprestimo {
    private int id;
    private int idAluno;
    private int idLivro;
    private Date dataEmprestimo;
    private Date dataDevolucao;

    public Emprestimo() {

    }

    public Emprestimo(int id, int idAluno, int idLivro, Date dataEmprestimo, Date dataDevolucao) {
        this.id = id;
        this.idAluno = idAluno;
        this.idLivro = idLivro;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucao = dataDevolucao;
    }

    public Emprestimo( int idAluno, int idLivro, int dias) {
        Date dataDevolucao = Date.valueOf(LocalDate.now().plusDays(dias));
        this.idAluno = idAluno;
        this.idLivro = idLivro;
        this.dataDevolucao = dataDevolucao;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdAluno() { return idAluno; }

    public void setIdAluno(int idAluno) {
        this.idAluno = idAluno;
    }

    public int getIdLivro() {
        return idLivro;
    }

    public void setIdLivro(int idLivro) {
        this.idLivro = idLivro;
    }

    public Date getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(Date dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public Date getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(Date dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }
    
    @Override
    public String toString() {
        return "Emprestimo {" +
                "id=" + id +
                ", idAluno=" + idAluno +
                ", idLivro=" + idLivro +
                ", dataEmprestimo=" + (dataEmprestimo != null ? dataEmprestimo.toString() : "null") +
                ", dataDevolucao=" + (dataDevolucao != null ? dataDevolucao.toString() : "null") +
                '}';
    }
    
}
