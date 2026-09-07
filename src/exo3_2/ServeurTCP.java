package exo3_2;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServeurTCP {
    public static void main(String[] args){// Méthode principale
        try{
            // Création du canal
            ServerSocket socketServeur = new ServerSocket(6666);

            while(true){
                // Mise en attente
                Socket socketClient = socketServeur.accept();

                // Accepter la connexion
                System.out.println("Connexion avec : " + socketClient.getInetAddress());

                // Emettre et recevoir
                ObjectInputStream fluxEntree = new ObjectInputStream(socketClient.getInputStream());
                ObjectOutputStream fluxSortie = new ObjectOutputStream(socketClient.getOutputStream());
                String message = (String) fluxEntree.readObject();
                System.out.println("Message reçu: " + message);

                while(!message.equals("CLOSE")){
                    NowDate date = new NowDate();
                    if(message.equals("DATE")){
                        fluxSortie.writeObject("Date actuelle : " + date.DateToString() + "\n");
                    }
                    if(message.equals("HOUR")){
                        fluxSortie.writeObject("Heure actuelle : " + date.TimeToString() + "\n");
                    }
                    if (message.equals("FULL")){
                        fluxSortie.writeObject("Date actuelle : " + date.DateToString() + "\n" + "Heure actuelle : " + date.TimeToString() + "\n");
                    }

                    message = (String) fluxEntree.readObject();
                }

                // Envoi de l'heure courante au client
                fluxSortie.writeObject("Socket du client fermée ");

                // Fermer la socket du client
                socketClient.close();
            }
        }catch(Exception e){
            System.err.println(e);
        }
    }
}
