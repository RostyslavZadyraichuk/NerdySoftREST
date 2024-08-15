package com.nerdysoft.rest.service.mapper;

import com.nerdysoft.rest.dto.MemberDTO;
import com.nerdysoft.rest.entity.Member;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberMapper {

    Member toEntity(MemberDTO memberDTO);

    MemberDTO toDTO(Member member);

}
