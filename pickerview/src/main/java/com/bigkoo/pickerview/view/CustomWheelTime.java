package com.bigkoo.pickerview.view;

import android.util.Log;
import android.view.View;

import com.bigkoo.pickerview.adapter.NumericWheelAdapter;
import com.contrarywind.listener.OnItemSelectedListener;

import java.util.Calendar;


public class CustomWheelTime extends WheelTime {

    private boolean isAccurate;

    public CustomWheelTime(View view, boolean[] type, int gravity, int textSize) {
        super(view, type, gravity, textSize);
    }

    public void resetTime() {
        resetYear();
        resetMonth();
        resetDay();
        resetHour();
        resetMinute();
        Log.e("tag", "resetTime");
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
    @Override
    protected void setSolar(int year, final int month, int day, int h, int m, int s) {
        Log.w("tag", "setSolar " + year + " " + month + " " + day + " " + h + " " + m + " " + s);
        setGravity();

        selectYear(year);
        selectMonth(month + 1);
        selectDay(day);
        selectHour(h);
        selectMinute(m);

        // 添加"年"监听
        wv_year.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                int selectMonth = currentMonth();
                resetMonth();
                selectMonth(selectMonth);

                if(mSelectChangeCallback != null) {
                    mSelectChangeCallback.onTimeSelectChanged();
                }
            }
        });


        // 添加"月"监听
        wv_month.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                int currentDay = currentDay();
                resetDay();
                selectDay(currentDay);

                if(mSelectChangeCallback != null) {
                    mSelectChangeCallback.onTimeSelectChanged();
                }
            }
        });

        if(isAccurate) {
            wv_day.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(int index) {
                    int currentHour = currentHour();
                    resetHour();
                    selectHour(currentHour);

                    if(mSelectChangeCallback != null) {
                        mSelectChangeCallback.onTimeSelectChanged();
                    }
                }
            });

            wv_hours.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(int index) {
                    int currentMinute = currentMinute();
                    resetMinute();
                    selectMinute(currentMinute);

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

    private void selectYear(int year) {
        wv_year.setCurrentItem(year - startYear); // 初始化时显示的数据
    }

    private void setGravity() {
        wv_year.setGravity(gravity);
        wv_month.setGravity(gravity);
        wv_day.setGravity(gravity);
        wv_hours.setGravity(gravity);
        wv_minutes.setGravity(gravity);
        wv_seconds.setGravity(gravity);
    }

    private void resetYear() {
        wv_year.setAdapter(new NumericWheelAdapter(startYear, endYear));// 设置"年"的显示数据
    }


    private void resetMinute() {
        if(isAccurate) {
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
        } else {
            wv_minutes
                    .setAdapter(new NumericWheelAdapter(DEFAULT_START_MINUTE, DEFAULT_END_MINUTE));
        }
    }

    private void selectMinute(int selectHour) {
        int index = wv_minutes.getAdapter().indexOfValue(selectHour);
        if(index >= 0 && index < wv_minutes.getItemsCount()) {
            wv_minutes.setCurrentItem(index);
        } else {
//            index = wv_minutes.getCurrentItem();
//            if(index >= 0 && index < wv_minutes.getItemsCount()) {
//                wv_minutes.setCurrentItem(index);
//            } else {
                wv_minutes.setCurrentItem(0);
//            }
        }
        wv_minutes.onItemSelected();
    }


    private void resetHour() {
        if(isAccurate) {
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
        } else {
            wv_hours.setAdapter(new NumericWheelAdapter(DEFAULT_START_HOUR, DEFAULT_END_HOUR));
        }
    }

    private void selectHour(int selectHour) {
        int index = wv_hours.getAdapter().indexOfValue(selectHour);
        if(index >= 0 && index < wv_hours.getItemsCount()) {
            wv_hours.setCurrentItem(index);
        } else {
//            index = wv_hours.getCurrentItem();
//            if(index >= 0 && index < wv_hours.getItemsCount()) {
//                wv_hours.setCurrentItem(index);
//            } else {
                wv_hours.setCurrentItem(0);
//            }
        }
        wv_hours.onItemSelected();
    }

    private int currentMinute() {
        return (int) wv_minutes.getAdapter().getItem(wv_minutes.getCurrentItem());
    }
    private int currentHour() {
        return (int) wv_hours.getAdapter().getItem(wv_hours.getCurrentItem());
//        int index = wv_hours.getCurrentItem();
//        int currentHour = index;
//        if(currentYear() == startYear && currentMonth() == startMonth && currentDay() == startDay) {
//            currentHour = index + startHour;
//        }
//        return currentHour;
    }

    private int currentDay() {
        return (int) wv_day.getAdapter().getItem(wv_day.getCurrentItem());
//        int index = wv_day.getCurrentItem();
//        int currentDay = index + 1;
//        int currentYear = currentYear();
//        int currentMonth = currentMonth();
//        if(currentYear == startYear && currentMonth == startMonth) {
//            currentDay = index + startDay;
//        }
//        return currentDay;
    }


    private int currentMonth() {
        return (int) wv_month.getAdapter().getItem(wv_month.getCurrentItem());
//        int month = wv_month.getCurrentItem();
//        int currentYear = currentYear();
//        if(currentYear == startYear) {
//            month += startMonth;
//        } else {
//            month += 1;
//        }
//        return month;
    }

    private void resetDay() {
        int currentYear = currentYear();
        int currentMonth = currentMonth();

        int day0, day1;
        day0 = DEFAULT_START_DAY;
        day1 = DEFAULT_END_DAY;

        if(smallMonth.contains(currentMonth)) {
            day1 = 30;
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

        if(currentYear == startYear && currentMonth == startMonth) {
            day0 = startDay;
        }

        if(currentYear == endYear && currentMonth == endMonth) {
            day1 = endDay;
        }


        wv_day.setAdapter(new NumericWheelAdapter(day0, day1));
    }

    private void selectDay(int selectDay) {
        int index = wv_day.getAdapter().indexOfValue(selectDay);
        if(index >= 0 && index < wv_day.getItemsCount()) {
            wv_day.setCurrentItem(index);
        } else {
//            index = wv_day.getCurrentItem();
//            if(index >= 0 && index < wv_day.getItemsCount()) {
//                wv_day.setCurrentItem(index);
//            } else {
                wv_day.setCurrentItem(0);
//            }
        }
        wv_day.onItemSelected();
    }

    private void resetMonth() {
        int month0, month1;
        int currentYear = currentYear();
        month0 = startYear == currentYear ? startMonth : DEFAULT_START_MONTH;
        month1 = endYear == currentYear ? endMonth : DEFAULT_END_MONTH;

        wv_month.setAdapter(new NumericWheelAdapter(month0, month1));
    }

    private void selectMonth(int selectMonth) {
        int index = wv_month.getAdapter().indexOfValue(selectMonth);
        if(index >= 0 && index < wv_month.getItemsCount()) {
            wv_month.setCurrentItem(index);
        } else {
//            if(wv_month.getItemsCount() > 0) {
//                index = wv_month.getCurrentItem();
//                if(index >= 0 && index < wv_month.getItemsCount()) {
//                    wv_month.setCurrentItem(index);
//                } else {
                    wv_month.setCurrentItem(0);
//                }
//            }
        }
        wv_month.onItemSelected();
    }

    private int currentYear() {
        return wv_year.getCurrentItem() + startYear;
    }


    @Override
    public String getTime() {
        if(isLunarCalendar) {
            //如果是农历 返回对应的公历时间
            return getLunarTime();
        }
        StringBuilder sb = new StringBuilder();

        sb.append((wv_year.getCurrentItem() + startYear)).append("-")
          .append(currentMonth()).append("-")
          .append(currentDay()).append(" ")
          .append(wv_hours.getAdapter().getItem(wv_hours.getCurrentItem())).append(":")
          .append(wv_minutes.getAdapter().getItem(wv_minutes.getCurrentItem())).append(":")
          .append(wv_seconds.getCurrentItem());

        return sb.toString();
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

        } else if(startDate != null && endDate == null) {
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

        } else if(startDate != null && endDate != null) {
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


    enum TimeType {
        YEAR,
        MONTH,
        DAY,
        HOUR,
        MINUTE,
        SECOND
    }
}
