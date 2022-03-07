package model;

import javax.persistence.*;
import java.util.Calendar;
import java.util.List;

@Entity
public class Venda {
    @Id
    @SequenceGenerator(name = "seq_venda",sequenceName = "seq_venda_id", allocationSize = 1)
    @GeneratedValue(generator = "seq_venda",strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String observacao;
    @Column(nullable = false)
    private Float valor_total;
    @Column(nullable = false)
    private Calendar data;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Pagamento pagamento;

    @ManyToMany
    @JoinTable(name = "tb_venda_produto", joinColumns = {@JoinColumn(name = "venda_id")},
            inverseJoinColumns = {@JoinColumn(name = "produto_id")})
    private List<Produto> produtos;

    @ManyToMany
    @JoinTable(name = "tb_venda_consulta", joinColumns = {@JoinColumn(name = "venda_id")},
            inverseJoinColumns = {@JoinColumn(name = "consulta_id")})
    private List<Consulta> consultas;

    @ManyToOne
    @JoinColumn(name = "cliente_cpf")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "funcionario_cpf")
    private Funcionario funcionario;

    public Venda() {
    }

    public Pagamento getPagamento() {
        return pagamento;
    }

    public void setPagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<Produto> produtos) {
        this.produtos = produtos;
    }

    public List<Consulta> getConsultas() {
        return consultas;
    }

    public void setConsultas(List<Consulta> consultas) {
        this.consultas = consultas;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Float getValor_total() {
        return valor_total;
    }

    public void setValor_total(Float valor_total) {
        this.valor_total = valor_total;
    }

    public Calendar getData() {
        return data;
    }

    public void setData(Calendar data) {
        this.data = data;
    }
}
