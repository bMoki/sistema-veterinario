package lpoo.model;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import lpoo.model.Agenda;
import lpoo.model.Consulta;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2022-03-09T15:33:52", comments="EclipseLink-2.7.7.v20200504-rNA")
@StaticMetamodel(Medico.class)
public class Medico_ extends Pessoa_ {

    public static volatile SingularAttribute<Medico, String> numero_crmv;
    public static volatile ListAttribute<Medico, Agenda> agendas;
    public static volatile ListAttribute<Medico, Consulta> consultas;

}