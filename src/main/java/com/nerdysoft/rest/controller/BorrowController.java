package com.nerdysoft.rest.controller;

import com.nerdysoft.rest.dto.BookDTO;
import com.nerdysoft.rest.dto.BorrowDTO;
import com.nerdysoft.rest.dto.MemberDTO;
import com.nerdysoft.rest.service.BorrowService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class BorrowController {

    private BorrowService borrowService;

    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    @GetMapping(path = "/borrows")
    public String borrows() {
        return "borrows";
    }

    @GetMapping(path = "/borrows/list")
    @ResponseBody
    public List<BorrowDTO> borrowsList() {
        return borrowService.findAll();
    }

    @PostMapping("/borrow/create")
    public ResponseEntity<String> createBorrow(@RequestBody BorrowDTO borrow,
                                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getFieldError().getDefaultMessage());
        }

        borrowService.create(borrow);
        return ResponseEntity.ok("Borrow created successfully");
    }

    @GetMapping("/borrow/find")
    @ResponseBody
    public List<BorrowDTO> findBooks(@RequestParam String bookTitle, @RequestParam String memberName) {
        BookDTO book = new BookDTO();
        book.setTitle(bookTitle);
        MemberDTO member = new MemberDTO();
        member.setName(memberName);

        if (!bookTitle.isEmpty() && !memberName.isEmpty()) {
            Optional<BorrowDTO> optional = borrowService.findFirstByMemberAndBook(member, book);
            if (optional.isPresent()) {
                return List.of(optional.get());
            }
        } else if (!bookTitle.isEmpty()) {
            return borrowService.findByBook(book);
        } else if (!memberName.isEmpty()) {
            return borrowService.findByMember(member);
        }
        return new ArrayList<>();
    }

    @PostMapping("/borrow/delete")
    public ResponseEntity<String> deleteBook(@RequestBody BorrowDTO borrow,
                                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getFieldError().getDefaultMessage());
        }

        borrowService.delete(borrow);
        return ResponseEntity.ok("Borrow deleted successfully");
    }

}
