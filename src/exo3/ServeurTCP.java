package exo3;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServeurTCP {
    public static void main(String[] args) {
        try {
            // 1 - Création du serveur TCP sur le port 6666
            ServerSocket socketServeur = new ServerSocket(6666);

            System.out.println("Serveur TCP démarré sur le port 6666...");

            while (true) {

                // 2 - Attendre la connexion d'un client
                Socket socketClient = socketServeur.accept();

                System.out.println("Client connecté : "
                        + socketClient.getInetAddress().getHostAddress());

                // 3 - Flux permettant d'envoyer du texte
                PrintWriter sortie =
                        new PrintWriter(socketClient.getOutputStream(), true);

                // 4 - Envoyer l'heure courante
                NowDate maintenant = new NowDate();

                sortie.println(maintenant.TimeToString());

                // 5 - Fermer la connexion avec ce client
                socketClient.close();

                System.out.println("Connexion client fermée.");
            }

        } catch (Exception e) {
            System.err.println(e);
        }
    }
}