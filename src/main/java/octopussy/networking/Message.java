package octopussy.networking;

public class Message {
  int channel;
  int type;
  String message;

  public static class jsonKey {
    public static final String CHANNEL = "channel";
    public static final String TYPE = "type";
    public static final String MESSAGE = "message";
  }

  public static class MessageType {
    public static final int ERROR      = -1;
    public static final int CONNECT    =  1;
    public static final int CONNACK    =  2;
    public static final int SUBSCRIBE  =  3;
    public static final int SUBACK     =  4;
    public static final int DISCONNECT =  5;
    public static final int PINGREQ    =  6;
    public static final int PINGRESP   =  7;
    public static final int SPIELZUG   =  8;
  }
}
