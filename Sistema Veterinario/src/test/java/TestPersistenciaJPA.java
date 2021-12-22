import model.dao.PersistenciaJPA;
import org.junit.Test;

public class TestPersistenciaJPA {
    @Test
    public void testConexaoGeracaoTabelas(){
        PersistenciaJPA persistenciaJPA = new PersistenciaJPA();
        if(persistenciaJPA.conexaoAberta()){
            System.out.println("abriu a conexão com o BD via JPA");
            persistenciaJPA.fecharConexao();
        }else{
            System.out.println("Não abriu a conexão com o BD via JPA");
        }
    }
}
