package lpoo.model;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import lpoo.model.Raca;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2022-03-07T16:20:37", comments="EclipseLink-2.7.7.v20200504-rNA")
@StaticMetamodel(Especie.class)
public class Especie_ { 

    public static volatile ListAttribute<Especie, Raca> racas;
    public static volatile SingularAttribute<Especie, String> nome;
    public static volatile SingularAttribute<Especie, Long> id;

}