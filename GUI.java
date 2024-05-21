package pl.edu.pg.eti.ksg.po.project2;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GUI implements ActionListener, KeyListener {
    private Toolkit toolkit;
    private Dimension dimension;
    private JFrame jFrame;
    private JMenu menu;
    private JMenuItem newGame, load, save, exit;
    private GrafikaPlanszy planszaGraphics = null;
    private KomentarzeLayout komentatorGraphics = null;
    private Oznaczenia oznaczenia = null;
    private JPanel mainPanel;
    private final int ODSTEP;
    private Swiat swiat;

    public GUI(String title) {
        toolkit = Toolkit.getDefaultToolkit();
        dimension = toolkit.getScreenSize();
        ODSTEP = dimension.height / 100;
        jFrame = new JFrame(title);
        jFrame.setBounds((dimension.width - 800) / 2, (dimension.height - 600) / 2, 1400, 800);
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//okresla co ma sie stac po zamknieciu ramki
        JMenuBar menuBar = new JMenuBar();
        UIManager.put("MenuBar.background", new Color(105,105,105));
        menu = new JMenu("Menu");
        newGame = new JMenuItem("Nowa gra");
        load = new JMenuItem("Wczytaj");
        save = new JMenuItem("Zapisz");
        exit = new JMenuItem("Wyjscie");
        newGame.addActionListener(this);
        load.addActionListener(this);
        save.addActionListener(this);
        exit.addActionListener(this);
        menu.add(newGame);
        menu.add(load);
        menu.add(save);
        menu.add(exit);
        menuBar.add(menu);
        jFrame.setLayout(new CardLayout());
        jFrame.setJMenuBar(menuBar);
        mainPanel = new JPanel();
        Color myColor = new Color(8, 169, 153);
        mainPanel.setBackground(myColor);
        mainPanel.setLayout(null);
        jFrame.addKeyListener(this);
        jFrame.add(mainPanel);
        jFrame.setVisible(true);
    }

    private class Oznaczenia extends JPanel {

        private final int ILOSC_TYPOW = 12;
        private JButton[] jButtons;

        public Oznaczenia() {
            super();
            setBounds(mainPanel.getX() + ODSTEP, mainPanel.getY() + ODSTEP,
                    mainPanel.getWidth() / 9 - ODSTEP * 2,
                    mainPanel.getHeight() - 2 * ODSTEP);
            setBackground(new Color(105, 105, 105));
            setLayout(new GridLayout(ILOSC_TYPOW, 1, 0, 5));

            jButtons = new JButton[ILOSC_TYPOW];
            jButtons[0] = createJButton("<html>Barszcz<br>Sosnowskiego</html>", new Color(128, 0, 128));
            jButtons[1] = createJButton("Guarana", new Color(128, 128, 0));
            jButtons[2] = createJButton("Mlecz", new Color(255, 215, 0));
            jButtons[3] = createJButton("Trawa", Color.GREEN);
            jButtons[4] = createJButton("Wilcze jagody", new Color(139, 0, 0));
            jButtons[5] = createJButton("Antylopa", new Color(0, 139, 139));
            jButtons[6] = createJButton("Czlowiek", Color.BLUE);
            jButtons[7] = createJButton("Lis", new Color(235, 109, 19));
            jButtons[8] = createJButton("Owca", new Color(139, 69, 19));
            jButtons[9] = createJButton("Wilk", new Color(64, 224, 208));
            jButtons[10] = createJButton("Zolw", new Color(0, 100, 0));
            jButtons[11] = createJButton("Cyber owca", Color.BLACK);

            for (int i = 0; i < ILOSC_TYPOW; i++) {
                add(jButtons[i]);
            }
        }

        private JButton createJButton(String text, Color background) {
            JButton jButton = new JButton(text);
            jButton.setPreferredSize(new Dimension(115, 55));
            jButton.setBackground(background);
            jButton.setEnabled(false);
            return jButton;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Nowa gra":
                Komentator.CzyszczenieKomentarzy();
                int sizeX = Integer.parseInt(JOptionPane.showInputDialog(jFrame,
                        "Podaj szerokosc swiata", "30"));
                int sizeY = Integer.parseInt(JOptionPane.showInputDialog(jFrame,
                        "Podaj wysokosc swiata", "30"));
                double zapelnienieSwiatu = Double.parseDouble(JOptionPane.showInputDialog
                        (jFrame, "Podaj zapelnienie swiata(wartosc od 0 do 1)", "0.4"));
                swiat = new Swiat(sizeX, sizeY, this);
                swiat.GenerujSwiat(zapelnienieSwiatu);
                if (planszaGraphics != null)
                    mainPanel.remove(planszaGraphics);
                if (komentatorGraphics != null)
                    mainPanel.remove(komentatorGraphics);
                if (oznaczenia != null)
                    mainPanel.remove(oznaczenia);
                startGame();
                break;
            case "Wczytaj":
                Komentator.CzyszczenieKomentarzy();
                String nameOfFile = JOptionPane.showInputDialog(jFrame, "Podaj nazwe pliku", "zapis1");
                swiat = Swiat.WczytajSwiat(nameOfFile);
                swiat.setSwiatGUI(this);
                planszaGraphics = new GrafikaPlanszy(swiat);
                komentatorGraphics = new KomentarzeLayout();
                oznaczenia = new Oznaczenia();
                if (planszaGraphics != null)
                    mainPanel.remove(planszaGraphics);
                if (komentatorGraphics != null)
                    mainPanel.remove(komentatorGraphics);
                if (oznaczenia != null)
                    mainPanel.remove(oznaczenia);
                startGame();
                break;
            case "Zapisz":
                String nameOfFileS = JOptionPane.showInputDialog(jFrame, "Podaj nazwe pliku", "zapis1");
                swiat.ZapiszSwiat(nameOfFileS);
                Komentator.DodajKomentarz("Zapisano swiat");
                komentatorGraphics.odswiezKomentarze();
                break;
            case "Wyjście":
                jFrame.dispose();
                break;
            default:
                break;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (swiat != null && swiat.isPauza()) {
            int keyCode = e.getKeyCode();
            if (keyCode == KeyEvent.VK_ENTER) {

            } else if (swiat.getCzyCzlowiekZyje()) {
                if (keyCode == KeyEvent.VK_UP) {
                    swiat.getCzlowiek().setKierunekRuchu(Organizm.Kierunek.GORA);
                } else if (keyCode == KeyEvent.VK_DOWN) {
                    swiat.getCzlowiek().setKierunekRuchu(Organizm.Kierunek.DOL);
                } else if (keyCode == KeyEvent.VK_LEFT) {
                    swiat.getCzlowiek().setKierunekRuchu(Organizm.Kierunek.LEWO);
                } else if (keyCode == KeyEvent.VK_RIGHT) {
                    swiat.getCzlowiek().setKierunekRuchu(Organizm.Kierunek.PRAWO);
                } else if (keyCode == KeyEvent.VK_P) {
                    Umiejetnosc tmpUmiejetnosc = swiat.getCzlowiek().getUmiejetnosc();
                    if (tmpUmiejetnosc.getCzyMoznaAktywowac()) {
                        tmpUmiejetnosc.Aktywuj();
                        swiat.getCzlowiek().setSila(10);
                        Komentator.DodajKomentarz("Umiejetnosc zwiekaszania sily aktywowana (Pozostaly" +
                                " czas trwania wynosi " + tmpUmiejetnosc.getCzasTrwania() + " tur)");

                    } else if (tmpUmiejetnosc.getCzyJestAktywna()) {
                        Komentator.DodajKomentarz("Umiejetnosc juz zostala aktywowana " + "(Pozostaly" +
                                " czas trwania wynosi " + tmpUmiejetnosc.getCzasTrwania() + " tur)");
                        komentatorGraphics.odswiezKomentarze();
                        return;
                    } else {
                        Komentator.DodajKomentarz("Umiejetnosc mozna wlaczyc tylko po "
                                + tmpUmiejetnosc.getCooldown() + " turach");
                        komentatorGraphics.odswiezKomentarze();
                        return;
                    }
                } else {
                    Komentator.DodajKomentarz("\nNieoznaczony symbol, sprobuj ponownie");
                    komentatorGraphics.odswiezKomentarze();
                    return;
                }
            } else if (!swiat.getCzyCzlowiekZyje() && (keyCode == KeyEvent.VK_UP ||
                    keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_LEFT ||
                    keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_P)) {
                Komentator.DodajKomentarz("Czlowiek umarl, nie mozesz im wiecej sterowac");
                komentatorGraphics.odswiezKomentarze();
                return;
            } else {
                Komentator.DodajKomentarz("\nNieoznaczony symbol, sprobuj ponownie");
                komentatorGraphics.odswiezKomentarze();
                return;
            }
            Komentator.CzyszczenieKomentarzy();
            swiat.setPauza(false);
            swiat.WykonajTure();
            odswiezSwiat();
            swiat.setPauza(true);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    private class GrafikaPlanszy extends JPanel {
        private final int sizeX;
        private final int sizeY;
        private PolePlanszy[][] polaPlanszy;
        private Swiat SWIAT;

        public GrafikaPlanszy(Swiat swiat) {
            super();
            setBounds(15*(mainPanel.getX() + ODSTEP), mainPanel.getY() + ODSTEP,
                    mainPanel.getHeight() * 8 / 6 - ODSTEP, mainPanel.getHeight()  - 2 * ODSTEP);
            SWIAT = swiat;
            this.sizeX = swiat.getSizeX();
            this.sizeY = swiat.getSizeY();

            polaPlanszy = new PolePlanszy[sizeY][sizeX];
            for (int i = 0; i < sizeY; i++) {
                for (int j = 0; j < sizeX; j++) {
                    polaPlanszy[i][j] = new PolePlanszy(j, i);
                    polaPlanszy[i][j].addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (e.getSource() instanceof PolePlanszy) {
                                PolePlanszy tmpPole = (PolePlanszy) e.getSource();
                                if (tmpPole.isEmpty == true) {
                                    OknoOrganizmow listaOrganizmow = new OknoOrganizmow
                                            (tmpPole.getX() + jFrame.getX(),
                                                    tmpPole.getY() + jFrame.getY(),
                                                    new Punkt(tmpPole.getPozX(), tmpPole.getPozY()));
                                }
                            }
                        }
                    });
                    this.add(polaPlanszy[i][j]);
                }
            }
            this.setLayout(new GridLayout(sizeY, sizeX));
        }

        private class PolePlanszy extends JButton {
            private boolean isEmpty;
            private Color kolor;
            private final int pozX;
            private final int pozY;
            public PolePlanszy(int X, int Y) {
                super();
                kolor = Color.WHITE;
                setBackground(kolor);
                isEmpty = true;
                pozX = X;
                pozY = Y;
            }
            public boolean isEmpty() {
                return isEmpty;
            }
            public void setEmpty(boolean empty) {
                isEmpty = empty;
            }

            public Color getKolor() {
                return kolor;
            }
            public void setKolor(Color kolor) {
                this.kolor = kolor;
                setBackground(kolor);
            }
            public int getPozX() {
                return pozX;
            }

            public int getPozY() {
                return pozY;
            }
        }
        public void odswiezPlansze() {
            for (int i = 0; i < sizeY; i++) {
                for (int j = 0; j < sizeX; j++) {
                    Organizm tmpOrganizm = swiat.getPlansza()[i][j];
                    if (tmpOrganizm != null) {
                        polaPlanszy[i][j].setEmpty(false);
                        polaPlanszy[i][j].setEnabled(false);
                        polaPlanszy[i][j].setKolor(tmpOrganizm.getKolor());
                    } else {
                        polaPlanszy[i][j].setEmpty(true);
                        polaPlanszy[i][j].setEnabled(true);
                        polaPlanszy[i][j].setKolor(Color.WHITE);
                    }
                }
            }
        }

        public int getSizeX() {
            return sizeX;
        }
        public int getSizeY() {
            return sizeY;
        }
        public PolePlanszy[][] getPolaPlanszy() {
            return polaPlanszy;
        }
    }

    private class KomentarzeLayout extends JPanel {
        private String tekst;
        private final String instriction = "Autor: Hubert Szymczak\nStrzalki - ruch czlowieka\n" +
                "P - aktywacja umiejetnosci\nEnter - przejscie do nastepnej tury\n";
        private JTextArea textArea;

        public KomentarzeLayout() {
            super();
            setBounds(planszaGraphics.getX() + planszaGraphics.getWidth() + ODSTEP,
                    mainPanel.getY() + ODSTEP,
                    mainPanel.getWidth()/5 - ODSTEP * 2 ,
                    mainPanel.getHeight()  - 2 * ODSTEP);
            tekst = Komentator.getTekst();
            textArea = new JTextArea(tekst);
            UIManager.put("TextArea.background", new Color(105,105,105));
            UIManager.put("TextArea.foreground", Color.WHITE);
            textArea.setEditable(false);
            setLayout(new BorderLayout()); // zmiana layoutu na BorderLayout

            // dodanie nagłówka
            JLabel headerLabel = new JLabel("KOMENTARZE", SwingConstants.CENTER);
            headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
            headerLabel.setForeground(Color.BLACK);

            add(headerLabel, BorderLayout.NORTH);

            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            textArea.setMargin(new Insets(5, 5, 5, 5));
            JScrollPane sp = new JScrollPane(textArea);
            add(sp, BorderLayout.CENTER);
        }
        public void odswiezKomentarze() {
            tekst = instriction + Komentator.getTekst();
            textArea.setText(tekst);
        }
    }

    private class OknoOrganizmow extends JFrame {
        private String[] listaOrganizmow;
        private Organizm.TypOrganizmu[] typOrganizmuList;
        private JList jList;

        public OknoOrganizmow(int x, int y, Punkt punkt) {
            super("Lista organizmów");
            setBounds(x, y, 200, 300);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setResizable(false);
            setIconImage(new ImageIcon("icon.png").getImage()); //dodanie ikony
            JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
            JLabel headerLabel = new JLabel("Wybierz organizm");
            headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
            headerLabel.setForeground(Color.WHITE);
            headerPanel.setBackground(new Color(50, 50, 50));
            headerPanel.add(headerLabel);
            add(headerPanel, BorderLayout.NORTH);

            listaOrganizmow = new String[]{"Barszcz Sosnowskiego", "Guarana", "Mlecz", "Trawa",
                    "Wilcze jagody", "Antylopa", "Lis", "Owca", "Wilk", "Zolw", "Cyber owca"};
            typOrganizmuList = new Organizm.TypOrganizmu[]{Organizm.TypOrganizmu.BARSZCZ_SOSNOWSKIEGO,
                    Organizm.TypOrganizmu.GUARANA, Organizm.TypOrganizmu.MLECZ, Organizm.TypOrganizmu.TRAWA,
                    Organizm.TypOrganizmu.WILCZE_JAGODY, Organizm.TypOrganizmu.ANTYLOPA,
                    Organizm.TypOrganizmu.LIS,
                    Organizm.TypOrganizmu.OWCA, Organizm.TypOrganizmu.WILK,
                    Organizm.TypOrganizmu.ZOLW, Organizm.TypOrganizmu.CYBER_OWCA
            };

            jList = new JList(listaOrganizmow);
            jList.setBackground(new Color(150, 150, 150));
            jList.setForeground(Color.WHITE);
            jList.setFont(new Font("Arial", Font.PLAIN, 16));
            jList.setVisibleRowCount(listaOrganizmow.length);
            jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            jList.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    Organizm tmpOrganizm = FabrykaOrganizmow.StworzNowyOrganizm
                            (typOrganizmuList[jList.getSelectedIndex()], swiat, punkt);
                    swiat.DodajOrganizm(tmpOrganizm);
                    Komentator.DodajKomentarz("Stworzono nowy organizm " + tmpOrganizm.OrganizmToSring());
                    odswiezSwiat();
                    dispose();

                }
            });


            JScrollPane sp = new JScrollPane(jList);
            add(sp);

            setVisible(true);
        }
    }
    private void startGame() {
        planszaGraphics = new GrafikaPlanszy(swiat);
        mainPanel.add(planszaGraphics);
        komentatorGraphics = new KomentarzeLayout();
        mainPanel.add(komentatorGraphics);
        oznaczenia = new Oznaczenia();
        mainPanel.add(oznaczenia);
        odswiezSwiat();
    }

    public void odswiezSwiat() {
        planszaGraphics.odswiezPlansze();
        komentatorGraphics.odswiezKomentarze();
        SwingUtilities.updateComponentTreeUI(jFrame);
        jFrame.requestFocusInWindow();
    }

    public Swiat getSwiat() {
        return swiat;
    }
    public GrafikaPlanszy getPlanszaGraphics() {
        return planszaGraphics;
    }
    public KomentarzeLayout getKomentatorGraphics() {
        return komentatorGraphics;
    }
}