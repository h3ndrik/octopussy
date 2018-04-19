package octopussy.networking;

import java.net.*;
import java.io.*;
import java.util.*;

public class MessageBroker {
  private HashMap clients = new HashMap();
  private HashMap<Integer, ArrayList<MessageReceiver>> channels = new HashMap<Integer, ArrayList<MessageReceiver>>();
  private ArrayList<MessageReceiver> servers = new ArrayList<MessageReceiver>();

  public void registerClient(ClientHandler client) {
    clients.put(client, client);
  }

  public void subscribeChannel(int channelId, MessageReceiver client) {
    ArrayList<MessageReceiver> channel = channels.get(channelId);
    if (channel == null) {
      channel = new ArrayList<MessageReceiver>();
      channels.put(channelId, channel);
    }
    channel.add(client);
  }

  public void subscribeAllChannels(MessageReceiver client) {
    servers.add(client);
  }

  public void publishMessage(Message msg) {
    transmitMessage(msg);
  }

  public void transmitMessage(Message msg) {
    try {
      if (msg.channel > 0) {
        for( MessageReceiver client : channels.get(msg.channel) ) {
          System.out.println("found client");
          client.messageCallback(msg);
        }
        for( MessageReceiver server : servers ) {
          System.out.println("found server");
          server.messageCallback(msg);
        }
      }
    } catch (NullPointerException e) {
      System.out.println("channel not found");
    }
  }

}
