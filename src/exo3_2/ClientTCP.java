package exo3_2;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class ClientTCP {
    public static void main(String[] args){// Méthode principale
        try {
            System.out.println("Vous pouvez avoir l'heure en tapant: HOUR");
            System.out.println("Vous pouvez avoir la date en tapant: DATE");
            System.out.println("Vous pouvez avoir la date et l'heure en tapant: FULL");

            Scanner scan = new Scanner(System.in);

            // 1 - Création du canal
            Socket socket = new Socket();
            InetSocketAddress adresseServeur = new InetSocketAddress("localhost", 6666);

            // 2 - Connexion avec un délai d'attente de 5 secondes
            socket.connect(adresseServeur, 5000);
            ObjectOutputStream fluxSortie = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream fluxEntree = new ObjectInputStream(socket.getInputStream());


            while(true){
                System.out.println("Entrez votre message: ");
                String ligne = scan.nextLine();
                if (ligne.equals("DATE") || ligne.equals("HOUR") || ligne.equals("FULL")) {
                    // Émettre et recevoir
                    fluxSortie.writeObject(ligne);
                    String reponse = (String) fluxEntree.readObject();
                    System.out.println("Message du serveur: \n" + reponse);
                }
                else if (ligne.equals("CLOSE")) {
                    // Émettre et recevoir
                    fluxSortie.writeObject(ligne);
                    String reponse = (String) fluxEntree.readObject();
                    System.out.println("Message du serveur: \n" + reponse);

                    // 4 - Libérer le canal
                    fluxEntree.close();
                    fluxSortie.close();
                    socket.close();
                    break;
                }
                else{
                    System.out.println("MESAGE INVALIDE VEUILLEZ RECOMMENCER \n");
                }
            }

        } catch (Exception e) {
            System.err.println(e);
        }
    }
}

