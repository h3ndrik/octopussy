package octopussy.networking;

import java.net.*;
import java.io.*;

import org.json.simple.*;
import org.json.simple.parser.*;

public class ClientHandler implements Runnable, MessageReceiver {
  Socket socket;
  MessageBroker broker;
  BufferedReader in;
  PrintWriter out;

  boolean disconnect = false;
  boolean logged_in = false;
  String playerName;

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
      while((clientMessage = in.readLine()) != null) { // new message from client
        try {
          JSONObject obj = (JSONObject)JSONValue.parseWithException(clientMessage);
          System.out.println("Client: " + clientMessage);
          Message message = Message.parseMessageObject(obj);
          if (!message.type.equals(Message.MessageType.CONNECT)) {
            message.player = playerName;
          }

          switch (message.type) {
            case Message.MessageType.SPIELZUG:
              broker.publishMessage(message);
              break;
            case Message.MessageType.CONNECT:
              JSONObject connMessage;
              if (!logged_in) {
                logged_in = broker.registerClient(message.player, this);
                if (logged_in) {
                  playerName = message.player;
                  connMessage = new Message(-1, Message.MessageType.CONNACK, "logged in as "+playerName).toJSON();
                  System.out.println("Player connected as: "+message.player);
                } else {
                  connMessage = new Message(-1, Message.MessageType.ERROR, "not possible").toJSON();
                }
              } else {
                connMessage = new Message(-1, Message.MessageType.ERROR, "Already logged in as "+playerName).toJSON();
              }

              out.println(connMessage.toJSONString());
              break;
            case Message.MessageType.SUBSCRIBE:
              broker.subscribeChannel(message.channel, this);
              break;
            case Message.MessageType.PINGREQ:
              JSONObject respMessage = new Message(-1, Message.MessageType.PINGRESP, message.message).toJSON();
              out.println(respMessage.toJSONString());
              break;
            case Message.MessageType.DISCONNECT:
              disconnect = true;
              break;
            default:
              System.out.println("unrecognized message type!");
              throw new UnsupportedOperationException();
          }
        } catch (ParseException e) {
          System.out.println("Invalid JSON: " + clientMessage);
          JSONObject errorMessage = new Message(-1, Message.MessageType.ERROR, "Invalid JSON").toJSON();
          out.println(errorMessage.toJSONString());
        } catch (IllegalArgumentException e) {
          System.out.println("Malformed Message: " + clientMessage);
          JSONObject errorMessage = new Message(-1, Message.MessageType.ERROR, "Malformed Message").toJSON();
          out.println(errorMessage.toJSONString());
        } catch (UnsupportedOperationException e) {
          System.out.println("Unsupported Operation: " + clientMessage);
          e.printStackTrace();
          JSONObject errorMessage = new Message(-1, Message.MessageType.ERROR, "Invalid Type").toJSON();
          out.println(errorMessage.toJSONString());
        }

        if (disconnect) { break; }
      }

      System.out.println("Beende Verbindung zu " + socket.getInetAddress().getHostName());
      socket.close();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /* Sends message to the client */
  public void messageCallback(Message msg) {
    out.println(msg.toJSON().toJSONString());
  }

  public void close() {
    this.disconnect = true;
  }

}
