package org.example;

import java.util.*;

public class App {

    private static final int TIMMAR = 24;
    private static final String[] timmar = new String[TIMMAR];
    private static final ArrayList<Integer> elpriser = new ArrayList<>(TIMMAR);

    public static void main(String[] args) {
        Locale.setDefault(new Locale("sv", "SE"));
        Scanner scanner = new Scanner(System.in);
        initTimmar();

        String val = "";

        while (!val.equalsIgnoreCase("e")) {
            printMenu();
            val = scanner.nextLine().toLowerCase();

            switch (val) {
                case "1" -> inputPrices(scanner);
                case "2" -> printMinMaxAverage();
                case "3" -> sortAndPrintPrices();
                case "4" -> cheapestChargingPeriod();
                case "5" -> printAllPrices();
                case "e" -> System.out.println("Avslutar programmet.");
                default -> System.out.print("Ogiltigt alternativ, försök igen.\n");
            }
        }
        scanner.close();
    }

    private static void initTimmar() {
        for (int i = 0; i < TIMMAR; i++) {
            timmar[i] = String.format("%02d-%02d", i, (i + 1) % TIMMAR);
        }
    }

    private static void printMenu() {
        String menu = """
                Elpriser
                ========
                1. Inmatning
                2. Min, Max och Medel
                3. Sortera
                4. Bästa Laddningstid (4h)
                5. Visualisering
                e. Avsluta
                """;
        System.out.print(menu);
    }

    private static void inputPrices(Scanner scanner) {
        elpriser.clear();
        for (int i = 0; i < TIMMAR; i++) {
            System.out.print("Pris klockan " + timmar[i] + ": ");
            elpriser.add(scanner.nextInt());
        }
        scanner.nextLine(); // Rensa scanner
    }

    private static void printMinMaxAverage() {
        if (elpriser.isEmpty()) {
            System.out.print("Ingen data tillgänglig.\n");
            return;
        }

        int min = Collections.min(elpriser);
        int max = Collections.max(elpriser);
        double average = elpriser.stream().mapToInt(Integer::intValue).average().orElse(0);

        int minIndex = elpriser.indexOf(min);
        int maxIndex = elpriser.indexOf(max);

        System.out.printf("Lägsta pris: %02d-%02d, %d öre/kWh\n", minIndex, (minIndex + 1) % TIMMAR, min);
        System.out.printf("Högsta pris: %02d-%02d, %d öre/kWh\n", maxIndex, (maxIndex + 1) % TIMMAR, max);
        System.out.printf("Medelpris: %.2f öre/kWh\n", average);
    }

    private static void sortAndPrintPrices() {
        if (elpriser.isEmpty()) {
            System.out.println("Ingen data att sortera.");
            return;
        }

        // Skapa en array för att hålla index för timmar
        int[] sorted = new int[TIMMAR];
        for (int i = 0; i < TIMMAR; i++) {
            sorted[i] = i;
        }

        // Sortera index baserat på priser
        for (int i = 0; i < TIMMAR; i++) {
            for (int j = i + 1; j < TIMMAR; j++) {
                if (elpriser.get(sorted[i]) < elpriser.get(sorted[j]) ||
                        (elpriser.get(sorted[i]) == elpriser.get(sorted[j]) && sorted[i] > sorted[j])) {
                    int temp = sorted[i];
                    sorted[i] = sorted[j];
                    sorted[j] = temp;
                }
            }
        }

        // Skriv ut de fyra högsta priserna
        for (int i = 0; i < Math.min(4, sorted.length); i++) {
            int hourIndex = sorted[i];
            int nextHour = (hourIndex + 1) % TIMMAR;
            String response;

            // Specialregel för att visa "23-24" istället för "23-00"
            if (hourIndex == 23) {
                response = String.format("%02d-%02d %d öre\n", hourIndex, 24, elpriser.get(hourIndex));
            } else {
                response = String.format("%02d-%02d %d öre\n", hourIndex, nextHour, elpriser.get(hourIndex));
            }

            System.out.print(response);
        }
    }

    private static void cheapestChargingPeriod() {
        if (elpriser.size() < 4) {
            System.out.println("Inte tillräckligt med data för att beräkna laddningstid.");
            return;
        }

        int minSum = Integer.MAX_VALUE;
        int startIndex = 0;

        for (int i = 0; i <= elpriser.size() - 4; i++) {
            int sum = elpriser.get(i) + elpriser.get(i + 1) + elpriser.get(i + 2) + elpriser.get(i + 3);
            if (sum < minSum) {
                minSum = sum;
                startIndex = i;
            }
        }

        double averagePrice = minSum / 4.0;
        System.out.printf("Påbörja laddning klockan %s\n", timmar[startIndex].substring(0, 2));
        System.out.printf("Medelpris 4h: %.1f öre/kWh\n", averagePrice);
    }

    private static void printAllPrices() {
        if (elpriser.isEmpty()) {
            System.out.println("Ingen data tillgänglig.");
            return;
        }

        for (int i = 0; i < elpriser.size(); i++) {
            System.out.printf("Klockan %s: %d öre\n", timmar[i], elpriser.get(i));
        }
    }
}