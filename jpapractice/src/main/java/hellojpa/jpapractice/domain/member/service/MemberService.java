package hellojpa.jpapractice.domain.member.service;

import hellojpa.jpapractice.domain.member.entity.Member;
import hellojpa.jpapractice.domain.member.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public MemberService(MemberRepository memberRepository, EntityManager entityManager) {
        this.memberRepository = memberRepository;
        this.entityManager = entityManager;
    }

    @Transactional
    public void insertMembersForTest() {
        memberRepository.save(new Member(1L, "Alice", 30));
        memberRepository.save(new Member(2L,  "Bob", 25));
    }

    @Transactional
    public void testAnnotation() {
        System.out.println("// @Transactional + JPA");
        Member member = memberRepository.findById(1L).get(); // select , 1차 캐시 저장

        member.setAge(35); // 1차 캐시 값이 변경됨.

        memberRepository.save(member);
        // Transaction이 끝나면서 쓰기 지연 저장소의 쿼리가 나감!
    }

    public void testNoneAnnotation() {
        System.out.println("// JPA만 사용하기(@Transactional 이 없는 경우)");
        Member member = memberRepository.findById(1L).get(); // select , @Transactional이 없으므로 1차 캐시 저장 X , 영속성 컨텍스트가 생성되지 않음, 준영속 상태이다.

        member.setAge(35); // 준영속 상태이므로 변경 감지가 일어나지 않음. 메모리 상의 age 값만 변경됨.

        memberRepository.save(member);
        System.out.println("result: " + member.getAge());
    }

    @Transactional
    public void testJPA() {
        System.out.println("// testJPA start");
        Member member1 = memberRepository.findById(1L).get();
        Member member2 = memberRepository.findById(1L).get();

        System.out.println("result" + (member1 == member2));
    }

    @Transactional
    public void testJPQL() {
        System.out.println("// testJPQL start");
        Member member1 = memberRepository.findById(1L).get();
        Member member2 = memberRepository.findByIdWithJPQL(1L);

        System.out.println("result" + (member1 == member2));
    }

    @Transactional
    public void testNativeQuery() {
        System.out.println("// testJPQL start");
        Member member1 = memberRepository.findById(1L).get();
        Member member2 = memberRepository.findByIdWithNative(1L);

        System.out.println("result" + (member1 == member2));
    }

    @Transactional
    public void testFlush() {
        System.out.println("// testFlush start");
        Member member = memberRepository.findById(1L).get();
        member.setAge(35);
        entityManager.flush();
        System.out.println("result" + member.getAge());
        memberRepository.save(member);
    }

    @Transactional
    public void testHell() {
        Member alice = memberRepository.findById(1L).get();
        Member bob = memberRepository.findById(2L).get();

        alice.setAge(40);
        memberRepository.delete(bob);

        Member newAlice = memberRepository.findByIdWithJPQL(1L);
        Member newBob = new Member(2L, "Bob", 99);
        entityManager.persist(newBob);

        entityManager.flush();
        Member nativeBob = memberRepository.findByIdWithNative(2L);

        memberRepository.save(newAlice);
        memberRepository.save(nativeBob);
    }

}
