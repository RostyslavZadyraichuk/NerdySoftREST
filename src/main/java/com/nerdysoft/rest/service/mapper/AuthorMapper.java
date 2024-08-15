package com.nerdysoft.rest.service.mapper;

import com.nerdysoft.rest.dto.AuthorDTO;
import com.nerdysoft.rest.entity.Author;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    Author toEntity(AuthorDTO authorDTO);

    AuthorDTO toDTO(Author author);

}
