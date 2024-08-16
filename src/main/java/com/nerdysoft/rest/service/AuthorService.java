package com.nerdysoft.rest.service;

import com.nerdysoft.rest.dto.AuthorDTO;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

public interface AuthorService {

    AuthorDTO create(@Valid AuthorDTO member);

    Optional<AuthorDTO> findByName(String name);

    void delete(AuthorDTO member);

    List<AuthorDTO> findAll();

    void deleteAll();

}
