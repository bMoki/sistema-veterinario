package model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Receita {
    @Id
    @SequenceGenerator(name = "seq_receita", sequenceName = "seq_receita_id",allocationSize = 1)
    @GeneratedValue(generator = "seq_receita",strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String orientacao;

    @ManyToOne
    @JoinColumn(name = "consulta_id")
    private Consulta consulta;

    @ManyToMany
    @JoinTable(name = "tb_receita_produto", joinColumns = {@JoinColumn(name = "receita_id")},
            inverseJoinColumns = {@JoinColumn(name = "produto_id")})
    private List<Produto> produtos;

    public Receita() {
    }

    public Consulta getConsulta() {
        return consulta;
    }

    public void setConsulta(Consulta consulta) {
        this.consulta = consulta;
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<Produto> produtos) {
        this.produtos = produtos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrientacao() {
        return orientacao;
    }

    public void setOrientacao(String orientacao) {
        this.orientacao = orientacao;
    }
}
