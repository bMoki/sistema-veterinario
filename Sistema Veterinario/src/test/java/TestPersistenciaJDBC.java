import lpoo.model.*;
import lpoo.model.dao.PersistenciaJDBC;
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
                fornecedor.setCpf("123.456.789-10");
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

                Funcionario funcionario = new Funcionario();
                funcionario.setCep("99700804");
                funcionario.setComplemento("123");
                funcionario.setCpf("051.042.760-07");
                funcionario.setEmail("funcionario@gmail.com");
                calendar = Calendar.getInstance();
                calendar.setTime(sdf.parse("01 03 1990"));
                funcionario.setData_nascimento(calendar);
                funcionario.setNome("Breno Sonda");
                funcionario.setNumero_celular("12345678919");
                funcionario.setRg("12345678910");
                funcionario.setSenha("1234");
                funcionario.setEndereco("Rua 10");
                funcionario.setNumero_ctps("12345");
                funcionario.setNumero_pis("123123");
                funcionario.setCargo(Cargo.ATENDENTE);

                o = persistencia.persist(funcionario);
                System.out.println("Funcionario de CPF "+o.toString()+" cadastrado!");

                Cliente cliente = new Cliente();
                cliente.setCep("99700804");
                cliente.setComplemento("123");
                cliente.setCpf("029.384.758-42");
                cliente.setEmail("funcionario@gmail.com");
                calendar = Calendar.getInstance();
                calendar.setTime(sdf.parse("01 03 1990"));
                cliente.setData_nascimento(calendar);
                cliente.setNome("Breno Sonda");
                cliente.setNumero_celular("12345678919");
                cliente.setRg("12345678910");
                cliente.setSenha("1234");
                cliente.setEndereco("Rua 10");
                calendar.setTime(sdf.parse("02 02 2022"));
                cliente.setData_ultima_visita(calendar);

                o = persistencia.persist(cliente);
                System.out.println("Cliente de CPF "+o.toString()+" cadastrado!");

                Medico medico = new Medico();
                medico.setCep("99700804");
                medico.setComplemento("123");
                medico.setCpf("021.364.118-22");
                medico.setEmail("funcionario@gmail.com");
                calendar = Calendar.getInstance();
                calendar.setTime(sdf.parse("01 03 1990"));
                medico.setData_nascimento(calendar);
                medico.setNome("Laura Sonda");
                medico.setNumero_celular("12345678919");
                medico.setRg("12345678910");
                medico.setSenha("1234");
                medico.setEndereco("Rua 10");
                medico.setNumero_crmv("12345");

                o = persistencia.persist(medico);
                System.out.println("Medico de CPF "+o.toString()+" cadastrado!");

                Venda venda = new Venda();
                venda.setValor_total(15.50F);
                venda.setObservacao("muito loco");
                venda.setPagamento(Pagamento.PIX);
                venda.setFuncionario(funcionario);
                venda.setCliente(cliente);

                o = persistencia.persist(venda);
                System.out.println("Venda de ID "+o.toString()+" cadastrada!");

                Agenda agenda = new Agenda();
                calendar.setTime(sdf.parse("01 01 2022"));
                agenda.setData_inicio(calendar);
                calendar.setTime(sdf.parse("15 01 2022"));
                agenda.setData_fim(calendar);
                agenda.setTipoProduto(TipoProduto.SESSAO_FISIOTERAPIA);
                agenda.setObservacao("repetir os alongamentos em casa tb");
                agenda.setFuncionario(funcionario);
                agenda.setMedico(medico);

                o = persistencia.persist(agenda);
                System.out.println("Agenda de ID "+o.toString()+ " cadastrada!");

                venda.setValor_total(20F);
                venda.setObservacao("cachorro mau");
                venda.setPagamento(Pagamento.BOLETO);
                venda.setFuncionario(funcionario);
                o = persistencia.persist(venda);
                System.out.println("Venda de ID "+o.toString()+" cadastrada!");

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

