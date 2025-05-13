package com.example.mapper;

import com.example.model.Announcement;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface AnnouncementMapper {
    List<Announcement> selectForUser(@Param("department") String department);
}