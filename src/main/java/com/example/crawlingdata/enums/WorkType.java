package com.example.crawlingdata.enums;

public enum WorkType {
    FULL_TIME("Hydrogen"),
    PART_TIME("Helium"),
    FRESHER("Neon");
    private final String label;
    private WorkType(String label) {
        this.label = label;
    }
}
