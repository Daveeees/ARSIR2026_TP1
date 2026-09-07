package exo4;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServeurTCP {

    public static void main(String[] args) {

        try {
            // 1 - Création du serveur sur le port 6666
            ServerSocket socketServeur = new ServerSocket(6666);

            System.out.println("Serveur démarré sur le port 6666...");

            while (true) {

                // 2 - Attente et acceptation d'un client
                Socket socketClient = socketServeur.accept();

                System.out.println(
                        "Connexion avec : " +
                                socketClient.getInetAddress()
                );

                // 3 - Flux d'entrée : recevoir du texte
                BufferedReader entree = new BufferedReader(
                        new InputStreamReader(socketClient.getInputStream())
                );

                // 4 - Flux de sortie : envoyer du texte
                PrintWriter sortie = new PrintWriter(
                        socketClient.getOutputStream(),
                        true
                );

                String message;

                // 5 - Traitement des lignes envoyées par le client
                while ((message = entree.readLine()) != null) {

                    // Si le client demande à quitter
                    if (message.equalsIgnoreCase("exit")) {
                        System.out.println("Fin de session pour ce client.");
                        break;
                    }

                    System.out.println("Reçu : " + message);

                    // Transformation en majuscules
                    String reponse = message.toUpperCase();

                    // Envoi de la réponse au client
                    sortie.println(reponse);
                }

                // 6 - Fermeture de la connexion avec ce client
                entree.close();
                sortie.close();
                socketClient.close();

                System.out.println("Client déconnecté.");
            }

        } catch (Exception e) {
            System.err.println(e);
        }
    }
}