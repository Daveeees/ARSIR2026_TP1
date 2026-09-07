package exo5;

public class Jeu {

    private char[][] grille;
    private char joueurCourant;
    private boolean termine;
    private char gagnant;

    public Jeu() {
        grille = new char[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                grille[i][j] = ' ';
            }
        }

        termine = false;
        gagnant = ' ';
    }

    // Tester si un coup est valide
    public synchronized boolean coupValide(int ligne, int colonne) {

        if (termine) {
            return false;
        }

        if (ligne < 0 || ligne > 2 || colonne < 0 || colonne > 2) {
            return false;
        }

        return grille[ligne][colonne] == ' ';
    }

    // Jouer un coup
    public synchronized boolean jouer(int ligne, int colonne, char symbole) {

        if (!coupValide(ligne, colonne)) {
            return false;
        }

        grille[ligne][colonne] = symbole;

        if (aGagne(symbole)) {
            termine = true;
            gagnant = symbole;
        } else if (grillePleine()) {
            termine = true;
        }

        return true;
    }

    // Tester si un joueur a gagné
    private boolean aGagne(char symbole) {

        // Lignes
        for (int i = 0; i < 3; i++) {
            if (grille[i][0] == symbole &&
                    grille[i][1] == symbole &&
                    grille[i][2] == symbole) {
                return true;
            }
        }

        // Colonnes
        for (int j = 0; j < 3; j++) {
            if (grille[0][j] == symbole &&
                    grille[1][j] == symbole &&
                    grille[2][j] == symbole) {
                return true;
            }
        }

        // Diagonales
        if (grille[0][0] == symbole &&
                grille[1][1] == symbole &&
                grille[2][2] == symbole) {
            return true;
        }

        return grille[0][2] == symbole &&
                grille[1][1] == symbole &&
                grille[2][0] == symbole;
    }

    // Tester si la grille est pleine
    private boolean grillePleine() {

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {

                if (grille[i][j] == ' ') {
                    return false;
                }
            }
        }

        return true;
    }

    public synchronized boolean estTermine() {
        return termine;
    }

    public synchronized char getGagnant() {
        return gagnant;
    }

    public synchronized String afficherGrille() {

        return
                " " + grille[0][0] + " | " + grille[0][1] + " | " + grille[0][2] + "\n" +
                        "---+---+---\n" +
                        " " + grille[1][0] + " | " + grille[1][1] + " | " + grille[1][2] + "\n" +
                        "---+---+---\n" +
                        " " + grille[2][0] + " | " + grille[2][1] + " | " + grille[2][2];
    }
}