package exo4;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientTCP {

    public static void main(String[] args) {

        try {
            // 1 - Connexion au serveur
            Socket socket = new Socket("localhost", 6666);

            System.out.println(
                    "Connecté au serveur. Tapez du texte ou 'exit' pour quitter."
            );

            // 2 - Flux de sortie vers le serveur
            PrintWriter sortie = new PrintWriter(
                    socket.getOutputStream(),
                    true
            );

            // 3 - Flux d'entrée venant du serveur
            BufferedReader entree = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );

            Scanner clavier = new Scanner(System.in);

            // 4 - Lire les lignes saisies au clavier
            while (clavier.hasNextLine()) {

                String ligne = clavier.nextLine();

                // Envoi de la ligne au serveur
                sortie.println(ligne);

                // Si l'utilisateur tape exit, on quitte
                if (ligne.equalsIgnoreCase("exit")) {
                    break;
                }

                // Réception de la réponse du serveur
                String reponse = entree.readLine();

                // Affichage de la réponse
                System.out.println("Serveur : " + reponse);
            }

            // 5 - Fermeture
            clavier.close();
            entree.close();
            sortie.close();
            socket.close();

            System.out.println("Connexion fermée.");

        } catch (Exception e) {
            System.err.println(e);
        }
    }
}