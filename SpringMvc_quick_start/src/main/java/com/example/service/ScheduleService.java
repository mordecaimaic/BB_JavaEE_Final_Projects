package com.example.service;

import com.example.model.Schedule;
import java.util.List;

public interface ScheduleService {
    List<Schedule> getSchedulesByUserId(int userId);
    Schedule getScheduleByIdAndUserId(int scheduleId, int userId);
    boolean addSchedule(Schedule schedule);
    boolean updateSchedule(Schedule schedule); // 业务层应确保userId匹配
    boolean deleteSchedule(int scheduleId, int userId);
}