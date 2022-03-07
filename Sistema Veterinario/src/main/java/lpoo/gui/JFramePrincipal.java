package lpoo.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class JFramePrincipal extends JFrame implements WindowListener {

    public CardLayout cardLayout;
    public JPanel jPanel;

    public JFramePrincipal(){
        initComponents();
    }

    private void initComponents() {

        this.setTitle("Sistema Veterinario");

        this.setMinimumSize(new Dimension(600,600));

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        this.addWindowListener(this);

        cardLayout = new CardLayout();
        jPanel = new JPanel();

        jPanel.setLayout(cardLayout);

        this.add(jPanel);

    }

    public void addTela(JPanel p, String nome){

        jPanel.add(p, nome);
    }

    public void showTela(String nome){

        cardLayout.show(jPanel, nome);
    }



    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        System.out.println("Fechando o jframe ..");
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
