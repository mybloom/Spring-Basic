package hello.core.singletone;

public class SingleToneService {

	private static final SingleToneService instance = new SingleToneService();

	private SingleToneService() {}

	public static SingleToneService getInstance() {
		return instance;
	}

	public void logic() {
		System.out.println("싱글톤 객체 로직 호출");
	}


}
