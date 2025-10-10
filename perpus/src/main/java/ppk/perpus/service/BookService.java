package ppk.perpus.service;

import ppk.perpus.dto.BookDto;
import java.util.List;
public interface BookService {
    public void createBook(BookDto bookDto);
    public List<BookDto> getBooks();
    public List<BookDto> searchBooks(String keyword);
}