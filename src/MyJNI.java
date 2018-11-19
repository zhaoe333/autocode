
public class MyJNI {
	static{
		System.loadLibrary("test");
	}
	
	public static native String sub(String name,String sex);
	
	public static void main(String[] args) {
		System.out.println(MyJNI.sub("abc", "123"));
	}
}
