package pl.edu.pg.eti.ksg.po.project2.zwierzeta;

import pl.edu.pg.eti.ksg.po.project2.Zwierze;
import pl.edu.pg.eti.ksg.po.project2.Komentator;
import pl.edu.pg.eti.ksg.po.project2.Swiat;
import pl.edu.pg.eti.ksg.po.project2.Punkt;
import pl.edu.pg.eti.ksg.po.project2.Umiejetnosc;

import java.awt.*;

public class Czlowiek extends Zwierze {
    private static final int ZASIEG_RUCHU_CZLOWIEKA = 1;
    private static final int SZANSA_WYKONYWANIA_RUCHU_CZLOWIEKA = 1;
    private static final int SILA_CZLOWIEKA = 5;
    private static final int INICJATYWA_CZLOWIEKA = 4;
    private Kierunek kierunekRuchu;
    private Umiejetnosc umiejetnosc;

    public Czlowiek(Swiat swiat, Punkt pozycja, int turaUrodzenia) {
        super(TypOrganizmu.CZLOWIEK, swiat, pozycja, turaUrodzenia, SILA_CZLOWIEKA, INICJATYWA_CZLOWIEKA);
        this.setZasiegRuchu(ZASIEG_RUCHU_CZLOWIEKA);
        this.setSzansaWykonywaniaRuchu(SZANSA_WYKONYWANIA_RUCHU_CZLOWIEKA);
        kierunekRuchu = Kierunek.BRAK_KIERUNKU;
        setKolor(Color.BLUE);
        umiejetnosc = new Umiejetnosc();
    }

    private void specUmiej() {
        setSila(getSila() -1);
    }

    @Override
    protected Punkt ZaplanujRuch() {
        int x = getPozycja().getX();
        int y = getPozycja().getY();
        LosujDowolnePole(getPozycja());//BLOKUJE KIERUNKI NIEDOZWOLONE PRZY GRANICY SWIATA
        if (kierunekRuchu == Kierunek.BRAK_KIERUNKU || CzyKierunekZablokowany(kierunekRuchu)) return getPozycja();
        else {
            switch (kierunekRuchu) {
                case GORA:
                    return new Punkt(x, y - 1);
                case DOL:
                    return new Punkt(x, y + 1);
                case LEWO:
                    return new Punkt(x - 1, y);
                case PRAWO:
                    return new Punkt(x + 1, y);
                default:
                    return new Punkt(x, y);
            }
        }
    }

    @Override
    public void Akcja() {
        if (umiejetnosc.getCzyJestAktywna()) {
            Komentator.DodajKomentarz(OrganizmToSring() + " Zwiekszenie sily jest aktywne (Pozostaly czas " + umiejetnosc.getCzasTrwania() + " tur)");
        }
        for (int i = 0; i < getZasiegRuchu(); i++) {
            Punkt przyszlaPozycja = ZaplanujRuch();
            if (getSwiat().CzyPoleJestZajete(przyszlaPozycja) && getSwiat().CoZnajdujeSieNaPolu(przyszlaPozycja) != this) {
                Kolizja(getSwiat().CoZnajdujeSieNaPolu(przyszlaPozycja));
                break;
            } else if (getSwiat().CoZnajdujeSieNaPolu(przyszlaPozycja) != this) WykonajRuch(przyszlaPozycja);
            if (umiejetnosc.getCzyJestAktywna()) specUmiej();
        }
        kierunekRuchu = Kierunek.BRAK_KIERUNKU;
        umiejetnosc.SprawdzWarunki();
    }

    @Override
    public String TypOrganizmuToString() {
        return "Czlowiek";
    }

    public Umiejetnosc getUmiejetnosc() {
        return umiejetnosc;
    }

    public void setKierunekRuchu(Kierunek kierunekRuchu) {
        this.kierunekRuchu = kierunekRuchu;
    }
}
