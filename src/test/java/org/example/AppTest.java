package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

class AppTest {

    // Lista för att lagra inmatade elpriser
    static List<Double> elpriser = new ArrayList<>();

    public static void main(String[] args) {
        startMeny();
    }

    public static void startMeny() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            skrivUtMeny();
            String val = scanner.nextLine();

            switch (val) {
                case "1":
                    hanteraInmatning(scanner);
                    break;
                case "2":
                    visaMinMaxMedel();
                    break;
                case "3":
                    sorteraPriser();
                    break;
                case "4":
                    visaBastaLaddningstid();
                    break;
                case "e":
                case "E":
                    avslutaProgram();
                    running = false;
                    break;
                default:
                    System.out.println("Ogiltigt val. Försök igen.");
                    break;
            }
            System.out.println();
        }

        scanner.close();
    }

    public static void skrivUtMeny() {
        System.out.println("Elpriser");
        System.out.println("========");
        System.out.println("1. Inmatning");
        System.out.println("2. Min, Max och Medel");
        System.out.println("3. Sortera");
        System.out.println("4. Bästa Laddningstid (4h)");
        System.out.println("e. Avsluta");
        System.out.print("Välj ett alternativ: ");
    }

    public static void hanteraInmatning(Scanner scanner) {
        System.out.println("Ange elpriser för 24 timmar (separera med mellanslag): ");
        String input = scanner.nextLine();
        String[] priser = input.split(" ");

        if (priser.length != 24) {
            System.out.println("Du måste mata in exakt 24 priser.");
            return;
        }

        elpriser.clear();
        for (String pris : priser) {
            try {
                elpriser.add(Double.parseDouble(pris));
            } catch (NumberFormatException e) {
                System.out.println(pris + " är inte ett giltigt tal.");
                return; // Om något går fel, avbryt inmatning
            }
        }

        System.out.println("Priser inmatade.");
    }

    public static void visaMinMaxMedel() {
        if (elpriser.isEmpty()) {
            System.out.println("Inga priser inmatade.");
            return;
        }

        double minPris = Collections.min(elpriser);
        double maxPris = Collections.max(elpriser);
        int minIndex = elpriser.indexOf(minPris);
        int maxIndex = elpriser.indexOf(maxPris);
        double medelPris = elpriser.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);

        String minTid = formateraTid(minIndex);
        String maxTid = formateraTid(maxIndex);

        System.out.println("Lägsta pris: " + minTid + ", " + (int)minPris + " öre/kWh");
        System.out.println("Högsta pris: " + maxTid + ", " + (int)maxPris + " öre/kWh");
        System.out.printf("Medelpris: %.2f öre/kWh\n", medelPris);
    }

    public static String formateraTid(int timme) {
        int startTimme = timme;
        int slutTimme = (timme + 1) % 24;
        return String.format("%02d-%02d", startTimme, slutTimme);
    }

    public static void sorteraPriser() {
        if (elpriser.isEmpty()) {
            System.out.println("Inga priser inmatade.");
            return;
        }

        Collections.sort(elpriser);
        System.out.println("Sorterade priser: " + elpriser);
    }

    public static void visaBastaLaddningstid() {
        System.out.println("Funktionen för bästa laddningstid kommer snart!");
    }

    public static void avslutaProgram() {
        System.out.println("Avslutar programmet...");
    }
}