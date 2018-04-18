package octopussy.test;

import java.net.*;
import java.io.*;

public class TestClient implements Runnable {
  static protected String serverHostname = "localhost";
  static protected int serverPort = 6000;
  Socket server;

  TestClient(Socket s) {
    this.server = s;
  }

  public static void main (String[] args) throws IOException, UnknownHostException {
    Socket socket = new Socket(serverHostname, serverPort);

    Thread listener = new Thread(new TestClient(socket));
    listener.start();

    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
    BufferedReader cmdlineReader = new BufferedReader(new InputStreamReader(System.in));

    out.println("Guten Tag und viel Spass!");

    while(true) {
      String cmdline = cmdlineReader.readLine();
      if (cmdline.equalsIgnoreCase("close")) { break; }
      out.println(cmdline);
    }

    listener.interrupt();

    try {
      Thread.sleep(2000);
    } catch(InterruptedException e) {
      Thread.currentThread().interrupt();
    }
    socket.close();
  }

  @Override
  public void run() {
    try {
      InputStreamReader streamReader = new InputStreamReader(server.getInputStream());
      BufferedReader in = new BufferedReader(streamReader);

      String serverMessage = null;
      while((serverMessage = in.readLine()) != null) {
        if (Thread.currentThread().isInterrupted()) {
          System.out.println("ioaejs. funktioniert nicht");
          break;
        }
        System.out.println("vom Server: " + serverMessage);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
