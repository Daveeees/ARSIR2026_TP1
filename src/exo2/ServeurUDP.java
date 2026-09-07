package exo2;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

public class ServeurUDP {
    public static void main(String[] args) {
        try {
            // 1 - Création du canal
            DatagramSocket socketServeur = new DatagramSocket(null);
            // 2 - Réservation du port
            InetSocketAddress adresse = new InetSocketAddress("localhost", 6666);
            socketServeur.bind(adresse);

            System.out.println("Serveur démarré...");

            boolean actif = true;
            while (actif) {
                byte[] recues = new byte[1024]; // tampon de réception (à recréer à chaque tour !)

                // 3 - Recevir
                DatagramPacket paquetRecu = new DatagramPacket(recues, recues.length);
                socketServeur.receive(paquetRecu); // bloquant : attend un message
                NowDate t1prim = new NowDate();

                String t1 = new String(paquetRecu.getData(), 0, paquetRecu.getLength());
                System.out.println("Reçu: " + t1);

                InetAddress adrClient = paquetRecu.getAddress();
                int prtClient = paquetRecu.getPort();

                //TimeUnit.SECONDS.sleep(1);
                // 4 - Émettre l'heure courante
                NowDate t2prim = new NowDate();
                String reponse =  t1 + ";"+  t1prim.TimeToString() + ";"+  t2prim.TimeToString();

                byte[] envoyees = reponse.getBytes();
                DatagramPacket paquetEnvoye = new DatagramPacket(envoyees, envoyees.length, adrClient, prtClient);
                socketServeur.send(paquetEnvoye);

            }

            // 5 - Libérer le canal
            socketServeur.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}