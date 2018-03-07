import org.apache.commons.lang3.time.DateFormatUtils;

public class TimeTest {
    public static void main(String[] args) {

       String ts =  DateFormatUtils.format(1519320201000L,"yyyy-MM-dd HH:mm:ss");
        System.out.println(ts);
    }
}
