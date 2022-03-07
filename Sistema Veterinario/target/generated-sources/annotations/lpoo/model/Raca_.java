package lpoo.model;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import lpoo.model.Especie;
import lpoo.model.Pet;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2022-03-07T16:20:37", comments="EclipseLink-2.7.7.v20200504-rNA")
@StaticMetamodel(Raca.class)
public class Raca_ { 

    public static volatile SingularAttribute<Raca, Especie> especie;
    public static volatile ListAttribute<Raca, Pet> pets;
    public static volatile SingularAttribute<Raca, String> nome;
    public static volatile SingularAttribute<Raca, Long> id;

}