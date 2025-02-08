package blog.howsavework;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NullIdMemberRepository extends JpaRepository<NullIdMember, Long> {
}
