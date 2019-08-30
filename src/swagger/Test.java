package swagger;

import com.template.util.StringUtil;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

public class Test {
	public static void main(String[] args) {
		ArrayList<String> test = new ArrayList<String>();
		System.out.println(((ParameterizedType)test.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
		
	}
}
