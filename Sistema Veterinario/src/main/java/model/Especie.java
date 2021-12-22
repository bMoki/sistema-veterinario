package model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Especie {
    @Id
    @SequenceGenerator(name = "seq_especie",sequenceName = "seq_especie_id", allocationSize = 1)
    @GeneratedValue(generator = "seq_especie",strategy = GenerationType.SEQUENCE)
    private Long id;

    private String nome;

    @OneToMany(mappedBy = "especie")
    private List<Raca> racas;

    public Especie() {
    }

    public List<Raca> getRacas() {
        return racas;
    }

    public void setRacas(List<Raca> racas) {
        this.racas = racas;
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
