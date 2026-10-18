package com.example.til.service;

import com.example.til.domain.Til;
import com.example.til.dto.TilCreateRequest;
import com.example.til.dto.TilDetail;
import com.example.til.dto.TilListItem;
import com.example.til.exception.TilNotFoundException;
import com.example.til.mapper.TilMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TilServiceTest {

    @Mock
    TilMapper tilMapper;

    @InjectMocks
    TilService tilService;

    @Test
    void findAll_목록_반환() {
        Til til = makeTil(1L, "제목", "내용", "AI");
        when(tilMapper.findAll()).thenReturn(List.of(til));

        List<TilListItem> result = tilService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("제목");
    }

    @Test
    void findById_존재하는_id_TilDetail_반환() {
        Til til = makeTil(1L, "제목", "내용", null);
        when(tilMapper.findById(1L)).thenReturn(Optional.of(til));

        TilDetail result = tilService.findById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getContent()).isEqualTo("내용");
    }

    @Test
    void findById_없는_id_TilNotFoundException() {
        when(tilMapper.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tilService.findById(999L))
                .isInstanceOf(TilNotFoundException.class)
                .hasMessageContaining("999");
    }

    @Test
    void create_mapper_insert_호출_후_TilDetail_반환() {
        TilCreateRequest req = new TilCreateRequest();
        req.setTitle("새 제목");
        req.setContent("새 내용");
        req.setTag("Java");

        Til saved = makeTil(10L, "새 제목", "새 내용", "Java");

        doAnswer(invocation -> {
            Til til = invocation.getArgument(0);
            til.setId(10L);
            return null;
        }).when(tilMapper).insert(any(Til.class));
        when(tilMapper.findById(10L)).thenReturn(Optional.of(saved));

        TilDetail result = tilService.create(req);

        verify(tilMapper).insert(any(Til.class));
        assertThat(result.getId()).isEqualTo(10L);
        assertThat(result.getTitle()).isEqualTo("새 제목");
        assertThat(result.getCreatedAt()).isNotNull();
    }

    @Test
    void delete_성공() {
        when(tilMapper.deleteById(1L)).thenReturn(1);

        tilService.delete(1L);

        verify(tilMapper).deleteById(1L);
    }

    @Test
    void delete_없는_id_TilNotFoundException() {
        when(tilMapper.deleteById(999L)).thenReturn(0);

        assertThatThrownBy(() -> tilService.delete(999L))
                .isInstanceOf(TilNotFoundException.class)
                .hasMessageContaining("999");
    }

    private Til makeTil(Long id, String title, String content, String tag) {
        Til til = new Til();
        til.setId(id);
        til.setTitle(title);
        til.setContent(content);
        til.setTag(tag);
        til.setCreatedAt(LocalDateTime.now());
        return til;
    }
}
