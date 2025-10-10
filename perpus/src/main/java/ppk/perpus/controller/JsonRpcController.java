package ppk.perpus.controller;

import com.fasterxml.jackson.databind.JsonNode;
import ppk.perpus.dto.BookDto;
import ppk.perpus.rpc.JsonRpcRequest;
import ppk.perpus.rpc.JsonRpcResponse;
import ppk.perpus.service.BookService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JsonRpcController {

    @Autowired
    private BookService bookService;

    @PostMapping(value = "/jsonrpc", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> handleJsonRpcRequest(@RequestBody JsonRpcRequest request) {
        try {
            String method = request.getMethod();
            JsonNode params = request.getParams();
            System.out.println("Method: "+ method);

            switch (method) {
                case "createBook" -> {
                    String title = params.get("title").asText();
                    String author = params.get("author").asText();
                    String description = params.get("description").asText();
                    BookDto book = BookDto.builder()
                            .title(title)
                            .description(description)
                            .author(author)
                            .build();
                    bookService.createBook(book);

                    return ResponseEntity.ok(new JsonRpcResponse("created", request.getId()));
                }
                case "getBooks" -> {
                    List<BookDto> books = bookService.getBooks();
                    return ResponseEntity.ok(new JsonRpcResponse(books, request.getId()));
                }
                case "searchBooks" -> {
                    String keyword = params.get("keyword").asText();
                    List<BookDto> searchResults = bookService.searchBooks(keyword);
                    return ResponseEntity.ok(new JsonRpcResponse(searchResults, request.getId()));
                }
                default -> {
                    return ResponseEntity.badRequest().build();
                }
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}