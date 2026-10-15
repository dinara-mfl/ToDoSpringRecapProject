package org.example.todospringrecapproject.todo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TodoController.class)
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TodoRepository todoRepository;

    @Test
    void getAllTodos_ReturnsEmptyList_WhenNoTodosExist() throws Exception {
        when(todoRepository.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/todo"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(todoRepository).findAll();
    }

    @Test
    void getAllTodos_ReturnsExistingTodos() throws Exception {
        Todo todo = new Todo(
                "1",
                "Spring Boot lernen",
                TodoStatus.OPEN
        );

        when(todoRepository.findAll()).thenReturn(List.of(todo));

        mockMvc.perform(get("/api/todo"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [
                          {
                            "id": "1",
                            "description": "Recap project",
                            "status": "OPEN"
                          }
                        ]
                        """));

        verify(todoRepository).findAll();
    }
}