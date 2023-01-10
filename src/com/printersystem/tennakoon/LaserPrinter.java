package com.printersystem.tennakoon;

public class LaserPrinter implements ServicePrinter {
    private final String printerId;
    private final ThreadGroup students;
    private int currentPaperLevel;
    private int currentTonerLevel;
    private int numberOfDocsPrinted;
    private int numberOfTimesPapersRefilled;
    private int numberOfTonerCartridgesReplaced;

    public LaserPrinter(String printerId, ThreadGroup students) {
        this.printerId = printerId;
        this.students = students;
        this.currentPaperLevel = ServicePrinter.FULL_PAPER_TRAY;
        this.currentTonerLevel = ServicePrinter.FULL_TONER_LEVEL;
        this.numberOfDocsPrinted = 0;
        this.numberOfTimesPapersRefilled = 0;
        this.numberOfTonerCartridgesReplaced = 0;
    }

    @Override
    public synchronized void printDocument(Document document) {
        System.out.println("[" + document.getUserId() + "] Printing " + document.getDocumentName() + " of "
                + document.getNumberOfPages() + " pages");

        while (currentPaperLevel < document.getNumberOfPages() || currentTonerLevel < document.getNumberOfPages()) {
            try {
                System.out.println("[" + document.getUserId() + "] Waiting for resources of the printer to be refilled");
                wait();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }

        currentPaperLevel -= document.getNumberOfPages();
        currentTonerLevel -= document.getNumberOfPages();
        numberOfDocsPrinted++;
        System.out.println("[" + document.getUserId() + "] Printed " + document.getNumberOfPages() + " pages of "
                + document.getDocumentName());

        notifyAll();
    }

    @Override
    public synchronized void refillPaper() {
        while (currentPaperLevel + ServicePrinter.SHEETS_PER_PACK > ServicePrinter.FULL_PAPER_TRAY) {
            try {
                if (students.activeCount() > 0) {
                    System.out.println("[PaperTechnician] Waiting to refill papers");
                    wait(5000);
                } else {
                    System.out.println("[PaperTechnician] No students are currently using the printer");
                    break;
                }
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }

        //TODO: Should there be a check for any active students? Yes
        if (currentPaperLevel + ServicePrinter.SHEETS_PER_PACK <= ServicePrinter.FULL_PAPER_TRAY) {
            currentPaperLevel += ServicePrinter.SHEETS_PER_PACK;
            numberOfTimesPapersRefilled++;

            String[] suffixes = {"st", "nd", "rd"};
            System.out.println("[PaperTechnician] Refilled papers for the " + numberOfTimesPapersRefilled
                    + suffixes[numberOfTimesPapersRefilled - 1] + " time");
        }

        notifyAll();
    }

    @Override
    public synchronized void replaceTonerCartridge() {
        while (currentTonerLevel >= ServicePrinter.MINIMUM_TONER_LEVEL) {
            try {
                if (students.activeCount() > 0) {
                    System.out.println("[TonerTechnician] Waiting to replace the toner cartridge");
                    wait(5000);
                } else {
                    System.out.println("[TonerTechnician] No students are currently using the printer");
                    break;
                }
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }

        if (currentTonerLevel < ServicePrinter.MINIMUM_TONER_LEVEL) {
            currentTonerLevel = ServicePrinter.FULL_TONER_LEVEL;
            numberOfTonerCartridgesReplaced++;

            String[] suffixes = {"st", "nd", "rd"};
            System.out.println("[TonerTechnician] Replaced the toner cartridge for the "
                    + numberOfTonerCartridgesReplaced + suffixes[numberOfTonerCartridgesReplaced - 1] + " time");
        }

        notifyAll();
    }

    public synchronized int getNumberOfTimesPapersRefilled() {
        return numberOfTimesPapersRefilled;
    }

    public synchronized int getNumberOfTonerCartridgesReplaced() {
        return numberOfTonerCartridgesReplaced;
    }

    @Override
    public synchronized String toString() {
        return "[ PrinterID: " + printerId
                + ", Paper Level: " + currentPaperLevel
                + ", Toner Level: " + currentTonerLevel
                + ", Documents Printed: " + numberOfDocsPrinted
                + ", Paper Packs Refilled: " + numberOfTimesPapersRefilled
                + ", Toner Cartridges Replaced: " + numberOfTonerCartridgesReplaced + " ]";
    }
}