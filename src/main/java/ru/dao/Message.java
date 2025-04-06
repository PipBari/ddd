package ru.dao;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Message {
    private final String id;
    private final String authorRole;
    private final String content;
    private final List<Attachment> attachments = new ArrayList<>();
    private final Instant createdAt;
    private boolean confirmed;

    public Message(String authorRole, String content, List<Attachment> attachments) {
        this.id = UUID.randomUUID().toString();
        this.authorRole = authorRole;
        this.content = content;
        if (attachments != null) {
            this.attachments.addAll(attachments);
        }
        this.createdAt = Instant.now();
        this.confirmed = false;
    }

    public void confirm() {
        this.confirmed = true;
    }

    public String getId() {
        return id;
    }

    public String getAuthorRole() {
        return authorRole;
    }

    public String getContent() {
        return content;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}

