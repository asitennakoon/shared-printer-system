package com.printersystem.tennakoon;

public class PrintingSystem {
    public static void main(String[] args) {
        ThreadGroup studentGroup = new ThreadGroup("[PrintingSystem] Student Thread Group");
        System.out.println("[PrintingSystem] Thread group for students created");
        LaserPrinter laserPrinter = new LaserPrinter(
                "lp-TT.24", studentGroup, ServicePrinter.FULL_PAPER_TRAY, ServicePrinter.FULL_TONER_LEVEL);
        System.out.println("[PrintingSystem] LaserPrinter monitor created");

        Student student1 = new Student(studentGroup, "Student1", laserPrinter);
        Student student2 = new Student(studentGroup, "Student2", laserPrinter);
        Student student3 = new Student(studentGroup, "Student3", laserPrinter);
        Student student4 = new Student(studentGroup, "Student4", laserPrinter);
        System.out.println("[PrintingSystem] Student threads created");

        ThreadGroup technicianGroup = new ThreadGroup("Technician Thread Group");
        System.out.println("[PrintingSystem] Thread group for technicians created");

        PaperTechnician paperTechnician = new PaperTechnician(technicianGroup, "PaperTechnician", laserPrinter);
        TonerTechnician tonerTechnician = new TonerTechnician(technicianGroup, "TonerTechnician", laserPrinter);
        System.out.println("[PrintingSystem] Technician threads created");

        System.out.println("[PrintingSystem] Starting Student1 thread...");
        student1.start();
        System.out.println("[PrintingSystem] Starting Student2 thread...");
        student2.start();
        System.out.println("[PrintingSystem] Starting Student3 thread...");
        student3.start();
        System.out.println("[PrintingSystem] Starting Student4 thread...");
        student4.start();
        System.out.println("[PrintingSystem] Starting PaperTechnician thread...");
        paperTechnician.start();
        System.out.println("[PrintingSystem] Starting TonerTechnician thread...");
        tonerTechnician.start();

        try {
            student1.join();
            student2.join();
            student3.join();
            student4.join();
            paperTechnician.join();
            tonerTechnician.join();

            System.out.printf("%n[PrintingSystem] Student1 Terminated: %s", !student1.isAlive());
            System.out.printf("%n[PrintingSystem] Student2 Terminated: %s", !student2.isAlive());
            System.out.printf("%n[PrintingSystem] Student3 Terminated: %s", !student3.isAlive());
            System.out.printf("%n[PrintingSystem] Student4 Terminated: %s", !student4.isAlive());
            System.out.printf("%n[PrintingSystem] PaperTechnician Terminated: %s", !paperTechnician.isAlive());
            System.out.printf("%n[PrintingSystem] TonerTechnician Terminated: %s", !tonerTechnician.isAlive());

            System.out.println("\n[PrintingSystem] All threads terminated\n\n[PrintingSystem] " + laserPrinter);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
