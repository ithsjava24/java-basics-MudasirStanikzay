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
        System.out.println("4. Bästa Laddningstid (4h)");
        System.out.println("e. Avsluta");
        System.out.print("Välj ett alternativ: ");
    }

    private static void hanteraInmatning(Scanner scanner) {
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
                System.out.printf("%s är inte ett giltigt tal.%n", pris);
                return;
            }
        }

        System.out.println("Priser inmatade.");
        System.out.println(STR."Inmatade priser: \{elpriser}");
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

        System.out.println(STR."Lägsta pris: \{minTid}, \{(int) minPris} öre/kWh");
        System.out.println(STR."Högsta pris: \{maxTid}, \{(int) maxPris} öre/kWh");
        System.out.printf("Medelpris: %.2f öre/kWh\n", medelPris);
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
            String pris = STR."\{elpriser.get(i).intValue()} öre";
            timmarOchPriser.add(STR."\{tid} \{pris}");
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
            System.out.println("Du måste mata in minst 4 priser för att beräkna bästa laddningstid.");
            return;
        }

        double lägstaTotalpris = Double.MAX_VALUE;
        int bästaStartTimme = 0;

        for (int i = 0; i <= elpriser.size() - 4; i++) {
            double totalpris = elpriser.get(i) + elpriser.get(i + 1) + elpriser.get(i + 2) + elpriser.get(i + 3);
            if (totalpris < lägstaTotalpris) {
                lägstaTotalpris = totalpris;
                bästaStartTimme = i;
            }
        }

        double medelpris = lägstaTotalpris / 4;
        String response = String.format("Påbörja laddning klockan %d\nMedelpris 4h: %.2f öre/kWh", bästaStartTimme, medelpris);
        System.out.println(response);
    }

    private static void avslutaProgram() {
        System.out.println("Avslutar programmet...");
    }
}