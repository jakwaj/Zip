package Zipper;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;


class OAutorze extends JDialog
{
    OAutorze(JFrame parent)
    {
        super(parent, true);
        this.setTitle("O autorze");
        this.setBounds(475, 375, 350, 150);

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(false);

        ok = new JButton("OK");
        autor = new JLabel("Autor: Jakub Wajda");
        kontakt = new JLabel("Kontakt: jakubwajda@hotmail.com");

        ok.addActionListener(new Listener());

        Container kontener = this.getContentPane();
        kontener.setLayout(null);
        kontener.add(ok);
        kontener.add(autor);
        kontener.add(kontakt);
        ok.setLocation(125, 75);
        ok.setSize(100, 30);
        kontakt.setLocation(80, 30);
        kontakt.setSize(250, 30);
        autor.setLocation(80, 10);
        autor.setSize(250, 30);

    }

    private JButton ok;
    private JLabel autor;
    private JLabel kontakt;

    class Listener implements ActionListener
    {
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    }
}
