package com.nerdysoft.rest.service.impl;

import com.nerdysoft.rest.dto.AuthorDTO;
import com.nerdysoft.rest.dto.BookDTO;
import com.nerdysoft.rest.dto.BorrowDTO;
import com.nerdysoft.rest.entity.Book;
import com.nerdysoft.rest.error.DatabaseOperationException;
import com.nerdysoft.rest.repository.BookRepository;
import com.nerdysoft.rest.service.AuthorService;
import com.nerdysoft.rest.service.BookService;
import com.nerdysoft.rest.service.BorrowService;
import com.nerdysoft.rest.service.mapper.AuthorMapper;
import com.nerdysoft.rest.service.mapper.BookMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    private BookRepository bookRepo;
    private BorrowService borrowService;
    private AuthorService authorService;
    private BookMapper bookMapper;
    private AuthorMapper authorMapper;

    public BookServiceImpl(BookRepository bookRepo,
                           BorrowService borrowService,
                           @Lazy AuthorService authorService,
                           BookMapper bookMapper,
                           AuthorMapper authorMapper) {
        this.bookRepo = bookRepo;
        this.borrowService = borrowService;
        this.authorService = authorService;
        this.bookMapper = bookMapper;
        this.authorMapper = authorMapper;
    }

    @Override
    public BookDTO create(BookDTO book) {
        Optional<AuthorDTO> optional = authorService.findByName(book.getAuthor().getName());
        if (optional.isPresent()) {
            Optional<Book> found = bookRepo.findByTitleAndAuthorName(book.getTitle(),
                    book.getAuthor().getName());
            if (found.isPresent()) {
                book = bookMapper.toDTO(found.get());
                book.setAmount(book.getAmount() + 1);
            }
        } else {
            book.setAuthor(authorService.create(book.getAuthor()));
        }

        Book bookEntity = bookMapper.toEntity(book);
        return bookMapper.toDTO(bookRepo.save(bookEntity));
    }

    @Override
    public BookDTO update(BookDTO book) {
        Optional<AuthorDTO> optional = authorService.findByName(book.getAuthor().getName());
        if (optional.isPresent()) {
            book.setAuthor(optional.get());
        } else {
            book.setAuthor(authorService.create(book.getAuthor()));
        }
        Book entity = bookMapper.toEntity(book);
        return bookMapper.toDTO(bookRepo.save(entity));
    }

    @Override
    public Optional<BookDTO> findById(int id) {
        Optional<Book> found = bookRepo.findById(id);
        if (found.isPresent()) {
            return Optional.of(bookMapper.toDTO(found.get()));
        }
        return Optional.empty();
    }

    @Override
    public void delete(BookDTO book) throws DatabaseOperationException {
        List<BorrowDTO> borrowed = borrowService.findByBook(book);
        if (!borrowed.isEmpty()) {
            throw new DatabaseOperationException(String.format("Book %s is borrowed %d times",
                    book.getTitle(), borrowed.size()));
        }
        bookRepo.delete(bookMapper.toEntity(book));
    }

    @Override
    public List<BookDTO> findAll() {
        return bookRepo.findAll().stream()
                .map(bookMapper::toDTO)
                .toList();
    }

    @Override
    public List<BookDTO> findByAuthor(AuthorDTO author) {
        List<Book> books = bookRepo.findByAuthorName(author.getName());
        return books.stream()
                .map(bookMapper::toDTO)
                .toList();
    }

    @Override
    public List<BookDTO> findByTitle(String title) {
        List<Book> books = bookRepo.findByTitle(title);
        return books.stream()
                .map(bookMapper::toDTO)
                .toList();
    }

    @Override
    public Optional<BookDTO> findByTitleAndAuthor(String title, AuthorDTO author) {
        Optional<Book> found = bookRepo.findByTitleAndAuthorName(title, author.getName());
        if (found.isPresent()) {
            return Optional.of(bookMapper.toDTO(found.get()));
        }
        return Optional.empty();
    }

    @Override
    public List<BookDTO> findAllDistinct() {
        return bookRepo.findAllDistinctBooks().stream()
                .map(bookMapper::toDTO)
                .toList();
    }

    @Override
    public List<BookDTO> findAllBorrowedDistinct() {
        return bookRepo.findAllDistinctBorrowedBooks().stream()
                .map(bookMapper::toDTO)
                .toList();
    }
}
