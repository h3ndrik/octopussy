package octopussy.networking;

import java.net.*;
import java.io.*;
import java.util.*;

public class OctopussySocket {
  static protected int serverPort = 6000;
  HashMap clients = new HashMap();
  MessageBroker broker = new MessageBroker();

  public void listen() throws IOException {
    ServerSocket server = new ServerSocket(serverPort);
    System.out.println("Server gestartet");

    while (true) {
      Socket client = server.accept();
      ClientHandler handler = new ClientHandler(client, broker);
//      broker.registerClient(handler);
      new Thread(handler).start();
    }
  }

}
