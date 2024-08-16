package com.nerdysoft.rest.repository;

import com.nerdysoft.rest.entity.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class MemberRepositoryTest {

    private static Member member;

    private final MemberRepository memberRepo;

    @Autowired
    public MemberRepositoryTest(MemberRepository memberRepo) {
        this.memberRepo = memberRepo;
    }

    @BeforeEach
    void beforeEach() {
        member = new Member();
        member.setName("User");
        member.setMembershipDate(LocalDate.now());
        member = memberRepo.save(member);
    }

    @Test
    void nameValidation() {
        Member nullName = new Member();
        nullName.setMembershipDate(LocalDate.now());
        assertThrows(DataIntegrityViolationException.class, () -> memberRepo.save(nullName));

        Member notUniqueName = new Member();
        notUniqueName.setName(member.getName());
        notUniqueName.setMembershipDate(LocalDate.now());
        assertThrows(DataIntegrityViolationException.class, () -> memberRepo.save(notUniqueName));

        Member correctName = new Member();
        correctName.setName("Unique");
        correctName.setMembershipDate(LocalDate.now());
        assertDoesNotThrow(() -> memberRepo.save(correctName));
        correctName.setName("Unique2");
        assertDoesNotThrow(() -> memberRepo.save(correctName));
        correctName.setName(member.getName());
        assertThrows(DataIntegrityViolationException.class, () -> memberRepo.save(correctName));
        correctName.setName(null);
        assertThrows(DataIntegrityViolationException.class, () -> memberRepo.save(correctName));
        Optional<Member> optional = memberRepo.findById(correctName.getId());
        assertTrue(optional.isPresent());
    }

    @Test
    void membershipDateValidation() {
        Member nullMembership = new Member();
        nullMembership.setName("Unique");
        nullMembership.setMembershipDate(null);
        assertThrows(DataIntegrityViolationException.class, () -> memberRepo.save(nullMembership));

        Member correctMembership = new Member();
        correctMembership.setName("Unique2");
        correctMembership.setMembershipDate(LocalDate.now());
        assertDoesNotThrow(() -> memberRepo.save(correctMembership));
        Optional<Member> optional = memberRepo.findById(correctMembership.getId());
        assertTrue(optional.isPresent());
    }

    @Test
    void createMember() {
        memberRepo.deleteAll();
        List<Member> members = memberRepo.findAll();
        assertEquals(0, members.size());

        member.setId(0);
        member = memberRepo.save(member);
        members = memberRepo.findAll();
        assertEquals(1, members.size());
        assertNotEquals(0, member.getId());

        member = memberRepo.findById(member.getId()).orElse(null);
        assertNotNull(member);
        assertEquals("User", member.getName());
        assertNotEquals(0, member.getId());
    }

    @Test
    void updateMember() {
        String newName = "Fill";
        int currentId = member.getId();

        member.setName(newName);
        member = memberRepo.save(member);
        assertNotNull(member);
        assertEquals(newName, member.getName());
        assertEquals(currentId, member.getId());
    }

    @Test
    void findMemberById() {
        Member found = memberRepo.findById(member.getId()).orElse(null);

        assertNotNull(member);
        assertNotNull(found);
        assertEquals(member.getId(), found.getId());
        assertEquals(member.getName(), found.getName());
        assertEquals(member.getMembershipDate(), found.getMembershipDate());
    }

    @Test
    void deleteMember() {
        List<Member> members = memberRepo.findAll();
        assertEquals(1, members.size());

        memberRepo.delete(member);
        members = memberRepo.findAll();
        assertEquals(0, members.size());
    }

    @AfterEach
    void afterEach() {
        memberRepo.deleteAll();
    }
}
