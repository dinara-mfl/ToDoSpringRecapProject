package org.example.todospringrecapproject.todo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TodoController.class)
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TodoRepository todoRepository;

    // GET - getAllTodos
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
                "Recap project",
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

    // POST - createTodo
    @Test
    void createTodo_ReturnsSavedTodo() throws Exception {
        Todo todoToSave = new Todo(
                null,
                "Recap project",
                TodoStatus.OPEN
        );

        Todo savedTodo = new Todo(
                "1",
                "Recap project",
                TodoStatus.OPEN
        );

        when(todoRepository.save(todoToSave)).thenReturn(savedTodo);

        mockMvc.perform(post("/api/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "description": "Recap project",
                              "status": "OPEN"
                            }
                            """))
                .andExpect(status().isCreated())
                .andExpect(content().json("""
                    {
                      "id": "1",
                      "description": "Recap project",
                      "status": "OPEN"
                    }
                    """));

        verify(todoRepository).save(todoToSave);
    }

    // GET - getTodoById
    @Test
    void getTodoById_ReturnsTodo_WhenIdExists() throws Exception {
        Todo todo = new Todo(
                "1",
                "Recap project",
                TodoStatus.OPEN
        );

        when(todoRepository.findById("1"))
                .thenReturn(Optional.of(todo));

        mockMvc.perform(get("/api/todo/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                    {
                      "id": "1",
                      "description": "Recap project",
                      "status": "OPEN"
                    }
                    """));

        verify(todoRepository).findById("1");
    }

    @Test
    void getTodoById_ReturnsNotFound_WhenIdDoesNotExist() throws Exception {
        when(todoRepository.findById("missing"))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/todo/missing"))
                .andExpect(status().isNotFound());

        verify(todoRepository).findById("missing");
    }

    // PUT - updateTodo
    @Test
    void updateTodo_ReturnsUpdatedTodo_WhenIdExists() throws Exception {
        Todo existingTodo = new Todo(
                "1",
                "Recap project",
                TodoStatus.OPEN
        );

        Todo updatedTodo = new Todo(
                "1",
                "Finish recap project",
                TodoStatus.IN_PROGRESS
        );

        when(todoRepository.findById("1"))
                .thenReturn(Optional.of(existingTodo));
        when(todoRepository.save(updatedTodo))
                .thenReturn(updatedTodo);

        mockMvc.perform(put("/api/todo/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "description": "Finish recap project",
                              "status": "IN_PROGRESS"
                            }
                            """))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                    {
                      "id": "1",
                      "description": "Finish recap project",
                      "status": "IN_PROGRESS"
                    }
                    """));

        verify(todoRepository).findById("1");
        verify(todoRepository).save(updatedTodo);
    }

    @Test
    void updateTodo_ReturnsNotFound_WhenIdDoesNotExist() throws Exception {
        when(todoRepository.findById("missing"))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/api/todo/missing")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "description": "Finish recap project",
                              "status": "IN_PROGRESS"
                            }
                            """))
                .andExpect(status().isNotFound());

        verify(todoRepository).findById("missing");
        verify(todoRepository, never()).save(any(Todo.class));
    }
}