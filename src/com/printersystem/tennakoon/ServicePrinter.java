package com.printersystem.tennakoon;

public interface ServicePrinter extends Printer {
    int FULL_PAPER_TRAY = 250;
    int SHEETS_PER_PACK = 50;

    int FULL_TONER_LEVEL = 500;
    int MINIMUM_TONER_LEVEL = 10;
    int PAGES_PER_TONER_CARTRIDGE = 500;

    void refillPaper();

    void replaceTonerCartridge();
}