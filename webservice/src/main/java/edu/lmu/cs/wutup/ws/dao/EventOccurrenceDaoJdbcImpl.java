package edu.lmu.cs.wutup.ws.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import org.joda.time.DateTime;

import edu.lmu.cs.wutup.ws.exception.EventOccurrenceExistsException;
import edu.lmu.cs.wutup.ws.exception.NoSuchEventOccurrenceException;
import edu.lmu.cs.wutup.ws.model.EventOccurrence;

@Repository
public class EventOccurrenceDaoJdbcImpl implements EventOccurrenceDao {

    private static final String CREATE_SQL = "insert into eventOccurrence (id, location) values (?,?)";
    private static final String UPDATE_SQL = "update eventOccurrence set location=? where id=?";
    private static final String FIND_BY_ID_SQL = "select id, name from eventOccurrence where id=?";
    private static final String FIND_BY_LOCATION_SQL = "select id, location from eventOccurrence where location=? limit ? offset ?";
    private static final String FIND_BY_START_TIME_SQL = "select id, location from eventOccurrence where start=? limit ? offset ?";
    private static final String FIND_ALL_SQL = "select id, name from eventOccurrence limit ? offset ?";
    private static final String DELETE_SQL = "delete from eventOccurrence where id=?";
    private static final String COUNT_SQL = "select count(*) from eventOccurrence";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void createEventOccurrence(EventOccurrence e) {
        try {
            jdbcTemplate.update(CREATE_SQL, e.getId(), e.getLocation());
        } catch (DuplicateKeyException ex) {
            throw new EventOccurrenceExistsException();
        }
    }

    @Override
    public void updateEventOccurrence(EventOccurrence e) {
        int rowsUpdated = jdbcTemplate.update(UPDATE_SQL, e.getId());
        if (rowsUpdated == 0) {
            throw new NoSuchEventOccurrenceException();
        }
    }

    @Override
    public EventOccurrence findEventOccurrenceById(int id) {
        try {
            return jdbcTemplate.queryForObject(FIND_BY_ID_SQL, new Object[]{id},
                    eventOccurrenceRowMapper);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new NoSuchEventOccurrenceException();
        }
    }

    @Override
    public List<EventOccurrence> findEventOccurrencesByLocation(String location,
            int pageNumber, int pageSize) {
        return jdbcTemplate.query(FIND_BY_LOCATION_SQL, new Object[]{location,
                pageSize, pageNumber * pageSize}, eventOccurrenceRowMapper);
    }

    @Override
    public List<EventOccurrence> findEventOccurrencesByStartTime(DateTime start,
            int pageNumber, int pageSize) {
        return jdbcTemplate.query(FIND_BY_START_TIME_SQL, new Object[]{start,
                pageSize, pageNumber * pageSize}, eventOccurrenceRowMapper);
    }

    @Override
    public List<EventOccurrence> findAllEventOccurrences(int pageNumber, int pageSize) {
        return jdbcTemplate.query(FIND_ALL_SQL, new Object[]{pageSize,
                pageNumber * pageSize}, eventOccurrenceRowMapper);
    }

    @Override
    public void deleteEventOccurrence(EventOccurrence e) {
        int rowsUpdated = jdbcTemplate.update(DELETE_SQL, e.getId());
        if (rowsUpdated == 0) {
            throw new NoSuchEventOccurrenceException();
        }
    }

    @Override
    public int findNumberOfEventOccurrences() {
        return jdbcTemplate.queryForInt(COUNT_SQL);
    }

    private static RowMapper<EventOccurrence> eventOccurrenceRowMapper =
            new RowMapper<EventOccurrence>() {
        public EventOccurrence mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new EventOccurrence(rs.getInt("id"), rs.getString("location"));
        }
    };
}
