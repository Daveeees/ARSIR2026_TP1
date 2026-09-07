package exo5;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientTCP {

    private static char[][] grille = new char[3][3];

    public static void main(String[] args) {

        try {
            // Initialisation de la grille
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    grille[i][j] = ' ';
                }
            }

            // 1 - Connexion TCP au serveur
            Socket socket = new Socket("localhost", 6666);

            System.out.println("Connecté au serveur Tic-Tac-Toe.");

            // 2 - Flux permettant de recevoir les messages
            BufferedReader entree = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );

            // 3 - Flux permettant d'envoyer les messages
            PrintWriter sortie = new PrintWriter(
                    socket.getOutputStream(),
                    true
            );

            Scanner clavier = new Scanner(System.in);

            boolean termine = false;

            // 4 - Boucle principale
            while (!termine) {

                String message = entree.readLine();

                if (message == null) {
                    System.out.println("Connexion avec le serveur fermée.");
                    break;
                }

                // Le serveur indique le symbole
                if (message.startsWith("SYMBOL")) {

                    String[] parties = message.split(" ");

                    char symbole = parties[1].charAt(0);

                    System.out.println("Vous jouez avec : " + symbole);
                }

                // C'est le tour du joueur
                else if (message.equals("TURN")) {

                    afficherGrille();

                    System.out.println("C'est votre tour.");

                    System.out.print("Ligne (0 à 2) : ");
                    int ligne = clavier.nextInt();

                    System.out.print("Colonne (0 à 2) : ");
                    int colonne = clavier.nextInt();

                    // Envoi du coup au serveur
                    sortie.println(
                            "PLAY " + ligne + " " + colonne
                    );
                }

                // Attendre l'adversaire
                else if (message.equals("WAIT")) {

                    System.out.println("Tour de l'adversaire...");
                }

                // Un coup valide a été joué
                else if (message.startsWith("VALID")) {

                    String[] parties = message.split(" ");

                    int ligne = Integer.parseInt(parties[1]);
                    int colonne = Integer.parseInt(parties[2]);
                    char symbole = parties[3].charAt(0);

                    grille[ligne][colonne] = symbole;

                    afficherGrille();
                }

                // Coup invalide
                else if (message.equals("INVALID")) {

                    System.out.println("Coup invalide !");
                }

                // Victoire
                else if (message.equals("WIN")) {

                    afficherGrille();

                    System.out.println("Vous avez gagné !");

                    termine = true;
                }

                // Défaite
                else if (message.equals("LOSE")) {

                    afficherGrille();

                    System.out.println("Vous avez perdu.");

                    termine = true;
                }

                // Match nul
                else if (message.equals("DRAW")) {

                    afficherGrille();

                    System.out.println("Match nul.");

                    termine = true;
                }
            }

            // 5 - Fermeture
            clavier.close();
            entree.close();
            sortie.close();
            socket.close();

        } catch (Exception e) {
            System.err.println(e);
        }
    }

    private static void afficherGrille() {

        System.out.println();

        System.out.println(
                " " + grille[0][0]
                        + " | " + grille[0][1]
                        + " | " + grille[0][2]
        );

        System.out.println("---+---+---");

        System.out.println(
                " " + grille[1][0]
                        + " | " + grille[1][1]
                        + " | " + grille[1][2]
        );

        System.out.println("---+---+---");

        System.out.println(
                " " + grille[2][0]
                        + " | " + grille[2][1]
                        + " | " + grille[2][2]
        );

        System.out.println();
    }
}