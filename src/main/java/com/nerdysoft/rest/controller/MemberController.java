package com.nerdysoft.rest.controller;

import com.nerdysoft.rest.dto.MemberDTO;
import com.nerdysoft.rest.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class MemberController {

    private MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping(path = "/members")
    public String members() {
        return "members";
    }

    @GetMapping(path = "/members/list")
    @ResponseBody
    public List<MemberDTO> membersList() {
        return memberService.findAll();
    }

    @PostMapping("/member/create")
    public ResponseEntity<String> createMember(@RequestBody MemberDTO member,
                                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getFieldError().getDefaultMessage());
        }

        memberService.create(member);
        return ResponseEntity.ok("Member created successfully");
    }

    @PostMapping("/member/update")
    public ResponseEntity<String> updateMember(@RequestBody MemberDTO member,
                                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getFieldError().getDefaultMessage());
        }

        memberService.update(member);
        return ResponseEntity.ok("Member updated successfully");
    }

    @GetMapping("/member/findByName")
    @ResponseBody
    public ResponseEntity<MemberDTO> findMembers(@RequestParam String name) {
        Optional<MemberDTO> member = memberService.findByName(name);
        if (member.isPresent()) {
            return ResponseEntity.ok(member.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/member/delete")
    public ResponseEntity<String> deleteMember(@RequestBody MemberDTO member,
                                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getFieldError().getDefaultMessage());
        }

        memberService.delete(member);
        return ResponseEntity.ok("Member deleted successfully");
    }

}
