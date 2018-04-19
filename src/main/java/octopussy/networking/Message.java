package octopussy.networking;

import org.json.simple.*;
import org.json.simple.parser.*;

public class Message {
  int channel;
  String type;
  String message;

  public Message(int channel, String type, String message) {
    this.channel = channel;
    this.type = type;
    this.message = message;
  }

  public JSONObject toJSON() {
    JSONObject obj = new JSONObject();
    obj.put(jsonKey.CHANNEL, channel);
    obj.put(jsonKey.TYPE, type);
    obj.put(jsonKey.MESSAGE, message);
    return obj;
  }

  public static class jsonKey {
    public static final String CHANNEL = "channel";
    public static final String TYPE = "type";
    public static final String MESSAGE = "message";
  }

  public static class MessageType {
    public static final String ERROR      = "ERROR";
//    public static final String CONNECT    = "CONNECT";
//    public static final String CONNACK    = "CONNACK";
    public static final String SUBSCRIBE  = "SUBSCRIBE";
    public static final String SUBACK     = "SUBACK";
    public static final String DISCONNECT = "DISCONNECT";
    public static final String PINGREQ    = "PINGREQ";
    public static final String PINGRESP   = "PINGRESP";
    public static final String SPIELZUG   = "SPIELZUG";
  }
}
