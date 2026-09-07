package exo3;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServeurTCP {
 public static void main(String[] args){// Méthode principale
         try{
             // 1 - Création du canal
             ServerSocket socketServeur = new ServerSocket(6666);

             while(true){
                 // 2 - Mise en attente
                 Socket socketClient = socketServeur.accept();

                 // 3 - Accepter la connexion
                 System.out.println("Connexion avec : " + socketClient.getInetAddress());

                 // 4- Emettre et recevoir
                 ObjectInputStream fluxEntree = new ObjectInputStream(socketClient.getInputStream());
                 ObjectOutputStream fluxSortie = new ObjectOutputStream(socketClient.getOutputStream());
                 String message = (String) fluxEntree.readObject();
                 System.out.println("Message reçu: " + message);
                 fluxSortie.writeObject("Accusé de réception");

                 }
             }catch(Exception e){
             System.err.println(e);
             }
         }
 }