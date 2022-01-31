package model;

import java.util.Calendar;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model.Cliente;
import model.Consulta;
import model.Funcionario;
import model.Pagamento;
import model.Produto;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2022-01-18T15:14:51", comments="EclipseLink-2.7.7.v20200504-rNA")
@StaticMetamodel(Venda.class)
public class Venda_ { 

    public static volatile SingularAttribute<Venda, Float> valor_total;
    public static volatile SingularAttribute<Venda, Cliente> cliente;
    public static volatile SingularAttribute<Venda, String> observacao;
    public static volatile SingularAttribute<Venda, Calendar> data;
    public static volatile ListAttribute<Venda, Produto> produtos;
    public static volatile ListAttribute<Venda, Consulta> consultas;
    public static volatile SingularAttribute<Venda, Long> id;
    public static volatile SingularAttribute<Venda, Funcionario> funcionario;
    public static volatile SingularAttribute<Venda, Pagamento> pagamento;

}