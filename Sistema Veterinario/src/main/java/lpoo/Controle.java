package lpoo;

import lpoo.gui.JFramePrincipal;
import lpoo.gui.JMenuBarHome;
import lpoo.gui.JPanelHome;
import lpoo.gui.autenticacao.JPanelAutenticacao;
import lpoo.gui.funcionario.JPanelFuncionario;
import lpoo.gui.produto.JPanelProduto;
import lpoo.gui.receita.JPanelReceita;
import lpoo.model.Pessoa;
import lpoo.model.dao.PersistenciaJDBC;

import javax.swing.*;

public class Controle {

    private PersistenciaJDBC conexaoJDBC;

    private JFramePrincipal frame;

    private JPanelAutenticacao pnlAutenticacao;

    private JMenuBarHome menuBar;

    private JPanelHome pnlHome;

    private JPanelFuncionario pnlFuncionario;
    private JPanelProduto pnlProduto;
    private JPanelReceita pnlReceita;

    //construtor.
    public Controle(){

    }

    public boolean conectarBD() throws Exception {

        conexaoJDBC = new PersistenciaJDBC();

        if(conexaoJDBC!= null){

            return conexaoJDBC.conexaoAberta();
        }

        return false;
    }

    public void fecharBD(){

        System.out.println("Fechando conexao com o Banco de Dados");
        conexaoJDBC.fecharConexao();

    }

    public void initComponents(){

        frame = new JFramePrincipal();

        pnlAutenticacao = new JPanelAutenticacao(this);

        menuBar = new JMenuBarHome(this);

        pnlHome = new JPanelHome(this);

        pnlFuncionario = new JPanelFuncionario(this);
        pnlProduto = new JPanelProduto(this);
        pnlReceita = new JPanelReceita(this);

        frame.addTela(pnlAutenticacao, "tela_autenticacao");
        frame.addTela(pnlHome, "tela_home");
        frame.addTela(pnlFuncionario,"tela_funcionario");
        frame.addTela(pnlProduto,"tela_produto");
        frame.addTela(pnlReceita, "tela_receita");

        frame.showTela("tela_autenticacao");

        frame.setVisible(true);


    }

    public void autenticar(String email, String senha) {

        try{

            Pessoa pessoa =  conexaoJDBC.doLogin(email, senha);

            if(pessoa != null){
                if(pessoa.getSenha().equals(senha)){
                    JOptionPane.showMessageDialog(pnlAutenticacao, "Pessoa "+ pessoa.getNome()+" autenticado com sucesso!", "Autenticação", JOptionPane.INFORMATION_MESSAGE);

                    frame.setJMenuBar(menuBar);
                    frame.showTela("tela_home");
                }else{
                    JOptionPane.showMessageDialog(pnlAutenticacao, "Senha inválida!", "Autenticação", JOptionPane.INFORMATION_MESSAGE);
                }

            }else{

                JOptionPane.showMessageDialog(pnlAutenticacao, "Email não cadastrado!", "Autenticação", JOptionPane.INFORMATION_MESSAGE);
            }

        }catch(Exception e){

            JOptionPane.showMessageDialog(pnlAutenticacao, "Erro ao executar a autenticação no Banco de Dados!", "Autenticação", JOptionPane.ERROR_MESSAGE);
        }

    }

    public void showTela(String nomeTela){
        if(nomeTela.equals("tela_funcionario")){
            pnlFuncionario.getFormulario().populaComboCargo();
        }else if(nomeTela.equals("tela_produto")){
            pnlProduto.getFormulario().populaComboTipoProduto();
            pnlProduto.getFormulario().populaComboFornecedores();
        }else if(nomeTela.equals("tela_receita")){
            pnlReceita.getFormulario().populaComboConsultas();
            pnlReceita.getFormulario().populaTabelaProdutosBD();
        }
        frame.showTela(nomeTela);
    }

    public  PersistenciaJDBC getConexaoJDBC(){
        return conexaoJDBC;
    }


}