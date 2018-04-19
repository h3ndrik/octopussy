package octopussy.networking;

import org.json.simple.*;
import org.json.simple.parser.*;

import static java.lang.Math.toIntExact;

public class Message {
  int channel;
  String type;
  String message;
  String player;

  public Message(int channel, String type, String message) {
    this.channel = channel;
    this.type = type;
    this.message = message;
  }

  public Message(int channel, String type, String message, String player) {
    this(channel, type, message);
    this.player = player;
  }

  public JSONObject toJSON() {
    JSONObject obj = new JSONObject();
    obj.put(jsonKey.CHANNEL, channel);
    obj.put(jsonKey.TYPE, type);
    obj.put(jsonKey.MESSAGE, message);
    obj.put(jsonKey.PLAYER, player);
    return obj;
  }

  public static Message parseMessageObject(JSONObject obj) {
    Message message = new Message(
        toIntExact((Long) obj.get(Message.jsonKey.CHANNEL)),
        (String) obj.get(Message.jsonKey.TYPE),
        (String) obj.get(Message.jsonKey.MESSAGE),
        (String) obj.get(Message.jsonKey.PLAYER)
    );
    return message;
  }

  public static class jsonKey {
    public static final String CHANNEL = "channel";
    public static final String TYPE = "type";
    public static final String MESSAGE = "message";
    public static final String PLAYER = "player";
  }

  public static class MessageType {
    public static final String ERROR      = "ERROR";
    public static final String CONNECT    = "CONNECT";  // Login
    public static final String CONNACK    = "CONNACK";
    public static final String SUBSCRIBE  = "SUBSCRIBE";
    public static final String SUBACK     = "SUBACK";
    public static final String DISCONNECT = "DISCONNECT";
    public static final String PINGREQ    = "PINGREQ";
    public static final String PINGRESP   = "PINGRESP";
    public static final String SPIELZUG   = "SPIELZUG";
  }
}
