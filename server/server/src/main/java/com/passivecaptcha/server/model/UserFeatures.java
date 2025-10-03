package com.passivecaptcha.server.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_features")
public class UserFeatures {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int numPointerMoves;
    private double avgPointerSpeed;
    private boolean usedKeyboard;
    private long sessionDuration;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    private String ipAddress;
    private String userAgent;

    // --- Constructors ---
    public UserFeatures() {
        // JPA requires a no-args constructor
    }

    public UserFeatures(int numPointerMoves, double avgPointerSpeed, boolean usedKeyboard, long sessionDuration, String ipAddress, String userAgent) {
        this.numPointerMoves = numPointerMoves;
        this.avgPointerSpeed = avgPointerSpeed;
        this.usedKeyboard = usedKeyboard;
        this.sessionDuration = sessionDuration;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
    }

    // --- Getters & Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public int getNumPointerMoves() { return numPointerMoves; }
    public void setNumPointerMoves(int numPointerMoves) { this.numPointerMoves = numPointerMoves; }

    public double getAvgPointerSpeed() { return avgPointerSpeed; }
    public void setAvgPointerSpeed(double avgPointerSpeed) { this.avgPointerSpeed = avgPointerSpeed; }

    public boolean isUsedKeyboard() { return usedKeyboard; }
    public void setUsedKeyboard(boolean usedKeyboard) { this.usedKeyboard = usedKeyboard; }

    public long getSessionDuration() { return sessionDuration; }
    public void setSessionDuration(long sessionDuration) { this.sessionDuration = sessionDuration; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

    public String getUserAgent() { return userAgent; }
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }

    // --- toString() for debugging ---
    @Override
    public String toString() {
        return "UserFeatures{" +
                "id=" + id +
                ", numPointerMoves=" + numPointerMoves +
                ", avgPointerSpeed=" + avgPointerSpeed +
                ", usedKeyboard=" + usedKeyboard +
                ", sessionDuration=" + sessionDuration +
                ", createdAt=" + createdAt +
                ", ipAddress='" + ipAddress + '\'' +
                ", userAgent='" + userAgent + '\'' +
                '}';
    }
}