package model;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model.Raca;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2021-12-27T16:23:07", comments="EclipseLink-2.7.7.v20200504-rNA")
@StaticMetamodel(Especie.class)
public class Especie_ { 

    public static volatile ListAttribute<Especie, Raca> racas;
    public static volatile SingularAttribute<Especie, String> nome;
    public static volatile SingularAttribute<Especie, Long> id;

}