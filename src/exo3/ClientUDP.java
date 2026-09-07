package exo3;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;
import java.time.Duration;
import java.time.LocalTime;

public class ClientUDP {
    public static void main(String[] args) {
        try {
            // 1 - Création du canal
            DatagramSocket socketClient = new DatagramSocket();
            InetAddress adresseServeur = InetAddress.getByName("localhost");

            System.out.println("Connecté au serveur " + adresseServeur.getHostAddress() + " sur le port 6666");


            NowDate t1 = new NowDate();
            String message = t1.TimeToString();

            // 2 - Émettre
            byte[] envoyees = message.getBytes();
            DatagramPacket messageEnvoye = new DatagramPacket(envoyees, envoyees.length, adresseServeur, 6666);
            socketClient.send(messageEnvoye);


            // 3 - Recevoir la réponse
            byte[] recues = new byte[1024];
            DatagramPacket paquetRecu = new DatagramPacket(recues, recues.length);
            socketClient.receive(paquetRecu);
            NowDate t2 = new NowDate();
            String reponse = new String(paquetRecu.getData(), 0, paquetRecu.getLength());
            System.out.println("Depuis le serveur: " + reponse);

            // 3 - Découpage : parts[0] = T1, parts[1] = T1', parts[2] = T2'
            String[] parts = reponse.split(";");
            LocalTime t1Client = t1.getLocalTime();
            LocalTime t2Client = t2.getLocalTime();
            LocalTime t1Serveur = LocalTime.parse(parts[1]);
            LocalTime t2Serveur = LocalTime.parse(parts[2]);

            //(T2 - T1)
            Duration dureeClient = Duration.between(t1Client, t2Client);

            //(T2' - T1')
            Duration dureeServeur = Duration.between(t1Serveur, t2Serveur);

            // delta = (T2 - T1) - (T2' - T1')
            Duration delta = dureeClient.minus(dureeServeur);


            // theta = ((T1' - T1) + (T2' - T2)) / 2
            long milieuServeurNanos = (t1Serveur.toNanoOfDay() + t2Serveur.toNanoOfDay()) / 2;
            long milieuClientNanos = (t1Client.toNanoOfDay() + t2Client.toNanoOfDay()) / 2;
            long thetaNanos = milieuServeurNanos - milieuClientNanos;

            Duration theta = Duration.ofNanos(thetaNanos);

            // 6 - Correction de l'horloge courante
            LocalTime heureCourante = LocalTime.now();
            LocalTime heureCorrigee = heureCourante.plus(theta);

            // Affichage des résultats
            System.out.println("Temps aller-retour total (T2 - T1)    : " + dureeClient.toNanos() / 1_000_000.0 + " ms");
            System.out.println("Temps traitement serveur (T2' - T1') : " + dureeServeur.toNanos() / 1_000_000.0 + " ms");
            System.out.println("Délai réseau (delta)                 : " + delta.toNanos() / 1_000_000.0 + " ms");
            System.out.println("Décalage horloge (thêta) : " + (thetaNanos / 1_000_000.0) + " ms");
            System.out.println("Heure locale brute      : " + heureCourante);
            System.out.println("Heure corrigée alignée  : " + heureCorrigee);


            // 4 - Libérer le canal
            socketClient.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}