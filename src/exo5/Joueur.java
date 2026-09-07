package exo5;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Joueur extends Thread {

    private Socket socket;
    private Jeu jeu;

    private BufferedReader entree;
    private PrintWriter sortie;

    private char symbole;

    private Joueur adversaire;

    private boolean monTour = false;

    public Joueur(Socket socket, Jeu jeu, char symbole) {

        this.socket = socket;
        this.jeu = jeu;
        this.symbole = symbole;

        try {
            entree = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );

            sortie = new PrintWriter(
                    socket.getOutputStream(),
                    true
            );

        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void setAdversaire(Joueur adversaire) {
        this.adversaire = adversaire;
    }

    public void setMonTour(boolean monTour) {
        this.monTour = monTour;
    }

    public void envoyer(String message) {
        sortie.println(message);
    }

    @Override
    public void run() {

        try {

            // Informer le client de son symbole
            envoyer("SYMBOL " + symbole);

            if (monTour) {
                envoyer("TURN");
            } else {
                envoyer("WAIT");
            }

            while (!jeu.estTermine()) {

                /*
                 * Si ce n'est pas son tour,
                 * le thread attend.
                 */
                synchronized (jeu) {

                    while (!monTour && !jeu.estTermine()) {
                        jeu.wait();
                    }
                }

                if (jeu.estTermine()) {
                    break;
                }

                // Lire un message venant du client
                String message = entree.readLine();

                if (message == null) {
                    break;
                }

                System.out.println(
                        "Joueur " + symbole + " : " + message
                );

                /*
                 * Format attendu :
                 * PLAY ligne colonne
                 */
                if (message.startsWith("PLAY")) {

                    String[] parties = message.split(" ");

                    if (parties.length != 3) {
                        envoyer("INVALID");
                        envoyer("TURN");
                        continue;
                    }

                    try {

                        int ligne = Integer.parseInt(parties[1]);
                        int colonne = Integer.parseInt(parties[2]);

                        synchronized (jeu) {

                            if (!jeu.jouer(ligne, colonne, symbole)) {

                                envoyer("INVALID");
                                envoyer("TURN");
                                continue;
                            }

                            // Coup accepté
                            envoyer(
                                    "VALID " +
                                            ligne + " " +
                                            colonne + " " +
                                            symbole
                            );

                            adversaire.envoyer(
                                    "VALID " +
                                            ligne + " " +
                                            colonne + " " +
                                            symbole
                            );

                            System.out.println(jeu.afficherGrille());

                            // Vérifier la fin du jeu
                            if (jeu.estTermine()) {

                                if (jeu.getGagnant() == symbole) {

                                    envoyer("WIN");
                                    adversaire.envoyer("LOSE");

                                } else {

                                    envoyer("DRAW");
                                    adversaire.envoyer("DRAW");
                                }

                                jeu.notifyAll();
                                break;
                            }

                            // Donner le tour à l'autre joueur
                            monTour = false;
                            adversaire.setMonTour(true);

                            envoyer("WAIT");
                            adversaire.envoyer("TURN");

                            jeu.notifyAll();
                        }

                    } catch (NumberFormatException e) {

                        envoyer("INVALID");
                        envoyer("TURN");
                    }

                } else {

                    envoyer("INVALID");
                    envoyer("TURN");
                }
            }

            socket.close();

        } catch (Exception e) {
            System.out.println(
                    "Joueur " + symbole + " déconnecté."
            );
        }
    }
}