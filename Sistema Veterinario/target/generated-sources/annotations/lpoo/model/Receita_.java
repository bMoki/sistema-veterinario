package model;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model.Consulta;
import model.Produto;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2022-02-08T19:57:15", comments="EclipseLink-2.7.7.v20200504-rNA")
@StaticMetamodel(Receita.class)
public class Receita_ { 

    public static volatile SingularAttribute<Receita, String> orientacao;
    public static volatile ListAttribute<Receita, Produto> produtos;
    public static volatile SingularAttribute<Receita, Long> id;
    public static volatile SingularAttribute<Receita, Consulta> consulta;

}