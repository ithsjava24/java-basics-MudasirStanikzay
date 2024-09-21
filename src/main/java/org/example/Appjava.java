package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Appjava {

    static List<Double> elpriser = new ArrayList<>();

    public static void main(String[] args) {
        startMeny();
    }

    private static void startMeny() {
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

    private static void skrivUtMeny() {
        System.out.println("Elpriser");
        System.out.println("========");
        System.out.println("1. Inmatning");
        System.out.println("2. Min, Max och Medel");
        System.out.println("3. Sortera");
        System.out.println("4. Basta Laddningstid (4h)");
        System.out.println("e. Avsluta");
        System.out.print("Valj ett alternativ: ");
    }

    private static void hanteraInmatning(Scanner scanner) {
        System.out.println("Ange elpriser for 24 timmar (separera med mellanslag): ");
        String input = scanner.nextLine();
        String[] priser = input.split(" ");

        if (priser.length != 24) {
            System.out.println("Du maste mata in exakt 24 priser.");
            return;
        }

        elpriser.clear();
        for (String pris : priser) {
            try {
                elpriser.add(Double.parseDouble(pris));
            } catch (NumberFormatException e) {
                System.out.printf("%s ar inte ett giltigt tal.%n", pris);
                return;
            }
        }

        System.out.println("Priser inmatade.");
        System.out.println("Inmatade priser: " + elpriser);
    }

    private static void visaMinMaxMedel() {
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

        System.out.println("Lagsta pris: " + minTid + ", " + (int) minPris + " ore/kWh");
        System.out.println("Hogsta pris: " + maxTid + ", " + (int) maxPris + " ore/kWh");
        System.out.printf("Medelpris: %.2f ore/kWh\n", medelPris);
    }

    private static String formateraTid(int timme) {
        int slutTimme = (timme + 1) % 24;
        return String.format("%02d-%02d", timme, slutTimme);
    }

    private static void sorteraPriser() {
        if (elpriser.isEmpty()) {
            System.out.println("Inga priser inmatade.");
            return;
        }

        List<String> timmarOchPriser = new ArrayList<>();

        for (int i = 0; i < elpriser.size(); i++) {
            String tid = formateraTid(i);
            String pris = (int) Math.round(elpriser.get(i)) + " ore"; // Rundar och konverterar
            timmarOchPriser.add(tid + " " + pris);
        }

        timmarOchPriser.sort((a, b) -> {
            int prisA = Integer.parseInt(a.split(" ")[1]);
            int prisB = Integer.parseInt(b.split(" ")[1]);
            return Integer.compare(prisB, prisA);
        });

        StringBuilder response = new StringBuilder();
        for (String rad : timmarOchPriser) {
            response.append(rad).append("\n");
        }

        System.out.println(response.toString().trim());
    }

    private static void visaBastaLaddningstid() {
        if (elpriser.size() < 4) {
            System.out.println("Du maste mata in minst 4 priser for att berakna basta laddningstid.");
            return;
        }

        double lagstaTotalpris = Double.MAX_VALUE;
        int bastaStartTimme = 0;

        for (int i = 0; i <= elpriser.size() - 4; i++) {
            double totalpris = elpriser.get(i) + elpriser.get(i + 1) + elpriser.get(i + 2) + elpriser.get(i + 3);
            if (totalpris < lagstaTotalpris) {
                lagstaTotalpris = totalpris;
                bastaStartTimme = i;
            }
        }

        double medelpris = lagstaTotalpris / 4;
        String response = String.format("Paaborja laddning klockan %d\nMedelpris 4h: %.2f ore/kWh", bastaStartTimme, medelpris);
        System.out.println(response);
    }

    private static void avslutaProgram() {
        System.out.println("Avslutar programmet...");
    }
}