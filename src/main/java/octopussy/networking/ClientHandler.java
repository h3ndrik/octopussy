package octopussy.networking;

import java.net.*;
import java.io.*;

import org.json.simple.JSONObject;

public class ClientHandler implements Runnable {
  Socket socket;

  ClientHandler(Socket client) {
    this.socket = client;
  }

  @Override
  public void run() {
    try {
      System.out.println("Neue Verbindung von " + socket.getInetAddress().getHostName());
      InputStreamReader streamReader = new InputStreamReader(socket.getInputStream());
      BufferedReader in = new BufferedReader(streamReader);
      PrintWriter out = new PrintWriter(socket.getOutputStream(),true);

      String clientMessage = null;
      while((clientMessage = in.readLine()) != null) {
        out.println("Ebenfalls " + clientMessage);
        System.out.println("Client: " + clientMessage);
        if (clientMessage.equalsIgnoreCase("quit")) {
          break;
        }
      }

      System.out.println("Beende Verbindung zu " + socket.getInetAddress().getHostName());
      socket.close();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
