package online.rh.simpleweather.util;

import java.util.Calendar;
import java.util.HashMap;

public class TimeTool {
    /**
     * 获得系统时间 年、月、日、小时、分钟
     *
     * @return HashMap
     */
    public static HashMap<String, String> getTimeNew() {
        HashMap<String, String> map = new HashMap<>();
        Calendar calendar = Calendar.getInstance();


        map.put("year", String.valueOf(calendar.get(Calendar.YEAR)));
        map.put("month", String.valueOf(calendar.get(Calendar.MONTH)));
        map.put("day", String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        map.put("hour", String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)));
        map.put("minute", String.valueOf(calendar.get(Calendar.MINUTE)));

        String Week = "";
        int wek = calendar.get(Calendar.DAY_OF_WEEK);
        if (wek == 1) {
            Week += "日";
        }
        if (wek == 2) {
            Week += "一";
        }
        if (wek == 3) {
            Week += "二";
        }
        if (wek == 4) {
            Week += "三";
        }
        if (wek == 5) {
            Week += "四";
        }
        if (wek == 6) {
            Week += "五";
        }
        if (wek == 7) {
            Week += "六";
        }

        map.put("week", Week);

        return map;
    }
}
