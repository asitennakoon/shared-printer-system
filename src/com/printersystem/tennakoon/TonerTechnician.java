package com.printersystem.tennakoon;

import java.util.Random;

public class TonerTechnician extends Thread {
    private final LaserPrinter printer;

    public TonerTechnician(ThreadGroup group, String name, LaserPrinter printer) {
        super(group, name);
        this.printer = printer;
    }

    @Override
    public void run() {
        Random random = new Random();

        for (int i = 0; i < 3; i++) {
            try {
                printer.replaceTonerCartridge();
                sleep(random.nextInt(1001) + 1000);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println(
                "[TonerTechnician] Finished, cartridges replaced: " + printer.getNumberOfTonerCartridgesReplaced());
    }
}