package ru.dao;

import java.util.UUID;

public class Attachment {
    private final String id;
    private final String fileName;
    private final String data;

    public Attachment(String fileName, String data) {
        this.id = UUID.randomUUID().toString();
        this.fileName = fileName;
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public String getData() {
        return data;
    }
}

