package octopussy.server;

import java.io.IOException;
import java.util.*;

import octopussy.networking.*;

public class OctopussyServer {
  HashMap games = new HashMap();

  public static void main (String[] args) {
    OctopussySocket socket = new OctopussySocket();
    try {
      socket.listen();
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(-1);
    }
  }
/*
  public void newGame() {
    games.put(game, game);
  }

  public void joinGame() {
    games.get(game).join(client);
  }
*/
}
