package com.printersystem.tennakoon;

import java.util.Random;

public class PaperTechnician extends Thread {
    private final LaserPrinter printer;

    public PaperTechnician(ThreadGroup group, String name, LaserPrinter printer) {
        super(group, name);
        this.printer = printer;
    }

    @Override
    public void run() {
        Random random = new Random();

        for (int i = 0; i < 3; i++) {
            try {
                printer.refillPaper();
                sleep(random.nextInt(1001) + 1000);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println(
                "[PaperTechnician] Finished, packs of paper used: " + printer.getNumberOfTimesPapersRefilled());
    }
}