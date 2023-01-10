package com.printersystem.tennakoon;

class Document {
    private final String userId;
    private final String documentName;
    private final int numberOfPages;

    public Document(String uid, String name, int length) {
        this.userId = uid;
        this.documentName = name;
        this.numberOfPages = length;
    }

    public String getUserId() {
        return userId;
    }

    public String getDocumentName() {
        return documentName;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public String toString() {
        return "com.printersystem.tennakoon.Document[ " +
                "UserId: " + userId + ", " +
                "Name: " + documentName + ", " +
                "Pages: " + numberOfPages +
                "]";
    }
}