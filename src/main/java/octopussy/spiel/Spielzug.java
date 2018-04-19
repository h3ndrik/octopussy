package octopussy.spiel;

import org.json.simple.*;
import org.json.simple.parser.*;

public class Spielzug {
  String aktion;
  Karte karte;
  Spieler spieler;

  public void reklamiere(String regel) {
    throw new UnsupportedOperationException("Spielzug reklamieren noch nicht implementiert");
  }

  public static Spielzug parseFromMessage(Object message) {
    Spielzug spielzug = new Spielzug();
      try {
//        JSONObject obj = (JSONObject)JSONValue.parseWithException(message);
        JSONObject obj = (JSONObject) message;
        spielzug.aktion = (String) obj.get("aktion");
        String spielerName = (String) obj.get("spieler");
        spielzug.spieler = Spieler.getByName(spielerName);
        JSONObject karteObj = (JSONObject) obj.get("karte");
        if (karteObj != null) {
          spielzug.karte = new Karte((String)karteObj.get("farbe"),(String)karteObj.get("symbol"));
        }

      } catch (Exception e) {
        System.out.println("FEHLER!!!!!!!!!!!!!!!!!!!!!");
        e.printStackTrace();
        spielzug.reklamiere("JSON Parsing Error!");
      }
    return spielzug;
  }

}
