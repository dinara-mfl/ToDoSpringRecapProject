package org.example.todospringrecapproject.todo;

public record TodoDto(
        String description,
        TodoStatus status
) { }