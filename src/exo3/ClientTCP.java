package exo3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientTCP {
    public static void main(String[] args) {

        try {
            // 1 - Connexion au serveur
            Socket socketClient = new Socket("localhost", 6666);

            System.out.println("Connecté au serveur.");

            // 2 - Flux permettant de recevoir du texte
            BufferedReader entree =
                    new BufferedReader(
                            new InputStreamReader(socketClient.getInputStream())
                    );

            // 3 - Recevoir l'heure envoyée par le serveur
            String reponse = entree.readLine();

            System.out.println("Heure du serveur : " + reponse);

            // 4 - Fermeture de la socket
            socketClient.close();

        } catch (Exception e) {
            System.err.println(e);
        }
    }
}