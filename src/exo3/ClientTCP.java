package exo3;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ClientTCP {
 public static void main(String[] args){// Méthode principale
         try {
             // 1 - Création du canal
             Socket socket = new Socket();
             InetSocketAddress adresseServeur = new InetSocketAddress("localhost", 6666);

             // 2 - Connexion avec un délai d'attente de 5 secondes
             socket.connect(adresseServeur, 5000);
             ObjectOutputStream fluxSortie = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream fluxEntree = new ObjectInputStream(socket.getInputStream());

             // 3 - Émettre et recevoir
             String message = "Salve !";
             fluxSortie.writeObject(message);
             String reponse = (String) fluxEntree.readObject();
             System.out.println("Message du serveur: " + reponse);

             // 4 - Libérer le canal
             fluxEntree.close();
             fluxSortie.close();
             socket.close();
             } catch (Exception e) {
             System.err.println(e);
             }
         }
 }
