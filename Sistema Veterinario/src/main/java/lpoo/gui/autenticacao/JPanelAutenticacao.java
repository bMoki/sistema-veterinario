package lpoo.gui.autenticacao;

import lpoo.Controle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JPanelAutenticacao extends JPanel implements ActionListener {
    private Controle controle;
    private GridBagLayout gridLayout;
    private GridBagConstraints posicionador;

    private JLabel lblEmail;
    private JLabel lblSenha;
    private JTextField txfEmail;
    private JPasswordField psfSenha;
    private JButton btnLogar;

    public JPanelAutenticacao(Controle controle){

        this.controle = controle;
        initComponents();
    }

    private void initComponents(){

        gridLayout = new GridBagLayout();
        this.setLayout(gridLayout);

        lblEmail = new JLabel("Email:");
        lblEmail.setFocusable(false);
        lblEmail.setToolTipText("lblEmail");
        posicionador = new GridBagConstraints();
        posicionador.gridy = 0;
        posicionador.gridx = 0;
        this.add(lblEmail, posicionador);

        txfEmail = new JTextField(10);
        txfEmail.setFocusable(true);
        txfEmail.setToolTipText("txfEmail");
        posicionador = new GridBagConstraints();
        posicionador.gridy = 0;
        posicionador.gridx = 1;
        this.add(txfEmail, posicionador);

        lblSenha = new JLabel("Senha:");
        lblSenha.setFocusable(false);
        lblSenha.setToolTipText("lblSenha");

        posicionador = new GridBagConstraints();
        posicionador.gridy = 1;
        posicionador.gridx = 0;
        this.add(lblSenha, posicionador);

        psfSenha = new JPasswordField(10);
        psfSenha.setFocusable(true);
        psfSenha.setToolTipText("psfSenha");
        posicionador = new GridBagConstraints();
        posicionador.gridy = 1;
        posicionador.gridx = 1;
        this.add(psfSenha, posicionador);

        btnLogar = new JButton("Autenticar");
        btnLogar.setFocusable(true);
        btnLogar.setToolTipText("btnLogar");
        posicionador = new GridBagConstraints();
        posicionador.gridy = 2;
        posicionador.gridx = 1;
        btnLogar.addActionListener(this);
        btnLogar.setActionCommand("comando_autenticar");
        this.add(btnLogar, posicionador);

    }


    @Override
    public void actionPerformed(ActionEvent e) {


        if(e.getActionCommand().equals(btnLogar.getActionCommand())){
            if(txfEmail.getText().trim().length() > 4 && new String(psfSenha.getPassword()).trim().length() != 0 ){
                controle.autenticar(txfEmail.getText().trim(), new String(psfSenha.getPassword()).trim());
            }else{
                JOptionPane.showMessageDialog(this, "Informe os dados para CPF e Senha!", "Autenticação", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
