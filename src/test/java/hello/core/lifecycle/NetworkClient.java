package hello.core.lifecycle;

import org.junit.platform.engine.support.discovery.EngineDiscoveryRequestResolver.InitializationContext;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class NetworkClient implements InitializingBean, DisposableBean {

	private String url;

	public NetworkClient() {
		System.out.println("생성자 호출, url = " + url);
	}

	public void setUrl(String url) {
		this.url = url;
	}

	//서비스 시작시 호출
	public void connect() {
		System.out.println("connect = " + url);
	}

	public void call(String message) {
		System.out.println("call: " + url + ", message = " + message);
	}

	//서비스 종료시 호출
	public void disconnect() {
		System.out.println("close = " + url);
	}

	//의존관계 주입이 끝나고 나면 호출됨
	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("NetworkCilent.afterPropertiesSet");
		connect();
		call("초기화 연결 메세지");
	}

	@Override
	public void destroy() throws Exception {
		System.out.println("NetworkCilent.destroy");
		disconnect();
	}
}
