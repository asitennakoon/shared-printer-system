package com.printersystem.tennakoon;

public class LaserPrinter implements ServicePrinter {
    private final String printerId;
    private final ThreadGroup students;
    private int currentPaperLevel;
    private int currentTonerLevel;
    private int numberOfDocsPrinted;
    private int numberOfTimesPapersRefilled;
    private int numberOfTonerCartridgesReplaced;

    public LaserPrinter(String printerId, ThreadGroup students, int currentPaperLevel, int currentTonerLevel) {
        this.printerId = printerId;
        this.students = students;
        this.currentPaperLevel = currentPaperLevel;
        this.currentTonerLevel = currentTonerLevel;
        this.numberOfDocsPrinted = 0;
        this.numberOfTimesPapersRefilled = 0;
        this.numberOfTonerCartridgesReplaced = 0;
    }

    @Override
    public synchronized void printDocument(Document document) {
        System.out.printf("%n[ %s ] Printing %s of %d pages",
                document.getUserId(), document.getDocumentName(), document.getNumberOfPages());

        while (currentPaperLevel < document.getNumberOfPages() || currentTonerLevel < document.getNumberOfPages()) {

            if (currentPaperLevel < document.getNumberOfPages() && currentTonerLevel < document.getNumberOfPages()) {
                System.out.printf("%n[ %s ] Waiting for resources of the printer to be refilled. Current Paper Level: %d, Current Toner Level: %d",
                        document.getUserId(), currentPaperLevel, currentTonerLevel);
            } else if (currentPaperLevel < document.getNumberOfPages()) {
                System.out.printf("%n[ %s ] Waiting for the paper tray of the printer to be refilled. Current Paper Level: %d",
                        document.getUserId(), currentPaperLevel);
            } else if (currentTonerLevel < document.getNumberOfPages()) {
                System.out.printf("%n[ %s ] Waiting for the toner cartridge of the printer to be replaced. Current Toner Level: %d",
                        document.getUserId(), currentTonerLevel);
            }

            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }

        currentPaperLevel -= document.getNumberOfPages();
        currentTonerLevel -= document.getNumberOfPages();
        numberOfDocsPrinted++;
        System.out.printf("%n[ %s ] Printed %d pages of %s. Current Paper Level: %d, Current Toner Level: %d",
                document.getUserId(), document.getNumberOfPages(), document.getDocumentName(), currentPaperLevel, currentTonerLevel);

        notifyAll();
    }

    @Override
    public synchronized void refillPaper() {
        while (currentPaperLevel + ServicePrinter.SHEETS_PER_PACK > ServicePrinter.FULL_PAPER_TRAY) {

            if (students.activeCount() > 0) {
                System.out.printf("%n[PaperTechnician] Waiting to refill papers. Current Paper Level: %d", currentPaperLevel);
                try {
                    wait(5000);
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                System.out.println("[PaperTechnician] No students are currently using the printer");
                break;
            }
        }

        if (currentPaperLevel + ServicePrinter.SHEETS_PER_PACK <= ServicePrinter.FULL_PAPER_TRAY && students.activeCount() > 0) {
            currentPaperLevel += ServicePrinter.SHEETS_PER_PACK;
            numberOfTimesPapersRefilled++;

            String[] suffixes = {"st", "nd", "rd"};
            System.out.printf("%n[PaperTechnician] Refilled papers for the %d%s time. Current Paper Level: %d",
                    numberOfTimesPapersRefilled, suffixes[numberOfTimesPapersRefilled - 1], currentPaperLevel);
        }

        notifyAll();
    }

    @Override
    public synchronized void replaceTonerCartridge() {
        while (currentTonerLevel >= ServicePrinter.MINIMUM_TONER_LEVEL) {

            if (students.activeCount() > 0) {
                System.out.printf("%n[TonerTechnician] Waiting to replace the toner cartridge. Current Toner Level: %d", currentTonerLevel);
                try {
                    wait(5000);
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                System.out.println("[TonerTechnician] No students are currently using the printer");
                break;
            }
        }

        if (currentTonerLevel < ServicePrinter.MINIMUM_TONER_LEVEL && students.activeCount() > 0) {
            currentTonerLevel = ServicePrinter.FULL_TONER_LEVEL;
            numberOfTonerCartridgesReplaced++;

            String[] suffixes = {"st", "nd", "rd"};
            System.out.printf("%n[TonerTechnician] Replaced the toner cartridge for the %d%s time. Current Toner Level: %d",
                    numberOfTonerCartridgesReplaced, suffixes[numberOfTonerCartridgesReplaced - 1], currentTonerLevel);
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