package com.project.alarmeweb.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class DateUtil {

    private final static String SIMPLEDATEFORMAT = "yyyyMMdd";

    /**
     *
     * <pre>
     * 기준일에 기간을 더하여 날짜를 반환
     * </pre>
     * @param       //날짜 format "yyyy년 MM월 dd일"
     * @param  term // 세번째  날짜 plus, minus 값
     * @param  date // 기준일  이 날짜를 기준으로 한다.
     * @return String
     */
    public static String getAddDay(String dateFormats, long term, Date date) {
        return format(getAddDate(date, term), dateFormats);
    }


    /**
     * <pre>
     * 기준일에 기간을 더하여 날짜를 반환
     * </pre>
     * @param  date // 기준일
     * @param  term // 첫번째 파라미터  날짜 plus, minus 값
     * @return Date
     */
    public static Date getAddDate(Date date, long term) {
        return new Date(date.getTime() + term * 24L * 60L * 60L * 1000L);
    }

    /**
     * <pre>
     * 기준일에 시간을 더하여 날짜를 반환
     * </pre>
     * @param  date // 기준일
     * @param  term // 첫번째 파라미터  날짜 plus, minus 값
     * @return Date
     */
    public static Date getAddTime(Date date, long term) {
        return new Date(date.getTime() + term * 60L * 60L * 1000L);
    }

    /**
     * <pre>
     * 날짜 포맷 설정
     * </pre>
     * @param  date //원하는 Date 타입의 날짜
     * @param  fmt //반환될 포맷. "yyyy-MM-dd"
     * @return String
     */
    public static String format(Date date, String fmt) {
        SimpleDateFormat simpledateformat = new SimpleDateFormat(fmt,
                java.util.Locale.KOREA);
        return simpledateformat.format(date);
    }

    /**
     * 첫번째 인자와 두번째 형태가 일치해야 한다.
     *
     * @param  s //원하는 날짜 "2006.03.04"
     * @param  s1 //포맷. "yyyy.MM.dd"
     * @return Date
     */
    public static Date parse(String s, String s1) throws ParseException {
        SimpleDateFormat simpledateformat = new SimpleDateFormat(s1);
        return simpledateformat.parse(s);
    }

    //날짜 값이 있을 경우만 체크 한다.(날짜의 유효성)
    public static boolean isValidDate(String date) {

        if (date == null || "".equals(date)){
            return false;
        }
        if (date.length() != 8){
            return false;
        }
        if (!StringUtils.isNumeric(date)){
            return false;
        }

        return isValidDate(date, SIMPLEDATEFORMAT);
    }

    //날짜 값이 있을 경우만 체크 한다.(날짜의 유효성)
    public static boolean isValidDate(String date, String frm) {
        if (frm == null || "".equals(frm)){
            frm = SIMPLEDATEFORMAT;
        }
        if (date == null || "".equals(date)) {
            return false;
        }

        String afterValue = null;
        try {
            afterValue = format(parse(date, frm), frm);
        } catch (Exception e) {
            log.error("{}",e);
            return false;
        }
        return date.equals(afterValue);
    }


    public static String cvtImageUrl(String url) {
        if (null == url || "".equals(url)) {
            return "";
        }

        return "";
    }

    /**
     * 현재 시간을 format 형태로
     *
     * @param format 날짜형식
     * @return format으로 설정된 날짜 String
     * @see java.text.SimpleDateFormat
     */
    public static String getToday(String format) {
        return format(new Date(), format);
    }

    /**
     * 현재 시간을 format 형태로
     *
     * @return format으로 설정된 날짜 String
     * @see java.text.SimpleDateFormat
     */
    public static String getToday() {
        return getSimpleDateFormat(SIMPLEDATEFORMAT).format(getDate());   // 기본값
    }

    /**
     * @param format
     *            날짜형식
     * @return format으로 설정된 SimpleDateFormat 인스턴스
     */
    public static SimpleDateFormat getSimpleDateFormat(String format) {
        return new SimpleDateFormat(format, java.util.Locale.KOREA);
    }

    /**
     * 현재 일자를 Date 객체로 반환한다.
     *
     * @return 현재 일자에 해당하는 Date
     */
    public static Date getDate() {
        return getCalendar().getTime();
    }

    public static Calendar getCalendar() {
        return Calendar.getInstance(java.util.Locale.KOREA);
    }

}