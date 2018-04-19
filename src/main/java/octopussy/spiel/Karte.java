package octopussy.spiel;

public class Karte {
  Farbe farbe;
  Symbol symbol;

  public Karte(String farbe, String symbol) {
    switch (farbe) {
      case Farbe.KARO:
        farbe = Farbe.KARO;
        break;
      case Farbe.HERZ:
        farbe = Farbe.HERZ;
        break;
      case Farbe.PIK:
        farbe = Farbe.PIK;
        break;
      case Farbe.KREUZ:
        farbe = Farbe.KREUZ;
        break;
      default:
        throw new IllegalArgumentException("ungültige Kartenfarbe");
    }
    switch (symbol) {
      case Symbol.ASS:
        farbe = Symbol.ASS;
        break;
      case Symbol.ZEHN:
        farbe = Symbol.ZEHN;
        break;
      case Symbol.KÖNIG:
        farbe = Symbol.KÖNIG;
        break;
      case Symbol.DAME:
        farbe = Symbol.DAME;
        break;
      case Symbol.BUBE:
        farbe = Symbol.BUBE;
        break;
      case Symbol.NEUN:
        farbe = Symbol.NEUN;
        break;
      default:
        throw new IllegalArgumentException("ungültiges Kartensymbol");
    }
  }

  public int getAugen() {
    throw new UnsupportedOperationException("noch nicht implementiert");
  }

  public class Farbe {
    public static final String KARO  = "Karo";
    public static final String HERZ  = "Herz";
    public static final String PIK   = "Pik";
    public static final String KREUZ = "Kreuz";

    public static final String TRUMPF = "Trumpf";
  }

  public static class Symbol {
    public static final String ASS   = "Ass";
    public static final String ZEHN  = "Zehn";
    public static final String KÖNIG = "König";
    public static final String DAME  = "Dame";
    public static final String BUBE  = "Bube";
    public static final String NEUN  = "Neun";
  }

}
