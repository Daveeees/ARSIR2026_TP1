import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.time.LocalTime;

public class ServeurUDP {
    public static void main(String[] args) {
        try {
            // 1 - Création du canal
            DatagramSocket socketServeur = new DatagramSocket(null);
            // 2 - Réservation du port
            InetSocketAddress adresse = new InetSocketAddress("localhost", 6666);
            socketServeur.bind(adresse);

            System.out.println("Serveur démarré, en attente de messages...");

            boolean actif = true;
            while (actif) {
                byte[] recues = new byte[1024]; // tampon de réception (à recréer à chaque tour !)

                // 3 - Recevir
                DatagramPacket paquetRecu = new DatagramPacket(recues, recues.length);
                socketServeur.receive(paquetRecu); // bloquant : attend un message

                String message = new String(paquetRecu.getData(), 0, paquetRecu.getLength());
                System.out.println("Reçu: " + message);

                InetAddress adrClient = paquetRecu.getAddress();
                int prtClient = paquetRecu.getPort();

                // Condition de sortie
                if (message.trim().equalsIgnoreCase("exit")) {
                    System.out.println("Message exit reçu, le client a quitté");
                } else {
                    // 4 - Émettre l'heure courante
                    NowDate dateActuelle = new NowDate();
                    String reponse = dateActuelle.DateToString() +  " " + dateActuelle.TimeToString();
                    byte[] envoyees = reponse.getBytes();
                    DatagramPacket paquetEnvoye = new DatagramPacket(envoyees, envoyees.length, adrClient, prtClient);
                    socketServeur.send(paquetEnvoye);
                }
            }

            // 5 - Libérer le canal
            socketServeur.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}