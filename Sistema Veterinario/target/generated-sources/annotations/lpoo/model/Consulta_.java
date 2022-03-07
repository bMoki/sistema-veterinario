package model;

import java.util.Calendar;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model.Medico;
import model.Pet;
import model.Receita;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2022-02-08T19:57:15", comments="EclipseLink-2.7.7.v20200504-rNA")
@StaticMetamodel(Consulta.class)
public class Consulta_ { 

    public static volatile SingularAttribute<Consulta, String> observacao;
    public static volatile SingularAttribute<Consulta, Calendar> data;
    public static volatile SingularAttribute<Consulta, Calendar> data_retorno;
    public static volatile SingularAttribute<Consulta, Medico> medico;
    public static volatile SingularAttribute<Consulta, Float> valor;
    public static volatile ListAttribute<Consulta, Receita> receitas;
    public static volatile SingularAttribute<Consulta, Long> id;
    public static volatile SingularAttribute<Consulta, Pet> pet;

}