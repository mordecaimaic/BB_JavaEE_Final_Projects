package com.example.model;

import lombok.Data;
import java.sql.Timestamp;

@Data
public class PersistentLogin {
    private String series;
    private String username;
    private String token;
    private Timestamp lastUsed;
}