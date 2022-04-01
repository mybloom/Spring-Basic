package hello.core.autowired;

import hello.core.member.Member;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.lang.Nullable;

public class AutowiredTest {

	@Test
	void AutowiredOption() {
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(TestBean.class);
	}

	static class TestBean {

		//1. 의존관계가 없으면 메서드 자체가 호출 안된다.
		@Autowired(required = false)
		public void setNoBean1(Member noBean1) {
			System.out.println("noBean1 = " + noBean1);
		}

		//2.
		@Autowired
		public void setNoBean2(@Nullable Member noBean2) {
			System.out.println("noBean2 = " + noBean2);
		}

		//3.
		@Autowired
		public void setNoBean3(Optional<Member> noBean3) {
			System.out.println("noBean3 = " + noBean3);
		}
	}
}
