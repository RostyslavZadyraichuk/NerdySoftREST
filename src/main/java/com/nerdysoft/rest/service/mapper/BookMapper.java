package com.nerdysoft.rest.service.mapper;

import com.nerdysoft.rest.dto.BookDTO;
import com.nerdysoft.rest.entity.Book;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookMapper {

    Book toEntity(BookDTO bookDTO);

    BookDTO toDTO(Book book);

}
