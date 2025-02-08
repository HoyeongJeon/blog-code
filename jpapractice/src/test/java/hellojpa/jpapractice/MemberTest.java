package hellojpa.jpapractice;

import hellojpa.jpapractice.domain.member.entity.Member;
import hellojpa.jpapractice.domain.member.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class MemberTest {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("엔티티 ID가 NULL인 경우")
    public void 엔티티_ID가_NULL인_경우() {
        // given
        System.out.println("========== 엔티티 ID가 NULL인 경우 ==========");
        Member member = new Member(3L, "Alice", 30);

        // when & then
        System.out.println(member.getId());
        memberRepository.save(member);


        System.out.println("=========================================");
    }

    @Test
    @DisplayName("merge 쿼리 확인")
    @Transactional
    public void merge_쿼리_확인() {
        // given
        System.out.println("========== given ==========");
        Member member = new Member(3L, "Alice", 30);
        memberRepository.save(member);

        // when & then
        System.out.println("========== when ==========");
        Member findMember = em.find(Member.class, 3L);
        em.detach(findMember);

        System.out.println("========== select 쿼리는 언제나갈까? ==========");
        findMember.setAge(31);

        em.merge(findMember);

        System.out.println("===================================");
    }
}
