package model;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model.Agenda;
import model.Cargo;
import model.Venda;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2022-01-26T09:42:55", comments="EclipseLink-2.7.7.v20200504-rNA")
@StaticMetamodel(Funcionario.class)
public class Funcionario_ { 

    public static volatile ListAttribute<Funcionario, Agenda> agendas;
    public static volatile ListAttribute<Funcionario, Venda> vendas;
    public static volatile SingularAttribute<Funcionario, String> numero_pis;
    public static volatile SingularAttribute<Funcionario, Cargo> cargo;
    public static volatile SingularAttribute<Funcionario, String> numero_ctps;

}