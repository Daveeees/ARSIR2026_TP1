import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class ClientUDP {
    public static void main(String[] args) {
        try {
            // 1 - Création du canal
            DatagramSocket socketClient = new DatagramSocket();
            InetAddress adresseServeur = InetAddress.getByName("localhost");

            System.out.println("Connecté au serveur " + adresseServeur.getHostAddress() + " sur le port 6666");

            Scanner clavier = new Scanner(System.in);

            boolean actif = true;
            while (actif) {
                System.out.print("Message à envoyer (exit pour quitter): ");
                String message = clavier.nextLine();

                // 2 - Émettre
                byte[] envoyees = message.getBytes();
                DatagramPacket messageEnvoye = new DatagramPacket(envoyees, envoyees.length, adresseServeur, 6666);
                socketClient.send(messageEnvoye);

                if (message.trim().equalsIgnoreCase("exit")) {
                    actif = false;
                } else {
                    // 3 - Recevoir la réponse
                    byte[] recues = new byte[1024];
                    DatagramPacket paquetRecu = new DatagramPacket(recues, recues.length);
                    socketClient.receive(paquetRecu);
                    String reponse = new String(paquetRecu.getData(), 0, paquetRecu.getLength());
                    System.out.println("Depuis le serveur (heure): " + reponse);
                }
            }

            clavier.close();
            // 4 - Libérer le canal
            socketClient.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}