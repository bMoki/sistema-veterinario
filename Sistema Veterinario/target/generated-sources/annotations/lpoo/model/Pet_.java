package model;

import java.util.Calendar;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model.Cliente;
import model.Consulta;
import model.Raca;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2022-02-08T19:57:15", comments="EclipseLink-2.7.7.v20200504-rNA")
@StaticMetamodel(Pet.class)
public class Pet_ { 

    public static volatile SingularAttribute<Pet, Cliente> cliente;
    public static volatile SingularAttribute<Pet, String> observacao;
    public static volatile SingularAttribute<Pet, Calendar> data_nascimento;
    public static volatile SingularAttribute<Pet, Raca> raca;
    public static volatile ListAttribute<Pet, Consulta> consultas;
    public static volatile SingularAttribute<Pet, String> nome;
    public static volatile SingularAttribute<Pet, Long> id;

}