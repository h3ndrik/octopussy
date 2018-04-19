package octopussy.networking;

import java.net.*;
import java.io.*;
import java.util.*;

import octopussy.spiel.Runde;

public class MessageBroker {
  private HashMap<String, ClientHandler> clients = new HashMap<String, ClientHandler>();
  private HashMap<Integer, ArrayList<MessageReceiver>> channels = new HashMap<Integer, ArrayList<MessageReceiver>>();
  private ArrayList<MessageReceiver> servers = new ArrayList<MessageReceiver>();

  public boolean registerClient(String player, ClientHandler client) {
    ClientHandler entry = clients.get(player);
    if (entry == null) {
      clients.put(player, client);
      return true;
    }
    return false;
  }

  public void subscribeChannel(int channelId, MessageReceiver client) {
    ArrayList<MessageReceiver> channel = channels.get(channelId);
    if (channel == null) {
      channel = new ArrayList<MessageReceiver>();
      channels.put(channelId, channel);
      // Create Game Server
      Runde runde = new Runde(channelId);
      new Thread(runde).start();
      channel.add(runde);
    }
    channel.add(client);
    client.messageCallback(new Message(channelId, Message.MessageType.SUBACK, "subscribed"));
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
