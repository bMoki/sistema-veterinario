package lpoo.gui.produto;

import lpoo.Controle;

import javax.swing.*;
import java.awt.*;

public class JPanelProduto extends JPanel {
    private CardLayout cardLayout;
    private Controle controle;

    private JPanelProdutoForm formulario;
    private JPanelProdutoListagem listagem;

    public JPanelProduto(Controle controle){
        this.controle = controle;
        initComponents();
    }

    private void initComponents(){
        cardLayout = new CardLayout();
        this.setLayout(cardLayout);

        formulario = new JPanelProdutoForm(this,controle);
        listagem = new JPanelProdutoListagem(this,controle);

        this.add(getFormulario(),"tela_produto_form");
        this.add(listagem,"tela_produto_listagem");

    }

    public void showTela(String nomeTela){

        if(nomeTela.equals("tela_produto_listagem")){

            listagem.populaTable();

        }else if(nomeTela.equals("tela_produto_form")){

            getFormulario().populaComboTipoProduto();
        }

        cardLayout.show(this, nomeTela);
    }

    public JPanelProdutoForm getFormulario(){
        return formulario;
    }

    public Controle getControle(){
        return controle;
    }
}
