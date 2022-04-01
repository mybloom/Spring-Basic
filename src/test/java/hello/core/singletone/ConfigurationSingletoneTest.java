package hello.core.singletone;

import hello.core.AppConfig;
import hello.core.member.MemberRepository;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import hello.core.order.OrderServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

public class ConfigurationSingletoneTest {

	@Test
	void configurationTest() {
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);

		MemberServiceImpl memberService = applicationContext.getBean("memberService" , MemberServiceImpl.class);
		OrderServiceImpl orderService = applicationContext.getBean("orderService", OrderServiceImpl.class);
		MemberRepository memberRepository = applicationContext.getBean("memberRepository", MemberRepository.class);

		MemberRepository memberRepository1 = memberService.getMemberRepository();
		MemberRepository memberRepository2 = orderService.getMemberRepository();

		System.out.println("memberService -> memberResposotiry : " + memberRepository1);
		System.out.println("orderService -> memberResposotiry : " + memberRepository2);
		System.out.println("memberResposotiry : " + memberRepository);

		Assertions.assertThat(memberService.getMemberRepository()).isSameAs(memberRepository);
		Assertions.assertThat(orderService.getMemberRepository()).isSameAs(memberRepository);
	}

}
