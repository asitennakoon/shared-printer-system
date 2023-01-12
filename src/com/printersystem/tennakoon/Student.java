package com.printersystem.tennakoon;

import java.util.Random;

public class Student extends Thread {
    private final LaserPrinter printer;

    public Student(ThreadGroup group, String name, LaserPrinter printer) {
        super(group, name);
        this.printer = printer;
    }

    @Override
    public void run() {
        Random random = new Random();
        int pageTotal = 0;

        for (int i = 0; i < 5; i++) {
            int documentLength = random.nextInt(16) + 10;
            pageTotal = pageTotal + documentLength;

            Document document = new Document(this.getName(), "DOC" + i, documentLength);
            printer.printDocument(document);

            // Student is only required to sleep between each printing request. Therefore, after the final request,
            // he/she wouldn't go in to sleep
            if (i != 4) {
                try {
                    sleep(random.nextInt(1001) + 1000);
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        System.out.printf("%n[%s] Finished Printing: 5 Documents, %d pages", this.getName(), pageTotal);
    }
}