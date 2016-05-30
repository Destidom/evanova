package com.tlabs.android.jeeves.views.character;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ViewSwitcher;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;
import com.tlabs.android.jeeves.views.R;
import com.tlabs.eve.api.character.CharacterCalendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class CharacterCalendarWidget extends FrameLayout {

    public static final int VIEW_CALENDAR = 0;
    public static final int VIEW_EVENTS = 1;

    public interface Listener {
        void onEventsSelected(final List<CharacterCalendar.Entry> entries);
    }

    private static class EventDecorator implements DayViewDecorator {
        private final Map<CalendarDay, List<CharacterCalendar.Entry>> entries = new HashMap<>();

        @Override
        public boolean shouldDecorate(CalendarDay calendarDay) {
            return entries.containsKey(calendarDay);
        }

        @Override
        public void decorate(DayViewFacade dayViewFacade) {
            dayViewFacade.addSpan(new DotSpan(10, Color.GREEN));
        }

        public void setCalendar(CharacterCalendar calendar) {
            this.entries.clear();
            if (null == calendar) {
                return;
            }

            for (CharacterCalendar.Entry e: calendar.getEntries()) {
                final CalendarDay day = toCalendarDay(e.getEventDate());
                List l = this.entries.get(day);
                if (null == l) {
                    l = new ArrayList<>();
                    this.entries.put(day, l);
                }
                l.add(e);
            }
        }

        private static CalendarDay toCalendarDay(final long time) {
            final Calendar c = Calendar.getInstance();
            c.setTimeInMillis(time);
            return CalendarDay.from(c);
        }
    }

    private EventDecorator decorator = new EventDecorator();

    private ViewSwitcher viewFlipper;
    private MaterialCalendarView calendarView;
    private CharacterCalendarEventListWidget listView;

    private Listener listener;

    public CharacterCalendarWidget(Context context) {
        super(context);
        init();
    }

    public CharacterCalendarWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CharacterCalendarWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setCalendar(final CharacterCalendar calendar) {
        this.decorator.setCalendar(calendar);
        this.calendarView.invalidateDecorators();
        this.listView.setItems(calendar.getEntries());
    }

    public void setView(final int view) {
        switch (view) {
            case VIEW_EVENTS:
                this.viewFlipper.setDisplayedChild(VIEW_EVENTS);
                break;
            default:
                this.viewFlipper.setDisplayedChild(VIEW_CALENDAR);
                break;
        }
    }

    private void init() {
        this.viewFlipper = (ViewSwitcher)inflate(getContext(), R.layout.f_character_calendar, null);
        addView(this.viewFlipper);

        this.calendarView = (MaterialCalendarView)this.viewFlipper.getChildAt(VIEW_CALENDAR);

        final Calendar minCalendar = GregorianCalendar.getInstance(TimeZone.getTimeZone("UTC"));
        minCalendar.set(Calendar.DAY_OF_MONTH, 0);

        final Calendar maxCalendar = GregorianCalendar.getInstance(TimeZone.getTimeZone("UTC"));
        maxCalendar.set(Calendar.YEAR, maxCalendar.get(Calendar.YEAR) + 1);

        this.calendarView.setMinimumDate(minCalendar);
        this.calendarView.setMaximumDate(maxCalendar);
        this.calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_SINGLE);
        this.calendarView.setOnDateChangedListener((widget, date, selected) -> {
            if (!selected) {
                return;
            }
            if (null == listener) {
                return;
            }
            if (!decorator.shouldDecorate(date)) {
                return;
            }
            listener.onEventsSelected(decorator.entries.get(date));
        });

        this.calendarView.addDecorator(this.decorator);

        this.listView = (CharacterCalendarEventListWidget)this.viewFlipper.getChildAt(VIEW_CALENDAR);
    }
}
