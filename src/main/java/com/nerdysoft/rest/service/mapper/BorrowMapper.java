package com.nerdysoft.rest.service.mapper;

import com.nerdysoft.rest.dto.BorrowDTO;
import com.nerdysoft.rest.entity.Borrow;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BorrowMapper {

    Borrow toEntity(BorrowDTO borrowDTO);

    BorrowDTO toDTO(Borrow borrow);

}
