package octopussy.networking;

import java.net.*;
import java.io.*;

import org.json.simple.*;
import org.json.simple.parser.*;

import static java.lang.Math.toIntExact;

public class ClientHandler implements Runnable, MessageReceiver {
  Socket socket;
  MessageBroker broker;
  BufferedReader in;
  PrintWriter out;


  ClientHandler(Socket client, MessageBroker broker) {
    this.socket = client;
    this.broker = broker;
  }

  @Override
  public void run() {
    try {
      System.out.println("Neue Verbindung von " + socket.getInetAddress().getHostName());
      InputStreamReader streamReader = new InputStreamReader(socket.getInputStream());
      in = new BufferedReader(streamReader);
      out = new PrintWriter(socket.getOutputStream(),true);

      String clientMessage = null;
      while((clientMessage = in.readLine()) != null) {
        try {
          JSONObject obj = (JSONObject)JSONValue.parseWithException(clientMessage);
          System.out.println("Client: " + clientMessage);
          Message message = parseMessageObject(obj);

          switch (message.type) {
            case Message.MessageType.SPIELZUG:
              broker.publishMessage(message);
              break;
            case Message.MessageType.SUBSCRIBE:
              broker.subscribeChannel(message.channel, this);
              break;
            default:
              System.out.println("unrecognized message type!");
              throw new IllegalArgumentException();
          }
        } catch (ParseException e) {
          System.out.println("Invalid JSON: " + clientMessage);
        } catch (IllegalArgumentException e) {
          System.out.println("Invalid Type: " + clientMessage);
        }
        out.println("Ebenfalls " + clientMessage);

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

  public void messageCallback(Message msg) {
    out.println("MESSAGE!!");
  }

  private Message parseMessageObject(JSONObject obj) {
    Message message = new Message();
    message.channel = toIntExact((Long) obj.get(Message.jsonKey.CHANNEL));;
    message.type = (String) obj.get(Message.jsonKey.TYPE);
    message.message = (String) obj.get(Message.jsonKey.MESSAGE);
    return message;
  }

}
