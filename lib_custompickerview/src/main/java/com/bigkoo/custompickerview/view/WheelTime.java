package com.bigkoo.custompickerview.view;

import android.view.View;

import com.bigkoo.custompickerview.R;
import com.bigkoo.custompickerview.adapter.ArrayWheelAdapter;
import com.bigkoo.custompickerview.adapter.NumericWheelAdapter;
import com.bigkoo.custompickerview.contrarywind.listener.OnItemSelectedListener;
import com.bigkoo.custompickerview.contrarywind.view.CustomWheelView;
import com.bigkoo.custompickerview.listener.ISelectTimeCallback;
import com.bigkoo.custompickerview.utils.ChinaDate;
import com.bigkoo.custompickerview.utils.LunarCalendar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


public class WheelTime {
    public static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private View view;
    private CustomWheelView wv_year;
    private CustomWheelView wv_month;
    private CustomWheelView wv_day;
    private CustomWheelView wv_hours;
    private CustomWheelView wv_minutes;
    private CustomWheelView wv_seconds;
    private int gravity;

    private boolean[] type;
    private static final int DEFAULT_START_YEAR = 1900;
    private static final int DEFAULT_END_YEAR = 2100;
    private static final int DEFAULT_START_MONTH = 1;
    private static final int DEFAULT_END_MONTH = 12;
    private static final int DEFAULT_START_DAY = 1;
    private static final int DEFAULT_END_DAY = 31;
    private static final int DEFAULT_START_HOUR = 0;
    private static final int DEFAULT_END_HOUR = 23;
    private static final int DEFAULT_START_MINUTE = 0;
    private static final int DEFAULT_END_MINUTE = 59;
    private static final int DEFAULT_START_SECOND = 0;
    private static final int DEFAULT_END_SECOND = 59;

    private int startYear = DEFAULT_START_YEAR;
    private int endYear = DEFAULT_END_YEAR;
    private int startMonth = DEFAULT_START_MONTH;
    private int endMonth = DEFAULT_END_MONTH;
    private int startDay = DEFAULT_START_DAY;
    private int endDay = DEFAULT_END_DAY; //表示31天的
    private int startHour = DEFAULT_START_HOUR;
    private int endHour = DEFAULT_END_HOUR;
    private int startMinute = DEFAULT_START_MINUTE;
    private int endMinute = DEFAULT_END_MINUTE;
//    private int startSecond = DEFAULT_START_SECOND;
//    private int endSecond = DEFAULT_END_SECOND;

//    private int currentYear;
//    private int currentMonth;
//    private int currentDay;
//    private int currentHour;
//    private int currentMinute;

    private int textSize;

    private boolean isLunarCalendar = false;
    private ISelectTimeCallback mSelectChangeCallback;
    private boolean isAccurate;

    public WheelTime(View view, boolean[] type, int gravity, int textSize) {
        super();
        this.view = view;
        this.type = type;
        this.gravity = gravity;
        this.textSize = textSize;
        wv_year = (CustomWheelView) view.findViewById(R.id.year);
        wv_month = (CustomWheelView) view.findViewById(R.id.month);
        wv_day = (CustomWheelView) view.findViewById(R.id.day);
        wv_hours = (CustomWheelView) view.findViewById(R.id.hour);
        wv_minutes = (CustomWheelView) view.findViewById(R.id.min);
        wv_seconds = (CustomWheelView) view.findViewById(R.id.second);
    }

    public void setLunarMode(boolean isLunarCalendar) {
        this.isLunarCalendar = isLunarCalendar;
    }

    public boolean isLunarMode() {
        return isLunarCalendar;
    }

    public void setPicker(int year, int month, int day) {
        this.setPicker(year, month, day, 0, 0, 0);
    }

    public void setPicker(int year, final int month, int day, int h, int m, int s) {
        if (isLunarCalendar) {
            int[] lunar = LunarCalendar.solarToLunar(year, month + 1, day);
            setLunar(lunar[0], lunar[1] - 1, lunar[2], lunar[3] == 1, h, m, s);
        } else {
            setSolar(year, month, day, h, m, s);
        }
    }

    /**
     * 设置农历
     *
     * @param year
     * @param month
     * @param day
     * @param h
     * @param m
     * @param s
     */
    private void setLunar(int year, final int month, int day, boolean isLeap, int h, int m, int s) {
        // 年
        wv_year.setAdapter(new ArrayWheelAdapter(ChinaDate.getYears(startYear, endYear)));// 设置"年"的显示数据
        wv_year.setLabel("");// 添加文字
        wv_year.setCurrentItem(year - startYear);// 初始化时显示的数据
        wv_year.setGravity(gravity);

        // 月
        wv_month.setAdapter(new ArrayWheelAdapter(ChinaDate.getMonths(year)));
        wv_month.setLabel("");

        int leapMonth = ChinaDate.leapMonth(year);
        if (leapMonth != 0 && (month > leapMonth - 1 || isLeap)) { //选中月是闰月或大于闰月
            wv_month.setCurrentItem(month + 1);
        } else {
            wv_month.setCurrentItem(month);
        }

        wv_month.setGravity(gravity);

        // 日
        // 判断大小月及是否闰年,用来确定"日"的数据
        if (ChinaDate.leapMonth(year) == 0) {
            wv_day.setAdapter(new ArrayWheelAdapter(ChinaDate.getLunarDays(ChinaDate.monthDays(year, month))));
        } else {
            wv_day.setAdapter(new ArrayWheelAdapter(ChinaDate.getLunarDays(ChinaDate.leapDays(year))));
        }
        wv_day.setLabel("");
        wv_day.setCurrentItem(day - 1);
        wv_day.setGravity(gravity);

        wv_hours.setAdapter(new NumericWheelAdapter(0, 23));
        //wv_hours.setLabel(context.getString(R.string.pickerview_hours));// 添加文字
        wv_hours.setCurrentItem(h);
        wv_hours.setGravity(gravity);

        wv_minutes.setAdapter(new NumericWheelAdapter(0, 59));
        //wv_minutes.setLabel(context.getString(R.string.pickerview_minutes));// 添加文字
        wv_minutes.setCurrentItem(m);
        wv_minutes.setGravity(gravity);

        wv_seconds.setAdapter(new NumericWheelAdapter(0, 59));
        //wv_seconds.setLabel(context.getString(R.string.pickerview_minutes));// 添加文字
        wv_seconds.setCurrentItem(m);
        wv_seconds.setGravity(gravity);

        // 添加"年"监听
        wv_year.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                int year_num = index + startYear;
                // 判断是不是闰年,来确定月和日的选择
                wv_month.setAdapter(new ArrayWheelAdapter(ChinaDate.getMonths(year_num)));
                if (ChinaDate.leapMonth(year_num) != 0 && wv_month.getCurrentItem() > ChinaDate.leapMonth(year_num) - 1) {
                    wv_month.setCurrentItem(wv_month.getCurrentItem() + 1);
                } else {
                    wv_month.setCurrentItem(wv_month.getCurrentItem());
                }

                int currentIndex = wv_day.getCurrentItem();
                int maxItem = 29;
                if (ChinaDate.leapMonth(year_num) != 0 && wv_month.getCurrentItem() > ChinaDate.leapMonth(year_num) - 1) {
                    if (wv_month.getCurrentItem() == ChinaDate.leapMonth(year_num) + 1) {
                        wv_day.setAdapter(new ArrayWheelAdapter(ChinaDate.getLunarDays(ChinaDate.leapDays(year_num))));
                        maxItem = ChinaDate.leapDays(year_num);
                    } else {
                        wv_day.setAdapter(new ArrayWheelAdapter(ChinaDate.getLunarDays(ChinaDate.monthDays(year_num, wv_month.getCurrentItem()))));
                        maxItem = ChinaDate.monthDays(year_num, wv_month.getCurrentItem());
                    }
                } else {
                    wv_day.setAdapter(new ArrayWheelAdapter(ChinaDate.getLunarDays(ChinaDate.monthDays(year_num, wv_month.getCurrentItem() + 1))));
                    maxItem = ChinaDate.monthDays(year_num, wv_month.getCurrentItem() + 1);
                }

                if (currentIndex > maxItem - 1) {
                    wv_day.setCurrentItem(maxItem - 1);
                }

                if (mSelectChangeCallback != null) {
                    mSelectChangeCallback.onTimeSelectChanged();
                }
            }
        });

        // 添加"月"监听
        wv_month.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                int month_num = index;
                int year_num = wv_year.getCurrentItem() + startYear;
                int currentIndex = wv_day.getCurrentItem();
                int maxItem = 29;
                if (ChinaDate.leapMonth(year_num) != 0 && month_num > ChinaDate.leapMonth(year_num) - 1) {
                    if (wv_month.getCurrentItem() == ChinaDate.leapMonth(year_num) + 1) {
                        wv_day.setAdapter(new ArrayWheelAdapter(ChinaDate.getLunarDays(ChinaDate.leapDays(year_num))));
                        maxItem = ChinaDate.leapDays(year_num);
                    } else {
                        wv_day.setAdapter(new ArrayWheelAdapter(ChinaDate.getLunarDays(ChinaDate.monthDays(year_num, month_num))));
                        maxItem = ChinaDate.monthDays(year_num, month_num);
                    }
                } else {
                    wv_day.setAdapter(new ArrayWheelAdapter(ChinaDate.getLunarDays(ChinaDate.monthDays(year_num, month_num + 1))));
                    maxItem = ChinaDate.monthDays(year_num, month_num + 1);
                }

                if (currentIndex > maxItem - 1) {
                    wv_day.setCurrentItem(maxItem - 1);
                }

                if (mSelectChangeCallback != null) {
                    mSelectChangeCallback.onTimeSelectChanged();
                }
            }
        });

        setChangedListener(wv_day);
        setChangedListener(wv_hours);
        setChangedListener(wv_minutes);
        setChangedListener(wv_seconds);

        if (type.length != 6) {
            throw new RuntimeException("type[] length is not 6");
        }
        wv_year.setVisibility(type[0] ? View.VISIBLE : View.GONE);
        wv_month.setVisibility(type[1] ? View.VISIBLE : View.GONE);
        wv_day.setVisibility(type[2] ? View.VISIBLE : View.GONE);
        wv_hours.setVisibility(type[3] ? View.VISIBLE : View.GONE);
        wv_minutes.setVisibility(type[4] ? View.VISIBLE : View.GONE);
        wv_seconds.setVisibility(type[5] ? View.VISIBLE : View.GONE);
        setContentTextSize();
    }

    /**
     * 设置公历
     *
     * @param year
     * @param month
     * @param day
     * @param h
     * @param m
     * @param s
     */
    private void setSolar(int year, final int month, int day, int h, int m, int s) {
        // 添加大小月月份并将其转换为list,方便之后的判断
        String[] months_big = {"1", "3", "5", "7", "8", "10", "12"};
        String[] months_little = {"4", "6", "9", "11"};

        final List<String> list_big = Arrays.asList(months_big);
        final List<String> list_little = Arrays.asList(months_little);

//        Log.w("tag", year + " " + month + " " + day + " " + h + " " + m + " " + s);
//        currentYear = year;
//        currentMonth = month + 1;
//        currentDay = day;
//        currentHour = h;
//        currentMinute = m;

        // 年

        wv_year.setAdapter(new NumericWheelAdapter(startYear, endYear));// 设置"年"的显示数据

        wv_year.setCurrentItem(year - startYear);// 初始化时显示的数据
        wv_year.setGravity(gravity);
//        // 月
//        if (startYear == endYear) {//开始年等于终止年
//            wv_month.setAdapter(new NumericWheelAdapter(startMonth, endMonth));
//            wv_month.setCurrentItem(month + 1 - startMonth);
//        } else if (year == startYear) {
//            //起始日期的月份控制
//            wv_month.setAdapter(new NumericWheelAdapter(startMonth, 12));
//            wv_month.setCurrentItem(month + 1 - startMonth);
//        } else if (year == endYear) {
//            //终止日期的月份控制
//            wv_month.setAdapter(new NumericWheelAdapter(1, endMonth));
//            wv_month.setCurrentItem(month);
//        } else {
//            wv_month.setAdapter(new NumericWheelAdapter(1, 12));
//            wv_month.setCurrentItem(month);
//        }
        wv_month.setGravity(gravity);
//        // 日
//
//        boolean leapYear = isLeapYear(year);
//        if (startYear == endYear && startMonth == endMonth) {
//            if (list_big.contains(String.valueOf(month + 1))) {
//                if (endDay > 31) {
//                    endDay = 31;
//                }
//                wv_day.setAdapter(new NumericWheelAdapter(startDay, endDay));
//            } else if (list_little.contains(String.valueOf(month + 1))) {
//                if (endDay > 30) {
//                    endDay = 30;
//                }
//                wv_day.setAdapter(new NumericWheelAdapter(startDay, endDay));
//            } else {
//                // 闰年
//                if (leapYear) {
//                    if (endDay > 29) {
//                        endDay = 29;
//                    }
//                    wv_day.setAdapter(new NumericWheelAdapter(startDay, endDay));
//                } else {
//                    if (endDay > 28) {
//                        endDay = 28;
//                    }
//                    wv_day.setAdapter(new NumericWheelAdapter(startDay, endDay));
//                }
//            }
//            wv_day.setCurrentItem(day - startDay);
//        } else if (year == startYear && month + 1 == startMonth) {
//            // 起始日期的天数控制
//            if (list_big.contains(String.valueOf(month + 1))) {
//
//                wv_day.setAdapter(new NumericWheelAdapter(startDay, 31));
//            } else if (list_little.contains(String.valueOf(month + 1))) {
//
//                wv_day.setAdapter(new NumericWheelAdapter(startDay, 30));
//            } else {
//                // 闰年 29，平年 28
//                wv_day.setAdapter(new NumericWheelAdapter(startDay, leapYear ? 29 : 28));
//            }
//            wv_day.setCurrentItem(day - startDay);
//        } else if (year == endYear && month + 1 == endMonth) {
//            // 终止日期的天数控制
//            if (list_big.contains(String.valueOf(month + 1))) {
//                if (endDay > 31) {
//                    endDay = 31;
//                }
//                wv_day.setAdapter(new NumericWheelAdapter(1, endDay));
//            } else if (list_little.contains(String.valueOf(month + 1))) {
//                if (endDay > 30) {
//                    endDay = 30;
//                }
//                wv_day.setAdapter(new NumericWheelAdapter(1, endDay));
//            } else {
//                // 闰年
//                if (leapYear) {
//                    if (endDay > 29) {
//                        endDay = 29;
//                    }
//                    wv_day.setAdapter(new NumericWheelAdapter(1, endDay));
//                } else {
//                    if (endDay > 28) {
//                        endDay = 28;
//                    }
//                    wv_day.setAdapter(new NumericWheelAdapter(1, endDay));
//                }
//            }
//            wv_day.setCurrentItem(day - 1);
//        } else {
//            // 判断大小月及是否闰年,用来确定"日"的数据
//            if (list_big.contains(String.valueOf(month + 1))) {
//                wv_day.setAdapter(new NumericWheelAdapter(1, 31));
//            } else if (list_little.contains(String.valueOf(month + 1))) {
//                wv_day.setAdapter(new NumericWheelAdapter(1, 30));
//            } else {
//                // 闰年 29，平年 28
//                wv_day.setAdapter(new NumericWheelAdapter(startDay, leapYear ? 29 : 28));
//            }
//            wv_day.setCurrentItem(day - 1);
//        }
        resetMonth(month + 1);
        resetDay(day);

        wv_day.setGravity(gravity);
        //时

        if(isAccurate) {
            resetHour(h);
        } else {
            wv_hours.setAdapter(new NumericWheelAdapter(0, 23));
            wv_hours.setCurrentItem(h);
        }
        wv_hours.setGravity(gravity);
        //分
        if(isAccurate) {
            resetMinute(m);
        } else {
            wv_minutes.setAdapter(new NumericWheelAdapter(0, 59));
            wv_minutes.setCurrentItem(m);
        }

        wv_minutes.setGravity(gravity);
        //秒
        wv_seconds.setAdapter(new NumericWheelAdapter(0, 59));

        wv_seconds.setCurrentItem(s);
        wv_seconds.setGravity(gravity);

        // 添加"年"监听
        wv_year.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                resetMonth();
//                int currentMonthItem = wv_month.getCurrentItem();//记录上一次的item位置
//                // 判断大小月及是否闰年,用来确定"日"的数据
//                if (startYear == endYear) {
//                    //重新设置月份
//                    wv_month.setAdapter(new NumericWheelAdapter(startMonth, endMonth));
//
//                    if (currentMonthItem > wv_month.getAdapter().getItemsCount() - 1) {
//                        currentMonthItem = wv_month.getAdapter().getItemsCount() - 1;
//                        wv_month.setCurrentItem(currentMonthItem);
//                    }
//
//                    int monthNum = currentMonthItem + startMonth;
//
//                    if (startMonth == endMonth) {
//                        //重新设置日
//                        setReDay(year_num, monthNum, startDay, endDay, list_big, list_little);
//                    } else if (monthNum == startMonth) {
//                        //重新设置日
//                        setReDay(year_num, monthNum, startDay, 31, list_big, list_little);
//                    } else if (monthNum == endMonth) {
//                        setReDay(year_num, monthNum, 1, endDay, list_big, list_little);
//                    } else {//重新设置日
//                        setReDay(year_num, monthNum, 1, 31, list_big, list_little);
//                    }
//                } else if (year_num == startYear) {//等于开始的年
//                    //重新设置月份
//                    wv_month.setAdapter(new NumericWheelAdapter(startMonth, 12));
//
//                    if (currentMonthItem > wv_month.getAdapter().getItemsCount() - 1) {
//                        currentMonthItem = wv_month.getAdapter().getItemsCount() - 1;
//                        wv_month.setCurrentItem(currentMonthItem);
//                    }
//
//                    int month = currentMonthItem + startMonth;
//                    if (month == startMonth) {
//                        //重新设置日
//                        setReDay(year_num, month, startDay, 31, list_big, list_little);
//                    } else {
//                        //重新设置日
//                        setReDay(year_num, month, 1, 31, list_big, list_little);
//                    }
//
//                } else if (year_num == endYear) {
//                    //重新设置月份
//                    wv_month.setAdapter(new NumericWheelAdapter(1, endMonth));
//                    if (currentMonthItem > wv_month.getAdapter().getItemsCount() - 1) {
//                        currentMonthItem = wv_month.getAdapter().getItemsCount() - 1;
//                        wv_month.setCurrentItem(currentMonthItem);
//                    }
//                    int monthNum = currentMonthItem + 1;
//
//                    if (monthNum == endMonth) {
//                        //重新设置日
//                        setReDay(year_num, monthNum, 1, endDay, list_big, list_little);
//                    } else {
//                        //重新设置日
//                        setReDay(year_num, monthNum, 1, 31, list_big, list_little);
//                    }
//
//                } else {
//                    //重新设置月份
//                    wv_month.setAdapter(new NumericWheelAdapter(1, 12));
//                    //重新设置日
//                    setReDay(year_num, wv_month.getCurrentItem() + 1, 1, 31, list_big, list_little);
//                }

                if(mSelectChangeCallback != null) {
                    mSelectChangeCallback.onTimeSelectChanged();
                }
            }
        });


        // 添加"月"监听
        wv_month.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
//                int month_num = index + 1;

                resetDay();
//                if (startYear == endYear) {
//                    month_num = month_num + startMonth - 1;
//                    if (startMonth == endMonth) {
//                        //重新设置日
//                        setReDay(currentYear, month_num, startDay, endDay, list_big, list_little);
//                    } else if (startMonth == month_num) {
//
//                        //重新设置日
//                        setReDay(currentYear, month_num, startDay, 31, list_big, list_little);
//                    } else if (endMonth == month_num) {
//                        setReDay(currentYear, month_num, 1, endDay, list_big, list_little);
//                    } else {
//                        setReDay(currentYear, month_num, 1, 31, list_big, list_little);
//                    }
//                } else if (currentYear == startYear) {
//                    month_num = month_num + startMonth - 1;
//                    if (month_num == startMonth) {
//                        //重新设置日
//                        setReDay(currentYear, month_num, startDay, 31, list_big, list_little);
//                    } else {
//                        //重新设置日
//                        setReDay(currentYear, month_num, 1, 31, list_big, list_little);
//                    }
//
//                } else if (currentYear == endYear) {
//                    if (month_num == endMonth) {
//                        //重新设置日
//                        setReDay(currentYear, wv_month.getCurrentItem() + 1, 1, endDay, list_big, list_little);
//                    } else {
//                        setReDay(currentYear, wv_month.getCurrentItem() + 1, 1, 31, list_big, list_little);
//                    }
//
//                } else {
//                    //重新设置日
//                    setReDay(currentYear, month_num, 1, 31, list_big, list_little);
//                }

                if(mSelectChangeCallback != null) {
                    mSelectChangeCallback.onTimeSelectChanged();
                }
            }
        });

        if(isAccurate) {
            wv_day.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(int index) {
                    resetHour(currentHour());

                    if(mSelectChangeCallback != null) {
                        mSelectChangeCallback.onTimeSelectChanged();
                    }
                }
            });

            wv_hours.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(int index) {
                    resetMinute();

                    if(mSelectChangeCallback != null) {
                        mSelectChangeCallback.onTimeSelectChanged();
                    }
                }
            });
        } else {
            setChangedListener(wv_day);
            setChangedListener(wv_hours);
        }
        setChangedListener(wv_minutes);
        setChangedListener(wv_seconds);

        if(type.length != 6) {
            throw new IllegalArgumentException("type[] length is not 6");
        }
        wv_year.setVisibility(type[0] ? View.VISIBLE : View.GONE);
        wv_month.setVisibility(type[1] ? View.VISIBLE : View.GONE);
        wv_day.setVisibility(type[2] ? View.VISIBLE : View.GONE);
        wv_hours.setVisibility(type[3] ? View.VISIBLE : View.GONE);
        wv_minutes.setVisibility(type[4] ? View.VISIBLE : View.GONE);
        wv_seconds.setVisibility(type[5] ? View.VISIBLE : View.GONE);
        setContentTextSize();
    }

    private boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
    }

    private void resetMinute() {
        resetMinute(0);
    }

    private void resetMinute(int selectHour) {
        int currentYear = currentYear();
        int currentMonth = currentMonth();
        int currentDay = currentDay();
        int currentHour = currentHour();

        int m0, m1;
        m0 = DEFAULT_START_MINUTE;
        m1 = DEFAULT_END_MINUTE;

        if(currentYear == startYear && currentMonth == startMonth && currentDay == startDay
                && currentHour == startHour) {
            m0 = startMinute;
        }
        if(currentYear == endYear && currentMonth == endMonth && currentDay == endDay
                && currentHour == endHour) {
            m1 = endMinute;
        }

        wv_minutes.setAdapter(new NumericWheelAdapter(m0, m1));
        int index = wv_minutes.getAdapter().indexOf(selectHour);
        if(index >= 0 && index < wv_minutes.getItemsCount()) {
            wv_minutes.setCurrentItem(index);
        } else {
            index = wv_minutes.getCurrentItem();
            if(index >= 0 && index < wv_minutes.getItemsCount()) {
                wv_minutes.setCurrentItem(index);
            } else {
                wv_minutes.setCurrentItem(0);
            }
        }
        wv_minutes.onItemSelected();
    }


    private void resetHour() {
        resetHour(0);
    }

    private void resetHour(int selectHour) {
        int currentYear = currentYear();
        int currentMonth = currentMonth();
        int currentDay = currentDay();

        int hour0, hour1;
        hour0 = DEFAULT_START_HOUR;
        hour1 = DEFAULT_END_HOUR;

        if(currentYear == startYear && currentMonth == startMonth && currentDay == startDay) {
            hour0 = startHour;
        }
        if(currentYear == endYear && currentMonth == endMonth && currentDay == endDay) {
            hour1 = endHour;
        }

        wv_hours.setAdapter(new NumericWheelAdapter(hour0, hour1));
        int index = wv_hours.getAdapter().indexOf(selectHour);
        if(index >= 0 && index < wv_hours.getItemsCount()) {
            wv_hours.setCurrentItem(index);
        } else {
            index = wv_hours.getCurrentItem();
            if(index >= 0 && index < wv_hours.getItemsCount()) {
                wv_hours.setCurrentItem(index);
            } else {
                wv_hours.setCurrentItem(0);
            }
        }
        wv_hours.onItemSelected();
    }

    private int currentHour() {
        int index = wv_hours.getCurrentItem();
        int currentHour = index;
        if(currentYear() == startYear && currentMonth() == startMonth && currentDay() == startDay) {
            currentHour = index + startHour;
        }
        return currentHour;
    }

    private int currentDay() {
        int index = wv_day.getCurrentItem();
        int currentDay = index + 1;
        int currentYear = currentYear();
        int currentMonth = currentMonth();
        if(currentYear == startYear && currentMonth == startMonth) {
            currentDay = index + startDay;
        }
        return currentDay;
    }

    private List<Integer> smallMonths = new ArrayList<>(Arrays.asList(4, 6, 9, 11));

    private int currentMonth() {
        int month = wv_month.getCurrentItem();
        int currentYear = currentYear();
        if(currentYear == startYear) {
            month += startMonth;
        } else {
            month += 1;
        }
        return month;
    }

    private void resetDay() {
        resetDay(0);
    }

    private void resetDay(int selectDay) {
        int currentYear = currentYear();
        int currentMonth = currentMonth();

        int day0, day1;
        day0 = DEFAULT_START_DAY;
        day1 = DEFAULT_END_DAY;

        if(currentYear == startYear && currentMonth == startMonth) {
            day0 = startDay;
        }

        if(currentYear == endYear && currentMonth == endMonth) {
            day1 = endDay;
        }

        if(currentMonth == 2) {
            if(isLeapYear(currentYear)) {
                if(day1 > 29) {
                    day1 = 29;
                }
            } else {
                if(day1 > 28) {
                    day1 = 28;
                }
            }
        }

        if(smallMonths.contains(currentMonth) && day1 == 31) {
            day1 = 30;
        }

        wv_day.setAdapter(new NumericWheelAdapter(day0, day1));
        int index = wv_day.getAdapter().indexOf(selectDay);
        if(index >= 0 && index < wv_day.getItemsCount()) {
            wv_day.setCurrentItem(index);
        } else {
            index = wv_day.getCurrentItem();
            if(index >= 0 && index < wv_day.getItemsCount()) {
                wv_day.setCurrentItem(index);
            } else {
                wv_day.setCurrentItem(0);
            }
        }
        wv_day.onItemSelected();
    }

    private void resetMonth() {
        resetMonth(0);
    }

    private void resetMonth(int selectMonth) {
        int currentYear = currentYear();

        int month0, month1;
        month0 = startYear == currentYear ? startMonth : DEFAULT_START_MONTH;
        month1 = endYear == currentYear ? endMonth : DEFAULT_END_MONTH;
        wv_month.setAdapter(new NumericWheelAdapter(month0, month1));
        int index = wv_month.getAdapter().indexOf(selectMonth);
        if(index >= 0 && index < wv_month.getItemsCount()) {
            wv_month.setCurrentItem(index);
        } else {
            if(wv_month.getItemsCount() > 0) {
                index = wv_month.getCurrentItem();
                if(index >= 0 && index < wv_month.getItemsCount()) {
                    wv_month.setCurrentItem(index);
                } else {
                    wv_month.setCurrentItem(0);
                }
            }
        }
        wv_month.onItemSelected();
//        } else {
//
//        Log.e("tag", "year=" + currentYear + " " + month0 + " " + month1);
//        }
//        wv_month.setCurrentItem(currentIndex);
    }

    private int currentYear() {
        return wv_year.getCurrentItem() + startYear;
    }


    private void setChangedListener(CustomWheelView wheelView) {
        if(mSelectChangeCallback != null) {
            wheelView.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(int index) {
                    mSelectChangeCallback.onTimeSelectChanged();
                }
            });
        }

    }


    private void setReDay(int year_num, int monthNum, int startD, int endD, List<String> list_big, List<String> list_little) {
        int currentItem = wv_day.getCurrentItem();

//        int maxItem;
        if (list_big.contains(String.valueOf(monthNum))) {
            if (endD > 31) {
                endD = 31;
            }
            wv_day.setAdapter(new NumericWheelAdapter(startD, endD));
//            maxItem = endD;
        } else if (list_little.contains(String.valueOf(monthNum))) {
            if (endD > 30) {
                endD = 30;
            }
            wv_day.setAdapter(new NumericWheelAdapter(startD, endD));
//            maxItem = endD;
        } else {
            if ((year_num % 4 == 0 && year_num % 100 != 0)
                    || year_num % 400 == 0) {
                if (endD > 29) {
                    endD = 29;
                }
                wv_day.setAdapter(new NumericWheelAdapter(startD, endD));
//                maxItem = endD;
            } else {
                if (endD > 28) {
                    endD = 28;
                }
                wv_day.setAdapter(new NumericWheelAdapter(startD, endD));
//                maxItem = endD;
            }
        }

        if (currentItem > wv_day.getAdapter().getItemsCount() - 1) {
            currentItem = wv_day.getAdapter().getItemsCount() - 1;
            wv_day.setCurrentItem(currentItem);
        }
    }


    private void setContentTextSize() {
        wv_day.setTextSize(textSize);
        wv_month.setTextSize(textSize);
        wv_year.setTextSize(textSize);
        wv_hours.setTextSize(textSize);
        wv_minutes.setTextSize(textSize);
        wv_seconds.setTextSize(textSize);
    }


    public void setLabels(String label_year, String label_month, String label_day, String label_hours, String label_mins, String label_seconds) {
        if (isLunarCalendar) {
            return;
        }

        if (label_year != null) {
            wv_year.setLabel(label_year);
        } else {
            wv_year.setLabel(view.getContext().getString(R.string.pickerview_year));
        }
        if (label_month != null) {
            wv_month.setLabel(label_month);
        } else {
            wv_month.setLabel(view.getContext().getString(R.string.pickerview_month));
        }
        if (label_day != null) {
            wv_day.setLabel(label_day);
        } else {
            wv_day.setLabel(view.getContext().getString(R.string.pickerview_day));
        }
        if (label_hours != null) {
            wv_hours.setLabel(label_hours);
        } else {
            wv_hours.setLabel(view.getContext().getString(R.string.pickerview_hours));
        }
        if (label_mins != null) {
            wv_minutes.setLabel(label_mins);
        } else {
            wv_minutes.setLabel(view.getContext().getString(R.string.pickerview_minutes));
        }
        if (label_seconds != null) {
            wv_seconds.setLabel(label_seconds);
        } else {
            wv_seconds.setLabel(view.getContext().getString(R.string.pickerview_seconds));
        }

    }

    public void setTextXOffset(int x_offset_year, int x_offset_month, int x_offset_day,
                               int x_offset_hours, int x_offset_minutes, int x_offset_seconds) {
        wv_year.setTextXOffset(x_offset_year);
        wv_month.setTextXOffset(x_offset_month);
        wv_day.setTextXOffset(x_offset_day);
        wv_hours.setTextXOffset(x_offset_hours);
        wv_minutes.setTextXOffset(x_offset_minutes);
        wv_seconds.setTextXOffset(x_offset_seconds);
    }

    /**
     * 设置是否循环滚动
     *
     * @param cyclic
     */
    public void setCyclic(boolean cyclic) {
        wv_year.setCyclic(cyclic);
        wv_month.setCyclic(cyclic);
        wv_day.setCyclic(cyclic);
        wv_hours.setCyclic(cyclic);
        wv_minutes.setCyclic(cyclic);
        wv_seconds.setCyclic(cyclic);
    }

    public String getTime() {
        if (isLunarCalendar) {
            //如果是农历 返回对应的公历时间
            return getLunarTime();
        }
        StringBuilder sb = new StringBuilder();
        if(currentYear() == startYear) {
           /* int i = wv_month.getCurrentItem() + startMonth;
            System.out.println("i:" + i);*/
            if((wv_month.getCurrentItem() + startMonth) == startMonth) {
                sb.append((wv_year.getCurrentItem() + startYear)).append("-")
                  .append((wv_month.getCurrentItem() + startMonth)).append("-")
                  .append((wv_day.getCurrentItem() + startDay)).append(" ")
                  .append(wv_hours.getAdapter().getItem(wv_hours.getCurrentItem())).append(":")
                  .append(wv_minutes.getAdapter().getItem(wv_minutes.getCurrentItem())).append(":")
                  .append(wv_seconds.getCurrentItem());
            } else {
                sb.append((wv_year.getCurrentItem() + startYear)).append("-")
                  .append((wv_month.getCurrentItem() + startMonth)).append("-")
                  .append((wv_day.getCurrentItem() + 1)).append(" ")
                  .append(wv_hours.getAdapter().getItem(wv_hours.getCurrentItem())).append(":")
                  .append(wv_minutes.getAdapter().getItem(wv_minutes.getCurrentItem())).append(":")
                        .append(wv_seconds.getCurrentItem());
            }

        } else {
            sb.append((wv_year.getCurrentItem() + startYear)).append("-")
                    .append((wv_month.getCurrentItem() + 1)).append("-")
                    .append((wv_day.getCurrentItem() + 1)).append(" ")
                    .append(wv_hours.getAdapter().getItem(wv_hours.getCurrentItem())).append(":")
                    .append(wv_minutes.getAdapter().getItem(wv_minutes.getCurrentItem())).append(":")
                    .append(wv_seconds.getCurrentItem());
        }

        return sb.toString();
    }


    /**
     * 农历返回对应的公历时间
     *
     * @return
     */
    private String getLunarTime() {
        StringBuilder sb = new StringBuilder();
        int year = wv_year.getCurrentItem() + startYear;
        int month = 1;
        boolean isLeapMonth = false;
        if (ChinaDate.leapMonth(year) == 0) {
            month = wv_month.getCurrentItem() + 1;
        } else {
            if ((wv_month.getCurrentItem() + 1) - ChinaDate.leapMonth(year) <= 0) {
                month = wv_month.getCurrentItem() + 1;
            } else if ((wv_month.getCurrentItem() + 1) - ChinaDate.leapMonth(year) == 1) {
                month = wv_month.getCurrentItem();
                isLeapMonth = true;
            } else {
                month = wv_month.getCurrentItem();
            }
        }
        int day = wv_day.getCurrentItem() + 1;
        int[] solar = LunarCalendar.lunarToSolar(year, month, day, isLeapMonth);

        sb.append(solar[0]).append("-")
                .append(solar[1]).append("-")
                .append(solar[2]).append(" ")
                .append(wv_hours.getCurrentItem()).append(":")
                .append(wv_minutes.getCurrentItem()).append(":")
                .append(wv_seconds.getCurrentItem());
        return sb.toString();
    }

    public View getView() {
        return view;
    }

    public int getStartYear() {
        return startYear;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public int getEndYear() {
        return endYear;
    }

    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }


    public void setRangDate(Calendar startDate, Calendar endDate, boolean isAccurate) {
        this.isAccurate = isAccurate;

        if(startDate == null && endDate != null) {
            int year = endDate.get(Calendar.YEAR);
            int month = endDate.get(Calendar.MONTH) + 1;
            int day = endDate.get(Calendar.DAY_OF_MONTH);
            int hour = endDate.get(Calendar.HOUR_OF_DAY);
            int minute = endDate.get(Calendar.MINUTE);
            if(year > startYear) {
                this.endYear = year;
                this.endMonth = month;
                this.endDay = day;
                this.endHour = hour;
                this.endMinute = minute;
            } else if(year == startYear) {
                if(month > startMonth) {
                    this.endYear = year;
                    this.endMonth = month;
                    this.endDay = day;
                    this.endHour = hour;
                    this.endMinute = minute;
                } else if(month == startMonth) {
                    if(day > startDay) {
                        this.endYear = year;
                        this.endMonth = month;
                        this.endDay = day;
                        this.endHour = hour;
                        this.endMinute = minute;
                    } else if(day == startDay) {
                        if(hour > startHour) {
                            this.endYear = year;
                            this.endMonth = month;
                            this.endDay = day;
                            this.endHour = hour;
                            this.endMinute = minute;
                        } else if(hour == startHour) {
                            this.endYear = year;
                            this.endMonth = month;
                            this.endDay = day;
                            this.endHour = hour;
                            this.endMinute = minute;
                        }
                    }
                }
            }

        } else if (startDate != null && endDate == null) {
            int year = startDate.get(Calendar.YEAR);
            int month = startDate.get(Calendar.MONTH) + 1;
            int day = startDate.get(Calendar.DAY_OF_MONTH);
            int hour = startDate.get(Calendar.HOUR_OF_DAY);
            int minute = startDate.get(Calendar.MINUTE);
            if(year < endYear) {
                this.startMonth = month;
                this.startDay = day;
                this.startYear = year;
                this.startHour = hour;
                this.startMinute = minute;
            } else if(year == endYear) {
                if(month < endMonth) {
                    this.startMonth = month;
                    this.startDay = day;
                    this.startYear = year;
                    this.startHour = hour;
                    this.startMinute = minute;
                } else if(month == endMonth) {
                    if(day < endDay) {
                        this.startMonth = month;
                        this.startDay = day;
                        this.startYear = year;
                        this.startHour = hour;
                        this.startMinute = minute;
                    } else if(day == endDay) {
                        if(hour < endHour) {
                            this.startMonth = month;
                            this.startDay = day;
                            this.startYear = year;
                            this.startHour = hour;
                            this.startMinute = minute;
                        } else if(hour == endHour) {
                            this.startMonth = month;
                            this.startDay = day;
                            this.startYear = year;
                            this.startHour = hour;
                            this.startMinute = minute;
                        }
                    }
                }
            }

        } else if (startDate != null && endDate != null) {
            this.startYear = startDate.get(Calendar.YEAR);
            this.endYear = endDate.get(Calendar.YEAR);
            this.startMonth = startDate.get(Calendar.MONTH) + 1;
            this.endMonth = endDate.get(Calendar.MONTH) + 1;
            this.startDay = startDate.get(Calendar.DAY_OF_MONTH);
            this.endDay = endDate.get(Calendar.DAY_OF_MONTH);
            this.startHour = startDate.get(Calendar.HOUR_OF_DAY);
            this.endHour = endDate.get(Calendar.HOUR_OF_DAY);
            this.startMinute = startDate.get(Calendar.MINUTE);
            this.endMinute = endDate.get(Calendar.MINUTE);
        }

    }

    /**
     * 设置间距倍数,但是只能在1.0-4.0f之间
     *
     * @param lineSpacingMultiplier
     */
    public void setLineSpacingMultiplier(float lineSpacingMultiplier) {
        wv_day.setLineSpacingMultiplier(lineSpacingMultiplier);
        wv_month.setLineSpacingMultiplier(lineSpacingMultiplier);
        wv_year.setLineSpacingMultiplier(lineSpacingMultiplier);
        wv_hours.setLineSpacingMultiplier(lineSpacingMultiplier);
        wv_minutes.setLineSpacingMultiplier(lineSpacingMultiplier);
        wv_seconds.setLineSpacingMultiplier(lineSpacingMultiplier);
    }

    /**
     * 设置分割线的颜色
     *
     * @param dividerColor
     */
    public void setDividerColor(int dividerColor) {
        wv_day.setDividerColor(dividerColor);
        wv_month.setDividerColor(dividerColor);
        wv_year.setDividerColor(dividerColor);
        wv_hours.setDividerColor(dividerColor);
        wv_minutes.setDividerColor(dividerColor);
        wv_seconds.setDividerColor(dividerColor);
    }

    /**
     * 设置分割线的类型
     *
     * @param dividerType
     */
    public void setDividerType(CustomWheelView.DividerType dividerType) {
        wv_day.setDividerType(dividerType);
        wv_month.setDividerType(dividerType);
        wv_year.setDividerType(dividerType);
        wv_hours.setDividerType(dividerType);
        wv_minutes.setDividerType(dividerType);
        wv_seconds.setDividerType(dividerType);
    }

    /**
     * 设置分割线之间的文字的颜色
     *
     * @param textColorCenter
     */
    public void setTextColorCenter(int textColorCenter) {
        wv_day.setTextColorCenter(textColorCenter);
        wv_month.setTextColorCenter(textColorCenter);
        wv_year.setTextColorCenter(textColorCenter);
        wv_hours.setTextColorCenter(textColorCenter);
        wv_minutes.setTextColorCenter(textColorCenter);
        wv_seconds.setTextColorCenter(textColorCenter);
    }

    /**
     * 设置分割线以外文字的颜色
     *
     * @param textColorOut
     */
    public void setTextColorOut(int textColorOut) {
        wv_day.setTextColorOut(textColorOut);
        wv_month.setTextColorOut(textColorOut);
        wv_year.setTextColorOut(textColorOut);
        wv_hours.setTextColorOut(textColorOut);
        wv_minutes.setTextColorOut(textColorOut);
        wv_seconds.setTextColorOut(textColorOut);
    }

    /**
     * @param isCenterLabel 是否只显示中间选中项的
     */
    public void isCenterLabel(boolean isCenterLabel) {
        wv_day.isCenterLabel(isCenterLabel);
        wv_month.isCenterLabel(isCenterLabel);
        wv_year.isCenterLabel(isCenterLabel);
        wv_hours.isCenterLabel(isCenterLabel);
        wv_minutes.isCenterLabel(isCenterLabel);
        wv_seconds.isCenterLabel(isCenterLabel);
    }

    public void setSelectChangeCallback(ISelectTimeCallback mSelectChangeCallback) {
        this.mSelectChangeCallback = mSelectChangeCallback;
    }

    public void setItemsVisible(int itemsVisibleCount) {
        wv_day.setItemsVisibleCount(itemsVisibleCount);
        wv_month.setItemsVisibleCount(itemsVisibleCount);
        wv_year.setItemsVisibleCount(itemsVisibleCount);
        wv_hours.setItemsVisibleCount(itemsVisibleCount);
        wv_minutes.setItemsVisibleCount(itemsVisibleCount);
        wv_seconds.setItemsVisibleCount(itemsVisibleCount);
    }

    public void setAlphaGradient(boolean isAlphaGradient) {
        wv_day.setAlphaGradient(isAlphaGradient);
        wv_month.setAlphaGradient(isAlphaGradient);
        wv_year.setAlphaGradient(isAlphaGradient);
        wv_hours.setAlphaGradient(isAlphaGradient);
        wv_minutes.setAlphaGradient(isAlphaGradient);
        wv_seconds.setAlphaGradient(isAlphaGradient);
    }
}
