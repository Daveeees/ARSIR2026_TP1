import java.time.LocalDate;

public static void main(String[] args){// Méthode principale
     try{
         // 1 - Création du canal
         DatagramSocket socketServeur = new DatagramSocket(null);
         // 2 - Réservation du port
         InetSocketAddress adresse = new InetSocketAddress("localhost", 6666);
         socketServeur.bind(adresse);
         byte[] recues = new byte[1024]; // tampon d'émission
         byte[] envoyees; // tampon de réception
         // 3 - Recevoir
         DatagramPacket paquetRecu = new DatagramPacket(recues, recues.length);
         socketServeur.receive(paquetRecu);
         String message = new String(paquetRecu.getData(), 0, paquetRecu.getLength());
         System.out.println("Reçu: " + message);
         // 4 - Émettre
         InetAddress adrClient = paquetRecu.getAddress(); int prtClient = paquetRecu.getPort();
         Date dateActuelle =  new Date();
         //String reponse = "Accusé de réception"; envoyees = reponse.getBytes();
         String dateActuelleString = dateActuelle.localDate.toString();
         byte[] dateEnvoyee = dateActuelleString.getBytes();
         DatagramPacket paquetEnvoye = new DatagramPacket(dateEnvoyee, dateEnvoyee.length, adrClient, prtClient);
         socketServeur.send(paquetEnvoye);
         // 5 - Libérer le canal
         socketServeur.close();
     }catch(Exception e){
         System.err.println(e);
         }
}
