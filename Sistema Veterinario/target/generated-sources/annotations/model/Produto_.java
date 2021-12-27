package model;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model.Fornecedor;
import model.TipoProduto;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2021-12-27T16:23:07", comments="EclipseLink-2.7.7.v20200504-rNA")
@StaticMetamodel(Produto.class)
public class Produto_ { 

    public static volatile SingularAttribute<Produto, Float> valor;
    public static volatile SingularAttribute<Produto, String> nome;
    public static volatile SingularAttribute<Produto, Long> id;
    public static volatile SingularAttribute<Produto, Fornecedor> fornecedor;
    public static volatile SingularAttribute<Produto, TipoProduto> tipoProduto;
    public static volatile SingularAttribute<Produto, Float> quantidade;

}