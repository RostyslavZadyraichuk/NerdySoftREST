package com.nerdysoft.rest.service.impl;

import com.nerdysoft.rest.dto.AuthorDTO;
import com.nerdysoft.rest.dto.BookDTO;
import com.nerdysoft.rest.entity.Author;
import com.nerdysoft.rest.error.DatabaseOperationException;
import com.nerdysoft.rest.repository.AuthorRepository;
import com.nerdysoft.rest.service.AuthorService;
import com.nerdysoft.rest.service.BookService;
import com.nerdysoft.rest.service.mapper.AuthorMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorServiceImpl implements AuthorService {

    private AuthorRepository authorRepo;
    private BookService bookService;
    private AuthorMapper authorMapper;

    public AuthorServiceImpl(AuthorRepository authorRepo,
                             @Lazy BookService bookService,
                             AuthorMapper authorMapper) {
        this.authorRepo = authorRepo;
        this.bookService = bookService;
        this.authorMapper = authorMapper;
    }

    @Override
    public AuthorDTO create(AuthorDTO author) {
        Author entity = authorMapper.toEntity(author);
        return authorMapper.toDTO(authorRepo.save(entity));
    }

    @Override
    public Optional<AuthorDTO> findByName(String name) {
        Optional<Author> optional = authorRepo.findByName(name);
        if (optional.isPresent()) {
            Author entity = optional.get();
            return Optional.of(authorMapper.toDTO(entity));
        }
        return Optional.empty();
    }

    @Override
    public void delete(AuthorDTO author) {
        List<BookDTO> books = bookService.findByAuthor(author);
        if (!books.isEmpty()) {
            throw new DatabaseOperationException(String.format("Author %s is used in %d books",
                    author.getName(), books.size()));
        }
        authorRepo.delete(authorMapper.toEntity(author));
    }

    @Override
    public List<AuthorDTO> findAll() {
        return authorRepo.findAll().stream()
                .map(authorMapper::toDTO)
                .toList();
    }

    @Override
    public void deleteAll() {
        List<BookDTO> books = bookService.findAll();
        if (!books.isEmpty()) {
            throw new DatabaseOperationException("Book's table is not empty");
        }
    }
}
