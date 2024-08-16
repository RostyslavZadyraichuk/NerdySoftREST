package com.nerdysoft.rest.repository;

import com.nerdysoft.rest.entity.Borrow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowRepository extends JpaRepository<Borrow, Integer> {

    List<Borrow> findByBookTitle(String book);

    List<Borrow> findByMemberName(String member);

    Optional<Borrow> findFirstByMemberNameAndBookTitle(String member, String book);

}
