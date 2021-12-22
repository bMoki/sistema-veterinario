package model;

import javax.persistence.*;
import java.util.Calendar;

@Entity
public class Pet {
    @Id
    @SequenceGenerator(name = "seq_pet",sequenceName = "seq_pet_id", allocationSize = 1)
    @GeneratedValue(generator = "seq_pet",strategy = GenerationType.SEQUENCE)
    private Long id;

    private String nome;
    private Calendar data_nascimento;
    private String observacao;

    @ManyToOne
    @JoinColumn(name = "raca_id")
    private Raca raca;

    @ManyToOne
    @JoinColumn(name = "cliente_cpf")
    private Cliente cliente;

    public Cliente getCliente() {
        return cliente;
    }

    public Pet() {
    }

    public Raca getRaca() {
        return raca;
    }

    public void setRaca(Raca raca) {
        this.raca = raca;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Calendar getData_nascimento() {
        return data_nascimento;
    }

    public void setData_nascimento(Calendar data_nascimento) {
        this.data_nascimento = data_nascimento;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
