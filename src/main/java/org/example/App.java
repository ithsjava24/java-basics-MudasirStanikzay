package org.example;

import java.util.Scanner;

public class App {

    private static int[] priser = new int[24];

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String val;

        do {
            String menu = """
            Elpriser
            ========
            1. Inmatning
            2. Min, Max och Medel
            3. Sortera
            4. Bästa Laddningstid (4h)
            e. Avsluta
            """;

            System.out.print(menu);
            val = scanner.nextLine();

            switch (val) {
                case "1":
                    inmatning(scanner);
                    break;
                case "2":
                    minMaxMedel();
                    break;
                case "3":
                    sortera();
                    break;
                case "4":
                    bästaLaddningstid();
                    break;
                case "e":
                case "E":
                    System.out.print("Avslutar programmet.\n");
                    break;
                default:
                    System.out.print("Ogiltligt alternativ, försök igen.\n");
            }
        } while (!val.equalsIgnoreCase("e"));

        scanner.close();
    }

    private static void inmatning(Scanner scanner) {
        System.out.print("Skriv in elpriserna för varje timme (0-23):\n");
        for (int i = 0; i <= 23; i++) {
            System.out.print(i + ": ");
            priser[i] = Integer.parseInt(scanner.nextLine());
        }
    }

    private static void minMaxMedel() {
        int min = priser[0], max = priser[0];
        int minHour = 0, maxHour = 0;
        int total = 0;

        for (int i = 0; i < 24; i++) {
            total += priser[i];
            if (priser[i] < min) {
                min = priser[i];
                minHour = i;
            }
            if (priser[i] > max) {
                max = priser[i];
                maxHour = i;
            }
        }

        double medel = total / 24.0;

        // Se till att formatet matchar exakt det som testerna kontrollerar
        String response = String.format("""
            Lägsta pris: %02d-%02d, %d öre/kWh
            Högsta pris: %02d-%02d, %d öre/kWh
            Medelpris: %.2f öre/kWh
            """,
                minHour, (minHour + 1) % 24, min,
                maxHour, (maxHour + 1) % 24, max,
                medel);

        System.out.print(response);
    }

    private static void sortera() {
        int[] sorted  = new int[24];

        for (int i = 0; i < 24; i++) {
            sorted[i] = i;
        }

        for (int i = 0; i < 24; i++) {
            for (int j = i + 1; j < 24; j++) {
                if (priser[sorted[i]] < priser[sorted[j]] ||
                        (priser[sorted[i]] == priser[sorted[j]] && sorted[i] > sorted[j])) {
                    int temp = sorted[i];
                    sorted[i] = sorted[j];
                    sorted[j] = temp;
                }
            }
        }

        for (int i : sorted) {
            int nextHour = (i + 1) % 24;
            String response;

            // Specialregel för att visa "23-24" istället för "23-00"
            if (i == 23) {
                response = String.format("""
            %02d-%02d %d öre
            """, i, 24, priser[i]);
            } else {
                response = String.format("""
            %02d-%02d %d öre
            """, i, nextHour, priser[i]);
            }

            System.out.print(response);
        }
    }

    private static void bästaLaddningstid() {
        int minTotal = Integer.MAX_VALUE;
        int startHour = 0;

        for (int i = 0; i <= 20; i++) { // Man kan börja ladda mellan timme 0-20
            int total = priser[i] + priser[i + 1] + priser[i + 2] + priser[i + 3];
            if (total < minTotal) {
                minTotal = total;
                startHour = i;
            }
        }

        double medel = minTotal / 4.0;

        String response = String.format("""
        Påbörja laddning klockan %02d
        Medelpris 4h: %.1f öre/kWh
        """,
                startHour, medel);

        System.out.print(response);
    }
}