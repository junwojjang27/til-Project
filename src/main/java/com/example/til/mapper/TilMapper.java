package com.example.til.mapper;

import com.example.til.domain.Til;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface TilMapper {

    List<Til> findAll();

    Optional<Til> findById(Long id);

    void insert(Til til);

    int deleteById(Long id);
}
