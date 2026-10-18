package com.example.til.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TilControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void 빈_목록_조회() throws Exception {
        mockMvc.perform(get("/api/tils"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void 작성_성공_201() throws Exception {
        String body = objectMapper.writeValueAsString(Map.of(
                "title", "오늘 배운 것",
                "content", "Spring Boot는 좋다",
                "tag", "Spring"
        ));

        mockMvc.perform(post("/api/tils")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("오늘 배운 것"))
                .andExpect(jsonPath("$.content").value("Spring Boot는 좋다"));
    }

    @Test
    void 상세_조회_성공() throws Exception {
        Long id = 작성하고_id_반환("제목", "상세내용", null);

        mockMvc.perform(get("/api/tils/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("상세내용"));
    }

    @Test
    void 없는_id_상세_조회_404() throws Exception {
        mockMvc.perform(get("/api/tils/99999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void 삭제_성공_204() throws Exception {
        Long id = 작성하고_id_반환("삭제할 제목", "삭제할 내용", null);

        mockMvc.perform(delete("/api/tils/" + id))
                .andExpect(status().isNoContent());
    }

    @Test
    void 없는_id_삭제_404() throws Exception {
        mockMvc.perform(delete("/api/tils/99999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void title_공백_작성_400() throws Exception {
        String body = objectMapper.writeValueAsString(Map.of(
                "title", "   ",
                "content", "내용"
        ));

        mockMvc.perform(post("/api/tils")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void title_101자_작성_400() throws Exception {
        String body = objectMapper.writeValueAsString(Map.of(
                "title", "a".repeat(101),
                "content", "내용"
        ));

        mockMvc.perform(post("/api/tils")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void content_공백_작성_400() throws Exception {
        String body = objectMapper.writeValueAsString(Map.of(
                "title", "제목",
                "content", ""
        ));

        mockMvc.perform(post("/api/tils")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void 잘못된_JSON_본문_400() throws Exception {
        mockMvc.perform(post("/api/tils")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("not json"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void 숫자_아닌_id_조회_400() throws Exception {
        mockMvc.perform(get("/api/tils/abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @SuppressWarnings("unchecked")
    private Long 작성하고_id_반환(String title, String content, String tag) throws Exception {
        Map<String, Object> payload = new HashMap<>();
        payload.put("title", title);
        payload.put("content", content);
        if (tag != null) payload.put("tag", tag);

        MvcResult result = mockMvc.perform(post("/api/tils")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andReturn();

        Map<String, Object> response = objectMapper.readValue(
                result.getResponse().getContentAsString(), Map.class);
        return ((Number) response.get("id")).longValue();
    }
}
