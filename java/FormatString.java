import static java.lang.System.out;
import java.util.Date;
public class FormatString {
	public static void main(String[] args) {
		int i = 3;
		float x = 3.14f;
		Date today = new Date();
		out.println(String.format("an int %d, an float %.2f", i, x));
		out.println(String.format("a date %tc and a time %tr", today, today));
		out.println(String.format("a date %tc and a time %<tr", today));
		out.println(String.format("%tA, %<td %<tB", today));
		out.println(String.format("Idag er %tA, %<td. %<tB %<tY.", today));
		out.println(String.format("%td.%<tb.%<ty, %<ta", today));
		out.println(String.format("%tY-%<tm-%<td", today));
	}
}