package octopussy.spiel;

import java.io.*;
import java.util.*;

import octopussy.networking.*;

public class Runde implements Runnable, MessageReceiver {
  int id;
  ArrayList<Spiel> spiele;

  public Runde(int id) {
    this.id = id;
    spiele = new ArrayList<Spiel>();
  }

  @Override
  public void run() {
    try{
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      System.out.println("Runde interrupted");
    }
  }

  public void messageCallback(Message msg) {
    System.out.println("RUNDE Message!!");
  }
}
