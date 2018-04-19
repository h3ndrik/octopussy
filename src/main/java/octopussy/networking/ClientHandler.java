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

  boolean disconnect = false;

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
          Message message = parseMessageObject(obj);

          switch (message.type) {
            case Message.MessageType.SPIELZUG:
              broker.publishMessage(message);
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
          System.out.println("Invalid Type: " + clientMessage);
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

  private Message parseMessageObject(JSONObject obj) {
    Message message = new Message(
        toIntExact((Long) obj.get(Message.jsonKey.CHANNEL)),
        (String) obj.get(Message.jsonKey.TYPE),
        (String) obj.get(Message.jsonKey.MESSAGE)
    );
    return message;
  }

}
