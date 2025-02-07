package hellojpa.jpapractice.domain.member.repository;


import hellojpa.jpapractice.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query(value = "SELECT * FROM member WHERE id = :id", nativeQuery = true)
    Member findByIdWithNative(@Param("id") Long id);

    @Query("SELECT m FROM Member m WHERE m.id = :id")
    Member findByIdWithJPQL(@Param("id") Long id);
}
