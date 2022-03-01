package com.nagaja.the330.utils;


import android.text.TextUtils;
import android.util.Log;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class AppDateUtils {
    public static String FORMAT_1 = "dd/MM/yyyy HH:mm:ss";
    public static String FORMAT_2 = "dd/MM/yyyy";
    public static String FORMAT_3 = "dd-MM-yyyy";
    public static String FORMAT_4 = "yyyy/MM/dd hh:mm:ss";
    public static String FORMAT_5 = "yyyy-MM-dd"; // UI mobile hay dung
    public static String FORMAT_6 = "yyyy-MM-dd'T'HH:mm:ssZ";
    public static String FORMAT_7 = "yyyy-MM-dd HH:mm"; // sv tra ve format nay
    public static String FORMAT_8 = "yyyy-MM-dd hh:mm";
    public static String FORMAT_9 = "hh:mm";
    public static String FORMAT_10 = "yyyy-MM-dd'T'HH:mm:ss.sss'Z'";
    public static String FORMAT_11 = "hh:mm aa";
    public static String FORMAT_12 = "yyyy-MM-dd hh:mm aa";
    public static String FORMAT_13 = "yyyy-MM-dd EEEE";
    public static String FORMAT_14 = "MMM dd, yyyy, hh:mm:ss aa";
    public static String FORMAT_15 = "YYYY.MM.dd";
    public static String FORMAT_16 = "yyyy-MM-dd'T'HH:mm:ss.sss";
    public static String FORMAT_17 = "yyyy-MM-dd HH:mm:ss";
    public static String FORMAT_18 = "yyyy년 MM월 dd일 hh:mm aa";
    public static String FORMAT_19 = "yyyy.MM.dd HH:mm";

    public static String changeDateFormat(String currentFormat, String requiredFormat, String dateString) {
        String result = dateString;
        if (dateString == null || TextUtils.isEmpty(dateString)) {
            return result;
        }
        SimpleDateFormat formatterOld = new SimpleDateFormat(currentFormat);
        formatterOld.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat formatterNew = new SimpleDateFormat(requiredFormat, Locale.getDefault());
//        formatterNew.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = formatterOld.parse(dateString);
        } catch (Exception e) {
            //  e.printStackTrace();
        }
        if (date != null) {
            result = formatterNew.format(date);
        }
        return result;
    }

    public static String changeDateFormatV2(String currentFormat, String requiredFormat, String dateString) {
        String result = dateString;
        if (dateString == null || TextUtils.isEmpty(dateString)) {
            return result;
        }
        SimpleDateFormat formatterOld = new SimpleDateFormat(currentFormat);
//        formatterOld.setTimeZone(TimeZone.getTimeZone("GMT"));
        SimpleDateFormat formatterNew = new SimpleDateFormat(requiredFormat);
        formatterNew.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = formatterOld.parse(dateString);
        } catch (Exception e) {
            //  e.printStackTrace();
        }
        if (date != null) {
            result = formatterNew.format(date);
        }
        return result;
    }

    public static String getTimeLocalAgo(String currentFormat, String requiredFormat, String dateString) {
        String result = dateString;
        if (dateString == null || TextUtils.isEmpty(dateString)) {
            return result;
        }
        SimpleDateFormat formaterOld = new SimpleDateFormat(currentFormat);
        formaterOld.setTimeZone(TimeZone.getTimeZone("UTC"));

        SimpleDateFormat formaterNew = new SimpleDateFormat(requiredFormat, Locale.getDefault());

        Date dateInput = null;
        Date dateNow = new Date();
        try {
            dateInput = formaterOld.parse(dateString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (dateInput != null) {
            result = formaterNew.format(dateInput);
        }
        long ago = TimeUnit.MILLISECONDS.toSeconds(dateNow.getTime() - dateInput.getTime());
        if (ago < 60) {
            return "지금";
        } else if (ago < 60 * 60) {
            return ago / 60 + "분전";
        } else if (ago < 60 * 60 * 24) {
            return ago / (60 * 60) + "시간전";
        } else if (ago < 60 * 60 * 24 * 7) {
            return ago / (60 * 60 * 24) + "일전";
        } else if (ago < 60 * 60 * 24 * 7 * 4) {
            return ago / (60 * 60 * 24 * 7) + "주전";
        } else return result;
    }

    public static Date convertStringDateToDate(String startDateString, String format) {
        try {
            DateFormat df = new SimpleDateFormat(format);
            Date startDate;
            startDate = df.parse(startDateString);
            String newDateString = df.format(startDate);
            return startDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * isOrigin:
     * true - get time from server
     * false - convert time to local first
     */
    public static Calendar StringDateToCalendar(String strDate, String format, Boolean isOrigin) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        if (!isOrigin) {
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        }
        try {
            cal.setTime(sdf.parse(strDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal;
    }

    public static boolean validateEndDateGreaterThanStartDate(String sStartDate, String sEndDate, String format) {
        try {
            if (sStartDate.length() > 0 && sEndDate.length() > 0) {
                Date startDate = AppDateUtils.convertStringDateToDate(sStartDate, format);
                Date endDate = AppDateUtils.convertStringDateToDate(sEndDate, format);
                //"endDate is after startDate
                if (endDate.compareTo(startDate) > 0) {
                    return true;
                } else if (endDate.compareTo(startDate) < 0) {
                    return false;
                } else {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean validateInputDateWithCurrentDate(String strInputDate) {
        try {
            if (strInputDate.length() > 0) {
                Date currentDate = AppDateUtils.convertStringDateToDate(getCurrentDate(), AppDateUtils.FORMAT_2);
                Date inputDate = AppDateUtils.convertStringDateToDate(strInputDate, AppDateUtils.FORMAT_2);
                // ngay nhap vao > ngay hien tai
                if (inputDate.after(currentDate)) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static String numberFormat2Digit(int number) {
        return (number < 10) ? "0" + number : number + "";
    }

    public static String convertTime(long time) {
        Date date = new Date(time);
        DateFormat format = new SimpleDateFormat(FORMAT_7);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format.format(date);
    }

    public static String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        String fromMothStr = "";
        int tempMonth = mMonth + 1;
        if (tempMonth < 10) {
            fromMothStr = "0" + tempMonth;
        } else {
            fromMothStr = "" + tempMonth;
        }
        String fromDayStr = "";
        if (mDay < 10) {
            fromDayStr = "0" + mDay;
        } else {
            fromDayStr = "" + mDay;
        }
        String currentDate = fromDayStr + "/" + fromMothStr + "/" + mYear;
        return currentDate;
    }

    public static String getTomorrowDate() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, +1);
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        String fromMothStr = "";
        int tempMonth = mMonth + 1;
        if (tempMonth < 10) {
            fromMothStr = "0" + tempMonth;
        } else {
            fromMothStr = "" + tempMonth;
        }
        String fromDayStr = "";
        if (mDay < 10) {
            fromDayStr = "0" + mDay;
        } else {
            fromDayStr = "" + mDay;
        }
        String currentDate = fromDayStr + "/" + fromMothStr + "/" + mYear;
        return currentDate;
    }

    public static String getYesterdayDate() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        String fromMothStr = "";
        int tempMonth = mMonth + 1;
        if (tempMonth < 10) {
            fromMothStr = "0" + tempMonth;
        } else {
            fromMothStr = "" + tempMonth;
        }
        String fromDayStr = "";
        if (mDay < 10) {
            fromDayStr = "0" + mDay;
        } else {
            fromDayStr = "" + mDay;
        }
        String currentDate = fromDayStr + "/" + fromMothStr + "/" + mYear;
        return currentDate;
    }

    /**
     * This Method is unit tested properly for very different cases , taking
     * care of Leap Year days difference in a year, and date cases month and
     * Year boundary cases (12/31/1980, 01/01/1980 etc)
     **/
    public static int getAge(String dateOfBirthString) {
        Date dateOfBirth = convertStringDateToDate(dateOfBirthString, AppDateUtils.FORMAT_2);

        Calendar today = Calendar.getInstance();
        Calendar birthDate = Calendar.getInstance();
        int age = 0;
        birthDate.setTime(dateOfBirth);
        if (birthDate.after(today)) {
            //throw new IllegalArgumentException("Can't be born in the future");
            return -1;
        }
        age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);
        // If birth date is greater than todays date (after 2 days adjustment of
        // leap year) then decrement age one year
        if ((birthDate.get(Calendar.DAY_OF_YEAR)
                - today.get(Calendar.DAY_OF_YEAR) > 3)
                || (birthDate.get(Calendar.MONTH) > today.get(Calendar.MONTH))) {
            age--;
            // If birth date and todays date are of same month and birth day of
            // month is greater than todays day of month then decrement age
        } else if ((birthDate.get(Calendar.MONTH) == today.get(Calendar.MONTH))
                && (birthDate.get(Calendar.DAY_OF_MONTH) > today
                .get(Calendar.DAY_OF_MONTH))) {
            age--;
        }
        return age;
    }


    public static final String PATTERN_DATE_FORMAT = "dd/MM/yyyy";
    public static final String PATTERN_DATE_FORMAT_SIMPLE = "MM/dd/yy";
    public static final String PATTERN_TIME_FORMAT = "HH:mm";
    public static final String PATTERN_DATE_TIME_FORMAT = "dd/MM/yyyy - HH:mm";


    public static String dateExpiredQuarter(Calendar cal) {
        String date;
        DateFormat dateFormatter = new SimpleDateFormat(PATTERN_DATE_FORMAT, Locale.ENGLISH);
//        cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 90); // 90 ngày
        date = dateFormatter.format(cal.getTime());
        return date;
    }

    public static String dateExpiredMonth(Calendar cal) {
        String date;
        DateFormat dateFormatter = new SimpleDateFormat(PATTERN_DATE_FORMAT, Locale.ENGLISH);
//        cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 30); // 30 ngày
        date = dateFormatter.format(cal.getTime());
        return date;
    }

    public static Date stringToDate(String strDate, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = null;
        if (strDate == null || strDate.length() == 0) {
            return null;
        }
        try {
            date = simpleDateFormat.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    public static boolean CheckTimeDuplicate(String timeFrom1, String timeTo1, String timeFrom2, String timeTo2) {

        try {
            if (isTimeBetweenTwoTime(timeFrom1, timeFrom2, timeTo2) ||
                    isTimeBetweenTwoTime(timeTo1, timeFrom2, timeTo2) ||
                    isTimeBetweenTwoTime(timeFrom2, timeFrom1, timeTo1) ||
                    isTimeBetweenTwoTime(timeTo2, timeFrom1, timeTo1)) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return true;
        }
        return false;
    }

    public static Long timeToCurrent(String inputTime, String formatTime) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatTime, Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date dateInput = null;
        try {
            dateInput = sdf.parse(inputTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long dateInputMil = dateInput.getTime();
        long dateNow = System.currentTimeMillis();
        return dateNow - dateInputMil;
    }

    private static boolean isTimeBetweenTwoTime(String timeCheck, String from, String to) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(FORMAT_1, Locale.getDefault());

        Date date_from = formatter.parse(from);
        Date date_to = formatter.parse(to);

        Date date_check = formatter.parse(timeCheck);

        Time startTime = new Time(date_from.getTime());
        Time endTime = new Time(date_to.getTime());
        Time checkTime = new Time(date_check.getTime());

        if (checkTime.before(endTime) && checkTime.after(startTime) || checkTime.equals(startTime) || checkTime.equals(endTime)) {
            return true;
        }
        return false;
    }

    public static Date getFirstDayOfQuarter(Calendar date) {
        date.set(Calendar.MONTH, date.get(Calendar.MONTH) - 1);
        return date.getTime();
    }

    public static Date getLastDayOfQuarter(Calendar date) {
        date.set(Calendar.MONTH, date.get(Calendar.MONTH) - 1);
        date.set(Calendar.DAY_OF_MONTH, date.getActualMaximum(Calendar.DAY_OF_MONTH));
        return date.getTime();
    }

    public static long daysBetweenDates(String sStartDate, String sEndDate) {
        try {
            Date startDate = AppDateUtils.convertStringDateToDate(sStartDate, AppDateUtils.FORMAT_2);
            Date endDate = AppDateUtils.convertStringDateToDate(sEndDate, AppDateUtils.FORMAT_2);

            long timeDifferenceMilliseconds = endDate.getTime() - startDate.getTime();
            long diffDays = timeDifferenceMilliseconds / (60 * 60 * 1000 * 24);
            return diffDays;
        } catch (Exception e) {
        }
        return -1;
    }

    public static String changeLocalDateFormat(String currentFormat, String requiredFormat, String dateString) {
        String result = dateString;
        if (dateString == null || TextUtils.isEmpty(dateString)) {
            return result;
        }
        SimpleDateFormat formatterOld = new SimpleDateFormat(currentFormat, Locale.getDefault());
        SimpleDateFormat formatterNew = new SimpleDateFormat(requiredFormat, Locale.getDefault());
//        formatterNew.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = formatterOld.parse(dateString);
        } catch (Exception e) {
            //  e.printStackTrace();
        }
        if (date != null) {
            result = formatterNew.format(date);
        }
        return result;
    }

    public static String format(long timestamp, SimpleDateFormat sdf) {
        Date timeD = new Date(timestamp);
        return sdf.format(timeD);
    }

    public static Date parse(long timestamp) {
        return new Date(timestamp);
    }

    public static String secondToTime(Long seconds) {
        // format hh:mm:ss
        long h = seconds/3600;
        long m = (seconds - h * 3600) / 60;
        long s = seconds - h * 3600 - m * 60;

        String time = "";
        if (h >= 0) time = String.format("0%s:", h);
        if (h >= 10) time += String.format("%s:", h);
        if (m >= 0 && m < 10) time += String.format("0%s:", m);
        if (m >= 10) time += String.format("%s:", m);
        if (s >= 0 && s < 10) time += String.format("0%s", s);
        if (s >= 10) time += String.format("%s", s);
        return time;
    }

    public static String secondToTime2(Long seconds) {
        //format mm:ss
        long h = seconds/3600;
        long m = (seconds - h * 3600) / 60;
        long s = seconds - h * 3600 - m * 60;

        String time = "";
        if (h == 0) time = "";
        if (h > 0) time += String.format("0%s:", h);
        if (h >= 10) time += String.format("%s:", h);
        if (m >= 0 && m < 10) time += String.format("0%s:", m);
        if (m >= 10) time += String.format("%s:", m);
        if (s >= 0 && s < 10) time += String.format("0%s", s);
        if (s >= 10) time += String.format("%s", s);
        return time;
    }
}
