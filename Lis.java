package pl.edu.pg.eti.ksg.po.project2.zwierzeta;

import pl.edu.pg.eti.ksg.po.project2.Zwierze;
import pl.edu.pg.eti.ksg.po.project2.Organizm;
import pl.edu.pg.eti.ksg.po.project2.Swiat;
import pl.edu.pg.eti.ksg.po.project2.Punkt;

import java.util.Random;
import java.awt.*;

public class Lis extends Zwierze {
    private static final int ZASIEG_RUCHU_LISA = 1;
    private static final int SZANSA_WYKONYWANIA_RUCHU_LISA = 1;
    private static final int SILA_LISA = 3;
    private static final int INICJATYWA_LISA = 7;

    public Lis(Swiat swiat, Punkt pozycja, int turaUrodzenia) {
        super(TypOrganizmu.LIS, swiat, pozycja, turaUrodzenia, SILA_LISA, INICJATYWA_LISA);
        this.setZasiegRuchu(ZASIEG_RUCHU_LISA);
        this.setSzansaWykonywaniaRuchu(SZANSA_WYKONYWANIA_RUCHU_LISA);
        setKolor(new Color(235, 109, 19));
    }

    @Override
    public String TypOrganizmuToString() {
        return "Lis";
    }

    @Override
    public Punkt LosujDowolnePole(Punkt pozycja) {
        OdblokujWszystkieKierunki();
        int pozX = pozycja.getX();
        int pozY = pozycja.getY();
        int sizeX = getSwiat().getSizeX();
        int sizeY = getSwiat().getSizeY();
        int ileKierunkowMozliwych = 0;
        Organizm tmpOrganizm;

        if (pozX == 0 || (tmpOrganizm = getSwiat().getPlansza()[pozY][pozX - 1]) != null && tmpOrganizm.getSila() > this.getSila()) {
            ZablokujKierunek(Kierunek.LEWO);
        } else {
            ileKierunkowMozliwych++;
        }

        if (pozX == sizeX - 1 || (tmpOrganizm = getSwiat().getPlansza()[pozY][pozX + 1]) != null && tmpOrganizm.getSila() > this.getSila()) {
            ZablokujKierunek(Kierunek.PRAWO);
        } else {
            ileKierunkowMozliwych++;
        }

        if (pozY == 0 || (tmpOrganizm = getSwiat().getPlansza()[pozY - 1][pozX]) != null && tmpOrganizm.getSila() > this.getSila()) {
            ZablokujKierunek(Kierunek.GORA);
        } else {
            ileKierunkowMozliwych++;
        }

        if (pozY == sizeY - 1 || (tmpOrganizm = getSwiat().getPlansza()[pozY + 1][pozX]) != null && tmpOrganizm.getSila() > this.getSila()) {
            ZablokujKierunek(Kierunek.DOL);
        } else {
            ileKierunkowMozliwych++;
        }

        if (ileKierunkowMozliwych == 0) {
            return new Punkt(pozX, pozY);
        }

        Random rand = new Random();
        while (true) {
            int r = rand.nextInt(ileKierunkowMozliwych);

            for (Kierunek k : Kierunek.values()) {
                if (!CzyKierunekZablokowany(k)) {
                    if (r == 0) {
                        switch (k) {
                            case LEWO:
                                return new Punkt(pozX - 1, pozY);
                            case PRAWO:
                                return new Punkt(pozX + 1, pozY);
                            case GORA:
                                return new Punkt(pozX, pozY - 1);
                            case DOL:
                                return new Punkt(pozX, pozY + 1);
                        }
                    }
                    r--;
                }
            }
        }
    }
}
