package octopussy.server;

import java.io.IOException;

import octopussy.networking.*;

public class OctopussyServer {

  public static void main (String[] args) {
    OctopussySocket socket = new OctopussySocket();
    try {
      socket.listen();
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(-1);
    }
  }

}
