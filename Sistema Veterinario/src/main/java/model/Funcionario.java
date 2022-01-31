package model;

import javax.persistence.*;
import java.util.List;

@Entity
@DiscriminatorValue("FU")
public class Funcionario extends Pessoa {

    @Column(nullable = false)
    private String numero_ctps;
    @Column(nullable = false)
    private String numero_pis;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Cargo cargo;

    @OneToMany(mappedBy = "funcionario")
    private List<Venda> vendas;

    @OneToMany(mappedBy = "funcionario")
    private List<Agenda> agendas;

    public Funcionario() {
    }

    public List<Venda> getVendas() {
        return vendas;
    }

    public void setVendas(List<Venda> vendas) {
        this.vendas = vendas;
    }

    public List<Agenda> getAgendas() {
        return agendas;
    }

    public void setAgendas(List<Agenda> agendas) {
        this.agendas = agendas;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public String getNumero_ctps() {
        return numero_ctps;
    }

    public void setNumero_ctps(String numero_ctps) {
        this.numero_ctps = numero_ctps;
    }

    public String getNumero_pis() {
        return numero_pis;
    }

    public void setNumero_pis(String numero_pis) {
        this.numero_pis = numero_pis;
    }
}
