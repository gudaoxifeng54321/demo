package com.ly.common.time;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;

/**
 * 时间工具
 * 
 * @author gzh
 * 
 */
public class TimeUtil {

  private static SimpleDateFormat formation = new SimpleDateFormat("yyyy-MM-dd");

  /**
   * 将java.util.time转为字符串日期"yyyy-MM-dd"
   * 
   * @param date
   * @return
   */
  public static String formatDay(Date date) {
    return formation.format(date);
  }

  /**
   * <p>
   * 将字符串转为日期对象
   * <p>
   * 字符串格式:yyyy-MM-dd HH:mm:ss"
   * 
   * @param strDate
   * @return
   */
  public static Date parseStringToDate(String strDate) {
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    try {
      Date date = dateFormat.parse(strDate);
      return date;
    } catch (ParseException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * 获取指定日期前一个月的最后一天
   * 
   * @param date
   * @return
   */
  public static Date getLastDayOfPrevMonth(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    int month = calendar.get(Calendar.MONTH);
    calendar.set(Calendar.MONTH, month - 1);
    String lastDayOfPrevMonth = getLastMonthDay(calendar.getTime());
    return parseStringToDate(lastDayOfPrevMonth);
  }

  /**
   * 获得指定日期的前一天
   * 
   * @param date
   * @return
   */
  public static String getBeforeDay(Date date) {
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    // 计算日期
    int day = c.get(Calendar.DATE);
    c.set(Calendar.DATE, day - 1);
    String beforeDay = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
    return beforeDay;
  }

  /**
   * 获得指定日期的前一天
   * 
   * @param date
   * @return
   */
  public static String getBeforeStrDay(Date date) {
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    // 计算日期
    int day = c.get(Calendar.DATE);
    c.set(Calendar.DATE, day - 1);
    String beforeDay = new SimpleDateFormat("MM月dd日").format(c.getTime());
    return beforeDay;
  }

  /**
   * 计算指定日期的后一天
   * 
   * @param date
   * @return
   */
  public static String getAfterDay(Date date) {
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    // 计算日期
    int day = c.get(Calendar.DATE);
    c.set(Calendar.DATE, day + 1);
    String afterDay = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
    return afterDay;
  }

  /**
   * 获得指定日期的前一天
   * 
   * @param date
   * @return
   */
  public static Calendar getLastDay(Date date) {
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    // 计算日期
    int day = c.get(Calendar.DATE);
    c.set(Calendar.DATE, day - 1);

    return c;
  }

  /**
   * 获取0点时间
   * 
   * @param date
   * @return
   */
  public static String getMorning(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    SimpleDateFormat formation = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);

    return formation.format(calendar.getTime());
  }

  /**
   * 获取24点时间
   * 
   * @param date
   * @return
   */
  public static String getNight(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    SimpleDateFormat formation = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    calendar.set(Calendar.HOUR_OF_DAY, 23);
    calendar.set(Calendar.MINUTE, 59);
    calendar.set(Calendar.SECOND, 59);
    calendar.set(Calendar.MILLISECOND, 59);

    return formation.format(calendar.getTime());
  }

  /**
   * 指定日期和今天的相差天数
   * 
   * @param time
   * @return
   */
  public static int getDiffDays(String time) {
    if (time == null || time.equals("")) {
      return 0;
    }

    try {
      Date beginDate = formation.parse(time);
      long beginUnixTime = beginDate.getTime();
      long now = System.currentTimeMillis();
      long dif = (now - beginUnixTime) / (1000 * 3600 * 24);
      return Integer.parseInt(String.valueOf(dif));
    } catch (ParseException e) {
      e.printStackTrace();
      return 0;
    }
  }

  /**
   * 获取当月第一天
   * 
   * @return
   */
  public static String getFirstMonthDay() {
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.DAY_OF_MONTH, 1);

    return formation.format(calendar.getTime());
  }

  /**
   * 获得指定日期的月份的第一天
   * 
   * @param date
   * @return
   */
  public static String getFirstMonthDay(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.set(Calendar.DAY_OF_MONTH, 1);
    return getMorning(calendar.getTime());
  }

  /**
   * 获取当月最后一天的24时
   * 
   * @return
   */
  public static String getLastMonthDay() {
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

    return getNight(calendar.getTime());
  }

  /**
   * 获得指定日期所在月份的最后一天
   * 
   * @param date
   * @param last
   * @return
   */
  public static String getLastMonthDay(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
    return getNight(calendar.getTime());
  }

  public static Calendar getLastDay(Date date, Integer last) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - last);

    return calendar;
  }

  /**
   * 获取当前时间
   * <p>
   * 时间格式为yyyy-MM-dd HH:mm:ss
   * 
   * @return
   */
  public static String paserDateToString(Date date) {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    return format.format(date);
  }

  public static String getNextMonth(String calMonth) {
    StringBuffer res = new StringBuffer();
    int year = Integer.parseInt(calMonth.substring(0, 4));
    int month = Integer.parseInt(calMonth.substring(4, 6));
    if (month > 12) {
      return null;
    }
    if (month == 12) {
      year++;
      month = 1;
    } else {
      month++;
    }
    if (month <= 9) {
      res.append(year).append(0).append(month);
    } else {
      res.append(year).append(month);
    }
    return res.toString();
  }

  /**
   * 获得日其所在月份
   * 
   * @param date
   * @return
   */
  public static String getMonthByDate(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    StringBuffer resMonth = new StringBuffer();
    if (month == 12) {
      year++;
      month = 1;
    } else {
      month++;
    }
    if (month <= 9) {
      resMonth.append(year).append(0).append(month);
    } else {
      resMonth.append(year).append(month);
    }
    return resMonth.toString();
  }

  public static String getPreMonthByDate(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    int y = calendar.get(Calendar.MONTH);
    calendar.set(Calendar.MONTH, y - 1);
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    StringBuffer resMonth = new StringBuffer();
    if (month == 12) {
      year++;
      month = 1;
    } else {
      month++;
    }
    if (month <= 9) {
      resMonth.append(year).append(0).append(month);
    } else {
      resMonth.append(year).append(month);
    }
    return resMonth.toString();
  }

  /**
   * 计算指定两日的相差天数
   * 
   * @param startDate
   * @param endDate
   * @return
   */
  public static int daysBetween(Date startDate, Date endDate) {
    if (startDate == null || endDate == null) {
      throw new RuntimeException("args is null");
    }
    return Days.daysBetween(new LocalDate(startDate), new LocalDate(endDate)).getDays();
  }

  /**
   * 添加天数
   */
  public static Date addDays(Date startDate, int days) {
    if (startDate == null) {
      return null;
    }
    return new DateTime(startDate).plusDays(days).toDate();
  }

  /**
   * 根据毫秒数得到天时分
   * 
   * @author lff
   * @param runTime
   * @return
   */
  public static String getRunTime(long runTime) {
    int day, hour, minute, second;
    day = (int) (runTime / (1000 * 60 * 60 * 24));
    runTime = runTime % (1000 * 60 * 60 * 24);
    hour = (int) (runTime / (1000 * 60 * 60));
    runTime = runTime % (1000 * 60 * 60);
    minute = (int) (runTime / (1000 * 60));
    runTime = runTime % (1000 * 60);
    second = (int) (runTime / 1000);
    return day + "天" + hour + "小时" + minute + "分" + second + "秒";
  }

  public static void main(String[] args) {
    long start = System.currentTimeMillis();
    System.out.println(getRunTime(43233030000L));
    System.out.println(System.currentTimeMillis() - start);

  }

  /**
   * 根据日期获得年
   */
  public static int getYearByDate(Date date) {
    DateTime dateTime = new DateTime(date);
    return dateTime.getYear();
  }

  /**
   * 根据日期获得年
   */
  public static int getMonthDayByDate(Date date) {
    DateTime dateTime = new DateTime(date);
    return dateTime.getDayOfMonth();
  }

  /**
   * 根据日期获得月份
   * 
   * @param date
   * @return
   */
  public static Integer getMonthOfYearByDate(Date date) {
    DateTime dateTime = new DateTime(date);
    return dateTime.getMonthOfYear();
  }

}
