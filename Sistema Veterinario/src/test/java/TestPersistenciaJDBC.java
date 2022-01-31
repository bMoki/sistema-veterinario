import model.*;
import model.dao.PersistenciaJDBC;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TestPersistenciaJDBC {

    @Test
    public void testConexaoGeracaoTabelas() throws Exception {
        PersistenciaJDBC persistencia = new PersistenciaJDBC();
        if(persistencia.conexaoAberta()){
            System.out.println("abriu a conexao com o BD via JDBC");

            persistencia.fecharConexao();

        }else{
            System.out.println("Nao abriu a conexao com o BD via JDBC");
        }
    }

    @Test
    public void testCrudReceitaProduto() throws Exception{
        PersistenciaJDBC persistencia = new PersistenciaJDBC();
        if(persistencia.conexaoAberta()){
            System.out.println("abriu a conexao com o BD via JDBC");
            if(!persistencia.findAll(Receita.class).isEmpty() || !persistencia.findAll(Produto.class).isEmpty()){

                Consulta consulta = new Consulta();
                Fornecedor fornecedor = new Fornecedor();

                if(!persistencia.findAll(Receita.class).isEmpty()){
                    System.out.println("----RECEITAS----\n");
                    for(Receita receita : (List<Receita>) persistencia.findAll(Receita.class)){
                        System.out.println("\nid: "+receita.getId());
                        System.out.println("Orientacao: "+receita.getOrientacao());
                        System.out.println("Consulta id: "+receita.getConsulta().getId());
                        consulta.setId(receita.getConsulta().getId());
                        System.out.println("Produtos: ");
                        for(Produto produto : receita.getProdutos()){
                            System.out.println("\tid: "+produto.getId());
                            System.out.println("\tNome: "+produto.getNome());
                            System.out.println("\tQuantidade: "+produto.getQuantidade());
                            System.out.println("\tValor: "+produto.getValor());
                            System.out.println("\tTipo Produto: "+produto.getTipoProduto());
                            System.out.println("\tFornecedor CPF: "+produto.getFornecedor().getCpf()+"\n");
                        }

                        persistencia.remover(receita);
                    }
                    persistencia.remover(consulta);
                }

                if(!persistencia.findAll(Produto.class).isEmpty()){
                    System.out.println("----PRODUTOS----");
                    for(Produto produto : (List<Produto>)persistencia.findAll(Produto.class)){
                        System.out.println("\nid: "+produto.getId());
                        System.out.println("Nome: "+produto.getNome());
                        System.out.println("Quantidade: "+produto.getQuantidade());
                        System.out.println("Tipo Produto: "+produto.getTipoProduto());
                        System.out.println("Valor: "+produto.getValor());
                        System.out.println("Fornecedor CPF: "+produto.getFornecedor().getCpf());
                        fornecedor.setCpf(produto.getFornecedor().getCpf());

                        persistencia.remover(produto);
                    }
                    persistencia.remover(fornecedor);
                }
            }else{
                Fornecedor fornecedor = new Fornecedor();
                fornecedor.setCep("99700804");
                fornecedor.setComplemento("123");
                fornecedor.setCpf("12345678910");
                fornecedor.setEmail("email@gmail.com");
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd MM yyyy", Locale.ENGLISH);
                calendar.setTime(sdf.parse("01 03 1990"));
                fornecedor.setData_nascimento(calendar);
                fornecedor.setNome("Jose da Silva");
                fornecedor.setNumero_celular("12345678919");
                fornecedor.setRg("12345678910");
                fornecedor.setSenha("123");
                fornecedor.setEndereco("Rua 10");
                fornecedor.setCnpj("1231982123");
                fornecedor.setIe("123");
                Object o = persistencia.persist(fornecedor);
                System.out.println("Fornecedor de CPF "+o.toString()+" cadastrado!");


                Produto produto1 = new Produto();
                produto1.setNome("Pomada");
                produto1.setValor((float)10.50);
                produto1.setQuantidade((float)6);
                produto1.setTipoProduto(TipoProduto.getTipoProduto("MEDICAMENTO"));
                produto1.setFornecedor(fornecedor);
                o = persistencia.persist(produto1);
                produto1.setId(Long.parseLong(o.toString()));
                System.out.println("Produto cadastrado com id "+o.toString());

                Produto produto2 = new Produto();
                produto2.setNome("comprimido");
                produto2.setValor((float)50);
                produto2.setQuantidade((float)20);
                produto2.setTipoProduto(TipoProduto.getTipoProduto("MEDICAMENTO"));
                produto2.setFornecedor(fornecedor);
                o = persistencia.persist(produto2);
                produto2.setId(Long.parseLong(o.toString()));
                System.out.println("Produto cadastrado com id "+o.toString());


                Consulta consulta = new Consulta();
                calendar.setTime(sdf.parse("30 01 2022"));
                consulta.setData(calendar);
                consulta.setData_retorno(calendar);
                consulta.setObservacao("nada");
                consulta.setValor((float)10);
                o = persistencia.persist(consulta);
                consulta.setId(Long.parseLong(o.toString()));
                System.out.println("Consulta cadastrada com id "+o.toString());

                Receita receita = new Receita();
                receita.setOrientacao("1 comprimido por dia");
                List<Produto> produtos = new ArrayList<>();
                produtos.add(produto1);
                produtos.add(produto2);
                receita.setProdutos(produtos);
                receita.setConsulta(consulta);
                o = persistencia.persist(receita);
                System.out.println("Receita cadastrada com id "+o.toString());
            }
        }else{
            System.out.println("Nao abriu a conexao com o BD via JDBC");
        }
    }

    @Test
    public void testFindReceita() throws Exception {
        PersistenciaJDBC persistencia = new PersistenciaJDBC();
        if(persistencia.conexaoAberta()){
            System.out.println("abriu a conexao com o BD via JDBC");

            if(persistencia.find(Receita.class,2) != null){
                Receita receita = (Receita)persistencia.find(Receita.class,2);
                System.out.println("Receita encontrada: ");
                System.out.println("id: "+receita.getId());
                System.out.println("Orientacao: "+receita.getOrientacao());
                System.out.println("Consulta id: "+receita.getConsulta().getId());
                System.out.println("Produtos: ");
                for(Produto produto : receita.getProdutos()){
                    System.out.println("\tid: "+produto.getId());
                    System.out.println("\tNome: "+produto.getNome());
                    System.out.println("\tQuantidade: "+produto.getQuantidade());
                    System.out.println("\tValor: "+produto.getValor());
                    System.out.println("\tTipo Produto: "+produto.getTipoProduto());
                    System.out.println("\tFornecedor CPF: "+produto.getFornecedor().getCpf()+"\n");
                }

            }else{
                System.out.println("Receita nao encontrada");
            }

            persistencia.fecharConexao();

        }else{
            System.out.println("Nao abriu a conexao com o BD via JDBC");
        }
}

    @Test
    public void testFindProduto() throws Exception {
        PersistenciaJDBC persistencia = new PersistenciaJDBC();
        if(persistencia.conexaoAberta()){
            System.out.println("abriu a conexao com o BD via JDBC");

            if(persistencia.find(Produto.class,1) != null){
                Produto produto = (Produto)persistencia.find(Produto.class,1);
                System.out.println("Produto encontrado: ");
                System.out.println("id: "+produto.getId());
                System.out.println("Nome: "+produto.getNome());
                System.out.println("Quantidade: "+produto.getQuantidade());
                System.out.println("Tipo Produto: "+produto.getTipoProduto());
                System.out.println("Valor: "+produto.getValor());
                System.out.println("Fornecedor CPF: "+produto.getFornecedor().getCpf());

            }else{
                System.out.println("Produto nao encontrada");
            }
            persistencia.fecharConexao();

        }else{
            System.out.println("Nao abriu a conexao com o BD via JDBC");
        }
    }
}

