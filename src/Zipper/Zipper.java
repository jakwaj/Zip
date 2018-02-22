package Zipper;

import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.swing.*;

public class Zipper extends JFrame {

    public Zipper()
    {
        this.setTitle("Zipper v1.0");
        this.setBounds(500, 300, 300, 300);
        this.setJMenuBar(pasekMenu);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenu menuInfo = pasekMenu.add(new JMenu("Info"));

        Action akcjaDodawania = new Akcja("Dodaj");
        Action akcjaUsuwania = new Akcja("Usuń");
        Action akcjaZipowania = new Akcja("Zip");
        Action akcjaPomoc = new Akcja("Pomoc");
        Action akcjaOAutorze = new Akcja("O autorze");

        menuInfo.add(akcjaOAutorze);
        menuInfo.add(akcjaPomoc);

        bDodaj = new JButton(akcjaDodawania);
        bUsun = new JButton(akcjaUsuwania);
        bZip = new JButton(akcjaZipowania);
        bOAutorze = new JButton(akcjaZipowania);
        bPomoc = new JButton(akcjaZipowania);

        JScrollPane scroll = new JScrollPane(lista);

        lista.setBorder(BorderFactory.createEtchedBorder());

        GroupLayout layout = new GroupLayout(this.getContentPane());
        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);
        layout.setHorizontalGroup(
                layout.createSequentialGroup().addComponent(scroll, 100, 150, Short.MAX_VALUE)
                        .addContainerGap(0, Short.MAX_VALUE).addGroup(
                        layout.createParallelGroup().addComponent(bDodaj).addComponent(bUsun).addComponent(bZip)));

        layout.setVerticalGroup(
                layout.createParallelGroup()
                        .addComponent(scroll, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
                                Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addComponent(bDodaj)
                        .addComponent(bUsun).addGap(5, 40, Short.MAX_VALUE).addComponent(bZip)));

        this.getContentPane().setLayout(layout);

    }

    private DefaultListModel modelListy = new DefaultListModel()
    {
        @Override
        public void addElement(Object obj) {
            lista.add(obj);
            super.addElement(((File)obj).getName());
        }

        ArrayList lista = new ArrayList();

        @Override
        public Object get(int index) {
            return lista.get(index);
        }

        @Override
        public Object remove(int index)
        {
            lista.remove(index);
            return super.remove(index);
        }
    };

    private JList lista = new JList(modelListy);
    private JButton bDodaj;
    private JButton bUsun;
    private JButton bZip;
    private JButton bPomoc;
    private JButton bOAutorze;
    private JMenuBar pasekMenu = new JMenuBar();
    private JFileChooser chooser = new JFileChooser();

    private class Akcja extends AbstractAction
    {
        public Akcja(String nazwa)
        {
            this.putValue(Action.NAME, nazwa);
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (e.getActionCommand().equals("Dodaj"))
                dodajWpisDoArchiwum();
            else if (e.getActionCommand().equals("Usuń"))
                usuwanieWpisowZListy();
            else if (e.getActionCommand().equals("Zip"))
            {
                if (!(modelListy.getSize() == 0))
                    stworzArchiwumZip();
            }
            else if (e.getActionCommand().equals("O autorze"))
                new OAutorze().setVisible(true);
            else if (e.getActionCommand().equals("Pomoc"))
                new Pomoc().setVisible(true);
        }

        private void dodajWpisDoArchiwum()
        {
            chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            chooser.setMultiSelectionEnabled(true);
            int tmp = chooser.showDialog(rootPane, "Dodaj do archiwum");

            if (tmp == JFileChooser.APPROVE_OPTION)
            {
                File[] sciezki = chooser.getSelectedFiles();

                for (int i = 0 ; i < sciezki.length ; i++)
                {
                    if (!czySiePowtarza(sciezki[i].getPath()))
                    modelListy.addElement(sciezki[i]);
                }
            }
        }

        private boolean czySiePowtarza(String testowanyWpis)
        {
            for (int i = 0 ; i < modelListy.getSize() ; i++)
            {
                if (((File)modelListy.get(i)).getPath().equals(testowanyWpis))
                    return true;
            }
            return false;
        }

        private void usuwanieWpisowZListy()
        {
            int[] tmp = lista.getSelectedIndices();

            for (int i = 0 ; i < tmp.length ; i++)
            {
                modelListy.remove(tmp[i]);
            }
        }

        ArrayList listaSciezek = new ArrayList();

        private void stworzArchiwumZip()
        {
            chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
            chooser.setSelectedFile(new File(System.getProperty("user.dir") + File.separator + "archiwum.zip"));
            int tmp = chooser.showDialog(rootPane, "Kompresuj");

            if (tmp == JFileChooser.APPROVE_OPTION) {
                byte tmpData[] = new byte[BUFFOR];

                try {
                    ZipOutputStream zOutS = new ZipOutputStream(new BufferedOutputStream(
                            new FileOutputStream(chooser.getSelectedFile()), BUFFOR));

                    for (int i = 0; i < modelListy.getSize(); i++) {
                        if (!((File) modelListy.get(i)).isDirectory()) {
                            zipuj(zOutS, ((File) modelListy.get(i)), tmpData, ((File) modelListy.get(i)).getPath());
                        } else {
                            wypiszSciezki((File) modelListy.get(i));

                            for (int j = 0; j < listaSciezek.size(); j++) {
                                zipuj(zOutS, ((File) listaSciezek.get(j)), tmpData, ((File) modelListy.get(i)).getPath());

                                listaSciezek.removeAll(listaSciezek);
                            }
                        }
                    }
                    zOutS.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        private void zipuj(ZipOutputStream zOutS, File sciezkaPliku, byte[] tmpData, String sciezkaBazowa) throws IOException
        {
            BufferedInputStream inS = new BufferedInputStream(new FileInputStream(sciezkaPliku), BUFFOR);

            zOutS.putNextEntry(new ZipEntry(sciezkaPliku.getPath().substring(sciezkaBazowa.lastIndexOf(File.separator)+1)));

            int counter;

            while ((counter = inS.read(tmpData, 0, BUFFOR)) != -1)
                zOutS.write(tmpData, 0, counter);

            zOutS.closeEntry();
            inS.close();
        }

        private static final int BUFFOR = 1024;

        private void wypiszSciezki(File nazwaSciezki)
        {
            String[] nazwyPlikowIKatalogow = nazwaSciezki.list();

            for (int i = 0; i < nazwyPlikowIKatalogow.length; i++)
            {
                File p = new File(nazwaSciezki.getPath(), nazwyPlikowIKatalogow[i]);

                if (p.isFile())
                    listaSciezek.add(p);

                if (p.isDirectory())
                    wypiszSciezki(new File(p.getPath()));
            }
        }
    }

    public static void main(String[] args) {
        new Zipper().setVisible(true);
    }
}


