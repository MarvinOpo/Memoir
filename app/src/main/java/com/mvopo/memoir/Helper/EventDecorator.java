package com.mvopo.memoir.Helper;

import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.List;

public class EventDecorator implements DayViewDecorator {

    private List<String> markedDays;

    private Drawable drawable;

    public EventDecorator(Drawable drawable, List<String> markedDays) {
        this.markedDays = markedDays;
        this.drawable = drawable;

    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return markedDays.contains(day.getDate().toString());
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setBackgroundDrawable(drawable);
    }

}
