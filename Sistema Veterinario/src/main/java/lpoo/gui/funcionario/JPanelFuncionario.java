package lpoo.gui.funcionario;

import lpoo.Controle;

import javax.swing.*;
import java.awt.*;

public class JPanelFuncionario extends JPanel {
    private CardLayout cardLayout;
    private Controle controle;

    private JPanelFuncionarioForm formulario;
    private JPanelFuncionarioListagem listagem;

    public JPanelFuncionario(Controle controle){
        this.controle = controle;
        initComponents();
    }

    private void initComponents(){
        cardLayout = new CardLayout();
        this.setLayout(cardLayout);

        formulario = new JPanelFuncionarioForm(this,controle);
        listagem = new JPanelFuncionarioListagem(this,controle);

        this.add(getFormulario(),"tela_funcionario_form");
        this.add(listagem,"tela_funcionario_listagem");


    }

    public void showTela(String nomeTela){

        if(nomeTela.equals("tela_funcionario_listagem")){

            listagem.populaTable();

        }else if(nomeTela.equals("tela_funcionario_form")){

            getFormulario().populaComboCargo();
        }

        cardLayout.show(this, nomeTela);
    }

    public JPanelFuncionarioForm getFormulario(){
        return formulario;
    }

    public Controle getControle(){
        return controle;
    }

}
