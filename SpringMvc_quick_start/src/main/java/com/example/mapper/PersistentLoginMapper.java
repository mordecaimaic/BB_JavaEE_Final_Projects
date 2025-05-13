package com.example.mapper;

import com.example.model.PersistentLogin;
import org.apache.ibatis.annotations.Param;

public interface PersistentLoginMapper {

    void save(PersistentLogin token);

    PersistentLogin findBySeries(String series);

    void updateToken(@Param("series") String series,
                     @Param("token") String token,
                     @Param("lastUsed") java.sql.Timestamp lastUsed);

    void deleteByUsername(String username);
}