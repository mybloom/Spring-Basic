package hello.core.singletone;

import static org.assertj.core.api.Assertions.*;

import hello.core.AppConfig;
import hello.core.member.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SingtoneTest {

	@Test
	@DisplayName("스프링 없는 순수한 DI 컨테이너")
	void pureContainer() {
		AppConfig appConfig = new AppConfig();

		//1. 조회 : 호출할 때마다 객체를 생성
		MemberService memberService1 = appConfig.memberService();

		//2. 조회 : 호출할 때마다 객체를 생성
		MemberService memberService2 = appConfig.memberService();

		//참조값이 다른 것 확인
		System.out.println("memberService1 = " + memberService1);
		System.out.println("memberService2 = " + memberService2);

		//memberService1 != memberService2
		assertThat(memberService1).isNotSameAs(memberService2);
	}

	@Test
	@DisplayName("싱글톤 패턴을 적용한 객체 사용")
	void singleToneServiceTest() {
		SingleToneService singleToneService1 = SingleToneService.getInstance();
		SingleToneService singleToneService2 = SingleToneService.getInstance();

		//참조값 확인
		System.out.println("singleToneService1 = " + singleToneService1);
		System.out.println("singleToneService2 = " + singleToneService2);

		assertThat(singleToneService1).isSameAs(singleToneService2);
		//same  : ==
		//equal : eqauls
	}

	@Test
	@DisplayName("스프링 컨테이너와 싱글톤")
	void springContainer() {
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);

		MemberService memberService1 = applicationContext.getBean("memberService", MemberService.class);
		MemberService memberService2 = applicationContext.getBean("memberService", MemberService.class);

		//참조값 확인
		System.out.println("memberService1 = " + memberService1);
		System.out.println("memberService2 = " + memberService2);

		assertThat(memberService1).isSameAs(memberService2);
	}

}
