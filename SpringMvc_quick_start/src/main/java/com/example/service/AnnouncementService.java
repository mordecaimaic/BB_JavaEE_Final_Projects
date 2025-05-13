package com.example.service;

import com.example.model.Announcement;
import java.util.List;

public interface AnnouncementService {
    List<Announcement> listForUser(String department);
}