package model;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model.Produto;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2022-02-08T19:57:15", comments="EclipseLink-2.7.7.v20200504-rNA")
@StaticMetamodel(Fornecedor.class)
public class Fornecedor_ extends Pessoa_ {

    public static volatile ListAttribute<Fornecedor, Produto> produtos;
    public static volatile SingularAttribute<Fornecedor, String> cnpj;
    public static volatile SingularAttribute<Fornecedor, String> ie;

}