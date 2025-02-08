package hellojpa.jpapractice;

import hellojpa.jpapractice.domain.member.service.MemberService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class JpapracticeApplication {

    public static void main(String[] args) {
        SpringApplication.run(JpapracticeApplication.class, args);
    }

}

@Component
class StartupRunner implements ApplicationRunner {
    private final MemberService memberService;

    public StartupRunner(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public void run(ApplicationArguments args) {
        memberService.insertMembersForTest();

//        memberService.testAnnotation();
        memberService.testNoneAnnotation();
//        memberService.testJPA();
//        memberService.testJPQL();
//        memberService.testNativeQuery();
//        memberService.testFlush();
//        memberService.testHell();
    }
}
