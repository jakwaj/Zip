package Zipper;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

class Pomoc extends JDialog
{
    Pomoc(JFrame parent)
    {
        super(parent, true);
        this.setTitle("Pomoc");
        this.setBounds(350, 350, 770, 140);

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(false);



        ok = new JButton("OK");
        l1 = new JLabel("Kliknij \"Dodaj\", aby wybrać pliki przeznaczone do skompresowania. Wybrane pliki zostaną wyświetlone na liście.");
        l2 = new JLabel("Aby usunąć z listy zaznaczony plik, wciśnij \"Usuń\".");
        l3 = new JLabel("Po kliknięciu \"Zip\" wybierz nazwę i lokalizację tworzonego pliku i wciśnij \"Kompresuj\".");

        ok.addActionListener(new Listener());

        GroupLayout layout = new GroupLayout(getContentPane());
        this.getContentPane().setLayout(layout);

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(
                layout.createSequentialGroup().addGroup(layout.createParallelGroup().addComponent(l1).addComponent(l2).addComponent(l3).addGap(0)).addComponent(ok, 100, 100, 100));

        layout.setVerticalGroup(
                layout.createSequentialGroup().addComponent(l1).addComponent(l2).addComponent(l3).addGroup(layout.createParallelGroup().addGap(0).addComponent(ok)));

    }

    private JButton ok;
    private JLabel l1;
    private JLabel l2;
    private JLabel l3;


    class Listener implements ActionListener
    {
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    }
}