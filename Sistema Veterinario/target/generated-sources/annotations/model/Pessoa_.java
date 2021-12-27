package model;

import java.util.Calendar;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2021-12-27T16:23:07", comments="EclipseLink-2.7.7.v20200504-rNA")
@StaticMetamodel(Pessoa.class)
public class Pessoa_ { 

    public static volatile SingularAttribute<Pessoa, String> senha;
    public static volatile SingularAttribute<Pessoa, String> complemento;
    public static volatile SingularAttribute<Pessoa, String> endereco;
    public static volatile SingularAttribute<Pessoa, String> rg;
    public static volatile SingularAttribute<Pessoa, Calendar> data_nascimento;
    public static volatile SingularAttribute<Pessoa, String> cpf;
    public static volatile SingularAttribute<Pessoa, String> numero_celular;
    public static volatile SingularAttribute<Pessoa, String> nome;
    public static volatile SingularAttribute<Pessoa, String> email;
    public static volatile SingularAttribute<Pessoa, String> cep;

}