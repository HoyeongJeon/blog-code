package blog.howsavework;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class SaveTest {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    NullIdMemberRepository nullIdMemberRepository;

    @Autowired
    EntityManager em;


    @Test
    @DisplayName("엔티티 ID가 NULL인 경우")
    public void 엔티티_ID가_NULL인_경우() {
        // given
        System.out.println("========== 엔티티 ID가 NULL인 경우 ==========");
        NullIdMember member = new NullIdMember("Alice", 30);

        // when & then
        nullIdMemberRepository.save(member);

        System.out.println("=========================================");
    }

    @Test
    @DisplayName("엔티티 ID가 존재하는 경우")
    public void 엔티티_ID가_존재하는_경우() {
        // given
        System.out.println("========== 엔티티 ID가 존재하는 경우 ==========");
        Member member = new Member(1L, "Alice", 30);

        // when & then
        memberRepository.save(member);

        System.out.println("=========================================");
    }

    @DisplayName("merge 쿼리 확인")
    @Nested
    class MergeTest {
        @Test
        @DisplayName("merge는 객체를 복사한다")
        @Transactional
        public void merge는_객체를_복사한다() {
            // given
            System.out.println("========== merge ==========");
            Member member = new Member(1L, "Alice", 30);
            em.merge(member);
//        // when & then
            Member detachedMember = em.find(Member.class, 1L);
            em.detach(detachedMember);
            detachedMember.setAge(40);
            Member merge = em.merge(detachedMember);

            System.out.println("둘이 같을까? " + (detachedMember == merge));
            System.out.println("=========================================");
        }
    }
}
