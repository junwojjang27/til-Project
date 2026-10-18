package com.example.til.service;

import com.example.til.domain.Til;
import com.example.til.dto.TilCreateRequest;
import com.example.til.dto.TilDetail;
import com.example.til.dto.TilListItem;
import com.example.til.exception.TilNotFoundException;
import com.example.til.mapper.TilMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TilService {

    private final TilMapper tilMapper;

    public TilService(TilMapper tilMapper) {
        this.tilMapper = tilMapper;
    }

    public List<TilListItem> findAll() {
        return tilMapper.findAll().stream()
                .map(TilListItem::from)
                .toList();
    }

    public TilDetail findById(Long id) {
        Til til = tilMapper.findById(id)
                .orElseThrow(() -> new TilNotFoundException(id));
        return TilDetail.from(til);
    }

    public TilDetail create(TilCreateRequest req) {
        Til til = new Til();
        til.setTitle(req.getTitle());
        til.setContent(req.getContent());
        til.setTag(req.getTag());
        tilMapper.insert(til);
        // DB가 설정한 CREATED_AT을 포함해 반환하기 위해 재조회
        return findById(til.getId());
    }

    public void delete(Long id) {
        int rows = tilMapper.deleteById(id);
        if (rows == 0) {
            throw new TilNotFoundException(id);
        }
    }
}
