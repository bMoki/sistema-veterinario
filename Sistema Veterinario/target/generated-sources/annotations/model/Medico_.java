package model;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model.Agenda;
import model.Consulta;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2022-01-18T15:14:51", comments="EclipseLink-2.7.7.v20200504-rNA")
@StaticMetamodel(Medico.class)
public class Medico_ extends Pessoa_ {

    public static volatile SingularAttribute<Medico, String> numero_crmv;
    public static volatile ListAttribute<Medico, Agenda> agendas;
    public static volatile ListAttribute<Medico, Consulta> consultas;

}