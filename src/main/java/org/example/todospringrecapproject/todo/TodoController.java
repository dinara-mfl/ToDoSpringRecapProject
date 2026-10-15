package org.example.todospringrecapproject.todo;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/todo")
public class TodoController {

    private final TodoRepository todoRepository;

    public TodoController(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @GetMapping
    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Todo createTodo(@RequestBody TodoDto todoDto) {
        Todo todo = new Todo(
                null,
                todoDto.description(),
                todoDto.status()
        );

        return todoRepository.save(todo);
    }

    @GetMapping("/{id}")
    public Todo getTodoById(@PathVariable String id) {
        return todoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Todo not found: " + id
                ));
    }

    @PutMapping("/{id}")
    public Todo updateTodo(
            @PathVariable String id,
            @RequestBody TodoDto todoDto
    ) {
        Todo existingTodo = todoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Todo not found: " + id
                ));

        Todo updatedTodo = new Todo(
                existingTodo.id(),
                todoDto.description(),
                todoDto.status()
        );

        return todoRepository.save(updatedTodo);
    }
}
