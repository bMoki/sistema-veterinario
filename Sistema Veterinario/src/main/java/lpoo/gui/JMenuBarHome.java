package lpoo.gui;

import lpoo.Controle;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class JMenuBarHome extends JMenuBar implements ActionListener {
    private JMenu menuArquivo;
    private JMenuItem menuItemSair;

    private JMenu menuCadastro;
    private JMenuItem menuItemFuncionario;
    private JMenuItem menuItemProduto;
    private JMenuItem menuItemReceita;

    private Controle controle;

    public JMenuBarHome(Controle controle){

        this.controle = controle;

        initComponents();
    }

    private void initComponents(){

        menuArquivo = new JMenu("Arquivo");
        menuArquivo.setMnemonic(KeyEvent.VK_A);//ativa o ALT + A para acessar esse menu - acessibilidade
        menuArquivo.setToolTipText("Arquivo");
        menuArquivo.setFocusable(true);


        menuItemSair = new JMenuItem("Sair");
        menuItemSair.setToolTipText("Sair");
        menuItemSair.setFocusable(true);

        menuItemSair.addActionListener(this);
        menuItemSair.setActionCommand("menu_sair");
        menuArquivo.add(menuItemSair);

        menuCadastro = new JMenu("Cadastros");
        menuCadastro.setMnemonic(KeyEvent.VK_C);//ativa o ALT + C para acessar esse menu - acessibilidade
        menuCadastro.setToolTipText("Cadastro");
        menuCadastro.setFocusable(true);

        //FUNCIONARIO-----------------------------------------------
        menuItemFuncionario = new JMenuItem("Funcionario");
        menuItemFuncionario.setToolTipText("Funcionario"); //acessibilidade
        menuItemFuncionario.setFocusable(true); //acessibilidade

        menuItemFuncionario.addActionListener(this);
        menuItemFuncionario.setActionCommand("menu_pessoa");
        menuCadastro.add(menuItemFuncionario);
        //PRODUTO----------------------------------------------------
        menuItemProduto = new JMenuItem("Produto");
        menuItemProduto.setToolTipText("Produto");
        menuItemProduto.setFocusable(true);

        menuItemProduto.addActionListener(this);
        menuItemProduto.setActionCommand("menu_produto");
        menuCadastro.add(menuItemProduto);
        //RECEITA----------------------------------------------------
        menuItemReceita = new JMenuItem("Receita");
        menuItemReceita.setToolTipText("Receita");
        menuItemReceita.setFocusable(true);

        menuItemReceita.addActionListener(this);
        menuItemReceita.setActionCommand("menu_receita");
        menuCadastro.add(menuItemReceita);



        this.add(menuArquivo);
        this.add(menuCadastro);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getActionCommand().equals(menuItemSair.getActionCommand())){

            int d = JOptionPane.showConfirmDialog(this, "Deseja realmente sair do sistema? ", "Sair", JOptionPane.YES_NO_OPTION);
            if(d == 0){
                controle.fecharBD();
                System.exit(0);
            }

        }else if(e.getActionCommand().equals(menuItemFuncionario.getActionCommand())){
            controle.showTela("tela_funcionario");
        }else if(e.getActionCommand().equals(menuItemProduto.getActionCommand())){
            controle.showTela("tela_produto");
        }else if(e.getActionCommand().equals(menuItemReceita.getActionCommand())){
            controle.showTela("tela_receita");
        }

    }
}
