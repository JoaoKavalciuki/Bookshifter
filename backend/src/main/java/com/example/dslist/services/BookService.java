package com.example.dslist.services;

import com.example.dslist.bookapi.google.FullRequestWrapper;
import com.example.dslist.bookapi.openlibrary.FullRequestOpenLibrary;
import com.example.dslist.dto.BookDTO;
import com.example.dslist.entities.Book;
import com.example.dslist.repositories.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class BookService implements com.example.dslist.services.interfaces.BookService {
    @Autowired
    private BookRepository repository;
    @Autowired
    private RestTemplate restTemplate;

    private Logger logger = LoggerFactory.getLogger(BookService.class);
    @Override
    public BookDTO saveBookByIsbn(Long isbn) {
        String bookUrl = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn;
        String largeCoverUrl = "https://covers.openlibrary.org/b/isbn/" + isbn + "-L.jpg";
        String mediumCoverURL = "https://covers.openlibrary.org/b/isbn/" + isbn + "-M.jpg";

        ResponseEntity<FullRequestWrapper> response = restTemplate.getForEntity(bookUrl, FullRequestWrapper.class);

        String url = "https://openlibrary.org/search.json?q=" + isbn;

        ResponseEntity<FullRequestOpenLibrary> extraInfoResponse = restTemplate.getForEntity(url, FullRequestOpenLibrary.class);

        Book newBook = new Book(
                response.getBody().getItems()[0].getVolumeInfo().getTitle(),
                List.of(response.getBody().getItems()[0].getVolumeInfo().getAuthors()),
                response.getBody().getItems()[0].getVolumeInfo().getDescription(),
                extraInfoResponse.getBody().getDocs()[0].getPublisher().get(0),
                extraInfoResponse.getBody().getDocs()[0].getPublishYear().get(0),
                response.getBody().getItems()[0].getVolumeInfo().getPageCount(),
                largeCoverUrl,
                mediumCoverURL
        );



        repository.save(newBook);
        return new BookDTO(newBook);
    }

    @Override
    public List<BookDTO> findAllBooks(){
        var result = repository.findAll();

        List<BookDTO> books = result.stream().map(BookDTO::new).toList();

        return  books;
    }

    @Override
    public BookDTO findById(Long id){
        var result = repository.findById(id);
        if(result.isEmpty()){
            throw new RuntimeException("Livro não existe");
        }
        Book bookFound = result.get();

        BookDTO dto = new BookDTO(bookFound);

        return dto;
    }

    @Override
    public void deleteBook(Long id){
        var result = repository.findById(id);

        if(result.isEmpty()){
            throw new RuntimeException("Livro não encontrado");
        }

        Book book = result.get();
        repository.delete(book);
    }
}