package hello.core.scope;

import static org.assertj.core.api.Assertions.assertThat;

import hello.core.scope.SingletonTest.SingletonBean;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

public class PrototypeTest {

	@Test
	void prototypeBeanFind() {
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(
			PrototypeBean.class);

		PrototypeBean prototypeBean1 = applicationContext.getBean(PrototypeBean.class);
		PrototypeBean prototypeBean2 = applicationContext.getBean(PrototypeBean.class);

		System.out.println("PrototypeBean1 = " + prototypeBean1);
		System.out.println("PrototypeBean2 = " + prototypeBean2);
		assertThat(prototypeBean1).isNotSameAs(prototypeBean2);

		//스프링 컨테이너가 관리하지 않는 빈이므로 close()가 동작하지 않는다.
//		applicationContext.close();

		//수동으로 해당 Bean의 close를 호출해야 한다.
		prototypeBean1.destroy();
		prototypeBean2.destroy();
	}


	@Scope("prototype")
	static class PrototypeBean {

		@PostConstruct
		public void init() {
			System.out.println("PrototypeBean.init");
		}

		@PreDestroy
		public void destroy() {
			System.out.println("PrototypeBean.destroy");
		}
	}
}
