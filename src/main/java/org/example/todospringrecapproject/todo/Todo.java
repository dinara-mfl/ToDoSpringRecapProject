package org.example.todospringrecapproject.todo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "todos")
public record Todo(
        @Id String id,
        String description,
        TodoStatus status
) {
}
