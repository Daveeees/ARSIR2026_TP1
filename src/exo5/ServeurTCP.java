package exo5;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class ServeurTCP {

    public static void main(String[] args) {

        try {
            // Création du serveur TCP
            ServerSocket socketServeur = new ServerSocket(6666);

            System.out.println(
                    "Serveur TCP Tic-Tac-Toe démarré sur le port 6666..."
            );

            /*
             * Le serveur reste actif.
             * Après une partie, il attend deux nouveaux joueurs.
             */
            while (true) {

                System.out.println();
                System.out.println("=== Nouvelle partie ===");

                // Nouvelle instance de Jeu pour chaque partie
                Jeu jeu = new Jeu();

                // Attente du joueur 1
                System.out.println("Attente du joueur 1...");

                Socket socketJoueur1 = socketServeur.accept();

                System.out.println(
                        "Joueur 1 connecté : "
                                + socketJoueur1.getInetAddress()
                );

                // Attente du joueur 2
                System.out.println("Attente du joueur 2...");

                Socket socketJoueur2 = socketServeur.accept();

                System.out.println(
                        "Joueur 2 connecté : "
                                + socketJoueur2.getInetAddress()
                );

                // Création des deux joueurs
                Joueur joueur1 = new Joueur(
                        socketJoueur1,
                        jeu,
                        'X'
                );

                Joueur joueur2 = new Joueur(
                        socketJoueur2,
                        jeu,
                        'O'
                );

                // Chaque joueur connaît son adversaire
                joueur1.setAdversaire(joueur2);
                joueur2.setAdversaire(joueur1);

                // Choix aléatoire du joueur qui commence
                Random random = new Random();

                if (random.nextBoolean()) {

                    joueur1.setMonTour(true);
                    joueur2.setMonTour(false);

                    System.out.println("Le joueur X commence.");

                } else {

                    joueur1.setMonTour(false);
                    joueur2.setMonTour(true);

                    System.out.println("Le joueur O commence.");
                }

                // Démarrage des deux threads
                joueur1.start();
                joueur2.start();

                /*
                 * Le serveur attend la fin de la partie
                 * avant d'accepter deux nouveaux joueurs.
                 */
                joueur1.join();
                joueur2.join();

                System.out.println("Partie terminée.");

            }

        } catch (Exception e) {
            System.err.println(e);
        }
    }
}