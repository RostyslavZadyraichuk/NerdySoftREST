package com.nerdysoft.rest.repository;

import com.nerdysoft.rest.entity.Member;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.TransactionSystemException;

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
        nullName.setMembershipDate(member.getMembershipDate());
        assertThrows(ConstraintViolationException.class, () -> memberRepo.save(nullName));

        Member blankName = new Member();
        blankName.setName("");
        blankName.setMembershipDate(member.getMembershipDate());
        assertThrows(ConstraintViolationException.class, () -> memberRepo.save(blankName));
        blankName.setName("      ");
        assertThrows(ConstraintViolationException.class, () -> memberRepo.save(blankName));

        Member correctName = new Member();
        correctName.setName("Joes");
        correctName.setMembershipDate(member.getMembershipDate());
        assertDoesNotThrow(() -> memberRepo.save(correctName));
        correctName.setName("");
        assertThrows(TransactionSystemException.class, () -> memberRepo.save(correctName));
        Optional<Member> optional = memberRepo.findById(correctName.getId());
        assertTrue(optional.isPresent());
    }

    @Test
    void membershipDateValidation() {
        Member nullDate = new Member();
        nullDate.setName("User");
        assertThrows(ConstraintViolationException.class, () -> memberRepo.save(nullDate));

        Member correctDate = new Member();
        correctDate.setName("User");
        correctDate.setMembershipDate(LocalDate.now());
        assertDoesNotThrow(() -> memberRepo.save(correctDate));
        correctDate.setMembershipDate(null);
        assertThrows(TransactionSystemException.class, () -> memberRepo.save(correctDate));
        Optional<Member> optional = memberRepo.findById(correctDate.getId());
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
