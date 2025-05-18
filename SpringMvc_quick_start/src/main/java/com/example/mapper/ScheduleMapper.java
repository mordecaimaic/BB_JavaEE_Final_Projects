package com.example.mapper;

import com.example.model.Schedule;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface ScheduleMapper {
    List<Schedule> findSchedulesByUserId(@Param("userId") int userId);
    Schedule findScheduleByIdAndUserId(@Param("scheduleId") int scheduleId, @Param("userId") int userId);
    int insertSchedule(Schedule schedule);
    int updateSchedule(Schedule schedule);
    int deleteSchedule(@Param("scheduleId") int scheduleId, @Param("userId") int userId);
}