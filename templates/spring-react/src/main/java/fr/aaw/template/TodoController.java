package fr.aaw.template;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/todos")
public class TodoController {
    private final TodoRepository repository;

    public TodoController(TodoRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Todo> list() {
        return repository.findAllByOrderByCreatedAtDesc();
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody NewTodo input) {
        String title = input.title() == null ? "" : input.title().strip();
        if (title.isEmpty() || title.length() > 255) {
            return ResponseEntity.badRequest().body(Map.of("error", "Titre de 1 à 255 caractères requis"));
        }
        if (input.priority() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Priorité obligatoire"));
        }
        Todo todo = repository.save(new Todo(title, input.priority()));
        return ResponseEntity.status(201).body(todo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        return repository.removeById(id) == 0
            ? ResponseEntity.notFound().build()
            : ResponseEntity.noContent().build();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> invalidBody() {
        return ResponseEntity.badRequest().body(Map.of("error", "JSON ou priorité invalide"));
    }

    public record NewTodo(String title, Todo.Priority priority) {}
}
