package com.example.service.impl;

import com.example.mapper.ScheduleMapper;
import com.example.model.Schedule;
import com.example.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // 引入事务注解

import java.util.List;

@Service // 声明为Spring Service Bean
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleMapper scheduleMapper;

    @Autowired
    public ScheduleServiceImpl(ScheduleMapper scheduleMapper) {
        this.scheduleMapper = scheduleMapper;
    }

    @Override
    @Transactional(readOnly = true) // 只读事务
    public List<Schedule> getSchedulesByUserId(int userId) {
        return scheduleMapper.findSchedulesByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Schedule getScheduleByIdAndUserId(int scheduleId, int userId) {
        return scheduleMapper.findScheduleByIdAndUserId(scheduleId, userId);
    }

    @Override
    @Transactional // 写操作事务
    public boolean addSchedule(Schedule schedule) {
        // 可以在此添加业务逻辑，例如检查时间冲突等
        return scheduleMapper.insertSchedule(schedule) > 0;
    }

    @Override
    @Transactional
    public boolean updateSchedule(Schedule schedule) {
        // 重要的业务逻辑：确保操作者只能修改自己的日程
        // Controller层应该已经填充了正确的userId到schedule对象中
        // Mapper层的SQL语句中也包含了 AND user_id = #{userId}
        Schedule existing = scheduleMapper.findScheduleByIdAndUserId(schedule.getScheduleId(), schedule.getUserId());
        if (existing == null) {
            return false; // 日程不存在或不属于该用户
        }
        return scheduleMapper.updateSchedule(schedule) > 0;
    }

    @Override
    @Transactional
    public boolean deleteSchedule(int scheduleId, int userId) {
        // 同样，确保操作者只能删除自己的日程
        Schedule existing = scheduleMapper.findScheduleByIdAndUserId(scheduleId, userId);
        if (existing == null) {
            return false; // 日程不存在或不属于该用户
        }
        return scheduleMapper.deleteSchedule(scheduleId, userId) > 0;
    }
}