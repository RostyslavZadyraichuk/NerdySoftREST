package com.nerdysoft.rest.service;

import com.nerdysoft.rest.dto.AuthorDTO;

import java.util.List;
import java.util.Optional;

public interface AuthorService {

    AuthorDTO create(AuthorDTO member);

    Optional<AuthorDTO> findByName(String name);

    void delete(AuthorDTO member);

    List<AuthorDTO> findAll();

    void deleteAll();

}
