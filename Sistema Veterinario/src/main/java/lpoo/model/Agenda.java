package model;

import javax.persistence.*;
import java.util.Calendar;

@Entity
public class Agenda {
    @Id
    @SequenceGenerator(name = "seq_agenda",sequenceName = "seq_agenda_id", allocationSize = 1)
    @GeneratedValue(generator = "seq_agenda",strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private Calendar data_inicio;
    @Column(nullable = false)
    private Calendar data_fim;
    @Column(nullable = false)
    private String observacao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoProduto tipoProduto;

    @ManyToOne
    @JoinColumn(name = "funcionario_cpf")
    private Funcionario funcionario;

    @ManyToOne
    @JoinColumn(name = "medico_cpf")
    private Medico medico;

    public Agenda() {
    }

    public TipoProduto getTipoProduto() {
        return tipoProduto;
    }

    public void setTipoProduto(TipoProduto tipoProduto) {
        this.tipoProduto = tipoProduto;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Calendar getData_inicio() {
        return data_inicio;
    }

    public void setData_inicio(Calendar data_inicio) {
        this.data_inicio = data_inicio;
    }

    public Calendar getData_fim() {
        return data_fim;
    }

    public void setData_fim(Calendar data_fim) {
        this.data_fim = data_fim;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
