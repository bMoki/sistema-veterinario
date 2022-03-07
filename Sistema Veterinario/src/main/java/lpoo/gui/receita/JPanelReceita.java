package lpoo.gui.receita;

import lpoo.Controle;

import javax.swing.*;
import java.awt.*;

public class JPanelReceita extends JPanel {
    private CardLayout cardLayout;
    private Controle controle;

    private JPanelReceitaForm formulario;
    private JPanelReceitaListagem listagem;

    public JPanelReceita(Controle controle){
        this.controle = controle;
        initComponents();
    }

    private void initComponents(){
        cardLayout = new CardLayout();
        this.setLayout(cardLayout);

        formulario = new JPanelReceitaForm(this,controle);
        listagem = new JPanelReceitaListagem(this,controle);

        this.add(getFormulario(),"tela_receita_form");
        this.add(listagem,"tela_receita_listagem");

    }

    public void showTela(String nomeTela){

        if(nomeTela.equals("tela_receita_listagem")){

            listagem.populaTable();

        }else if(nomeTela.equals("tela_receita_form")){

            getFormulario().populaComboConsultas();
        }

        cardLayout.show(this, nomeTela);
    }

    public JPanelReceitaForm getFormulario(){
        return formulario;
    }

    public Controle getControle(){
        return controle;
    }
}
