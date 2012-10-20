package edu.lmu.cs.wutup.ws.model;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertFalse;

import org.joda.time.DateTime;
import org.junit.Test;

public class EventOccurrenceTest {

    @Test
    public void fieldsSetByConstructorCanBeRead() {
        DateTime start = new DateTime(2012, 9, 27, 10, 50, 0);
        DateTime end = new DateTime(2012, 9, 27, 12, 5, 0);
        EventOccurrence o = new EventOccurrence(3, new Venue(3, "", "", 15.0, 15.0, null), start, end);
        assertThat(o.getId(), is(3));
        assertThat(o.getVenue().toString(),
                is("Venue{id=3, address=, latitude=15.0, longitude=15.0, propertyMap=null}"));
        assertThat(o.getStart(), is(start));
        assertThat(o.getEnd(), is(end));
    }

    @Test
    public void fieldsSetBySettersCanBeRead() {
        EventOccurrence o = new EventOccurrence();
        DateTime start = new DateTime(2012, 9, 27, 9, 25, 0);
        DateTime end = new DateTime(2012, 9, 27, 10, 40, 0);
        o.setId(5);
        o.setVenue(new Venue(o.getId(), "", "", 0.0, 100.0, null));
        o.setStart(start);
        o.setEnd(end);
        assertThat(o.getId(), is(5));
        assertThat(o.getVenue().toString(),
                is("Venue{id=5, address=, latitude=0.0, longitude=100.0, propertyMap=null}"));
        assertThat(o.getStart(), is(start));
        assertThat(o.getEnd(), is(end));
    }

    @Test
    public void toStringProducesExpectedString() {
        DateTime start = new DateTime(2012, 9, 27, 9, 25, 0);
        DateTime end = new DateTime(2012, 9, 27, 10, 40, 0);
        EventOccurrence occurrence1 = new EventOccurrence(3, new Venue(3, "", "", 33.969369, -118.414386, null), start,
                end);
        String expected1 = "EventOccurrence{id=3, location=Venue{id=3, address=, "
                + "latitude=33.969369, longitude=-118.414386, propertyMap=null}, "
                + "start=2012-09-27T09:25:00.000-07:00, end=2012-09-27T10:40:00.000-07:00}";
        EventOccurrence occurrence2 = new EventOccurrence();
        occurrence2.setId(5);
        occurrence2.setStart(start);
        occurrence2.setEnd(end);
        String expected2 = "EventOccurrence{id=5, location=null, "
                + "start=2012-09-27T09:25:00.000-07:00, end=2012-09-27T10:40:00.000-07:00}";
        assertThat(occurrence1.toString(), is(expected1));
        assertThat(occurrence2.toString(), is(expected2));
    }

    @Test
    public void hashCodeProducesId() {
        assertThat(new EventOccurrence(7, new Venue(), new DateTime(), new DateTime()).hashCode(), is(7));
    }

    @Test
    public void equalsUsesIdOnly() {
        DateTime start1 = new DateTime(2011, 12, 25, 0, 0, 0);
        DateTime end1 = new DateTime(2011, 12, 25, 11, 59, 59);
        DateTime start2 = new DateTime(2011, 12, 25, 0, 0, 0);
        DateTime end2 = new DateTime(2011, 12, 25, 11, 59, 59);
        assertThat(new EventOccurrence(7, new Venue(), start1, end1), equalTo(new EventOccurrence(7, new Venue(),
                start2, end2)));
        assertThat(new EventOccurrence(7, new Venue(10, "", "", 5.0, 5.0, null), start1, end1),
                not(equalTo(new EventOccurrence(17, new Venue(10, "", "", 5.0, 5.0, null), new DateTime(),
                        new DateTime()))));
        assertThat(new EventOccurrence(7, new Venue(10, "", "", 5.0, 5.0, null), new DateTime(), new DateTime()),
                equalTo(new EventOccurrence(7, new Venue(8, "", "", 15.0, 5.0, null), new DateTime(), new DateTime())));
        assertFalse(new EventOccurrence(7, new Venue(10, "", "", 5.0, 5.0, null), new DateTime(), new DateTime()).equals("some string"));
        assertFalse(new EventOccurrence(7, new Venue(10, "", "", 5.0, 5.0, null), new DateTime(), new DateTime()).equals(null));
    }

}
