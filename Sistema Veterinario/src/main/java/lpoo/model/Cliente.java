package model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Calendar;
import java.util.List;

@Entity
@DiscriminatorValue("C")
public class Cliente extends Pessoa {

    @Column
    private Calendar data_ultima_visita;

    @OneToMany(mappedBy = "cliente")
    private List<Pet> pets;

    @OneToMany(mappedBy = "cliente")
    private List<Venda> vendas;

    public Cliente() {
    }

    public List<Venda> getVendas() {
        return vendas;
    }

    public void setVendas(List<Venda> vendas) {
        this.vendas = vendas;
    }

    public List<Pet> getPets() {
        return pets;
    }

    public void setPets(List<Pet> pets) {
        this.pets = pets;
    }

    public Calendar getData_ultima_visita() {
        return data_ultima_visita;
    }

    public void setData_ultima_visita(Calendar data_ultima_visita) {
        this.data_ultima_visita = data_ultima_visita;
    }
}
