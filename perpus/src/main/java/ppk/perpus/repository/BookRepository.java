package ppk.perpus.repository;


import ppk.perpus.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long>{
//    query untuk mencari buku
    List<Book> findByTitleContainingIgnoreCase(String keyword);
}