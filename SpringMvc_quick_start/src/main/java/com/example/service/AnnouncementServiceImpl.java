package com.example.service;

import com.example.mapper.AnnouncementMapper;
import com.example.model.Announcement;
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