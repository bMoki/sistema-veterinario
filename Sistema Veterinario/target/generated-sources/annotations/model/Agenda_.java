package model;

import java.util.Calendar;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model.Funcionario;
import model.Medico;
import model.TipoProduto;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2021-12-27T16:23:07", comments="EclipseLink-2.7.7.v20200504-rNA")
@StaticMetamodel(Agenda.class)
public class Agenda_ { 

    public static volatile SingularAttribute<Agenda, Calendar> data_inicio;
    public static volatile SingularAttribute<Agenda, Calendar> data_fim;
    public static volatile SingularAttribute<Agenda, String> observacao;
    public static volatile SingularAttribute<Agenda, Medico> medico;
    public static volatile SingularAttribute<Agenda, Long> id;
    public static volatile SingularAttribute<Agenda, Funcionario> funcionario;
    public static volatile SingularAttribute<Agenda, TipoProduto> tipoProduto;

}