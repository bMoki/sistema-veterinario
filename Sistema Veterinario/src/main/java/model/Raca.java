package model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Raca {
    @Id
    @SequenceGenerator(name = "seq_raca",sequenceName = "seq_raca_id", allocationSize = 1)
    @GeneratedValue(generator = "seq_raca",strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(nullable = false)
    private String nome;

    @ManyToOne
    @JoinColumn(name = "especie_id")
    private Especie especie;

    @OneToMany(mappedBy = "raca")
    private List<Pet> pets;

    public Raca() {
    }

    public List<Pet> getPets() {
        return pets;
    }

    public void setPets(List<Pet> pets) {
        this.pets = pets;
    }

    public Especie getEspecie() {
        return especie;
    }

    public void setEspecie(Especie especie) {
        this.especie = especie;
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
}
