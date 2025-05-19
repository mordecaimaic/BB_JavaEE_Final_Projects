package com.example.service.impl;

import com.example.mapper.AnnouncementMapper;
import com.example.model.Announcement;
import com.example.service.AnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementMapper announcementMapper;

    @Override
    @Transactional(readOnly = true)
    public List<Announcement> listForUser(String department) {
        return announcementMapper.selectForUser(department);
    }
}