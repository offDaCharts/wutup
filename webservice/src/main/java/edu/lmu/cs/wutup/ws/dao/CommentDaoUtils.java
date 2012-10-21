package edu.lmu.cs.wutup.ws.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import edu.lmu.cs.wutup.ws.exception.CommentExistsException;
import edu.lmu.cs.wutup.ws.exception.NoSuchCommentException;
import edu.lmu.cs.wutup.ws.model.Comment;
import edu.lmu.cs.wutup.ws.model.User;

public class CommentDaoUtils {

    public static void addComment(JdbcTemplate jdbcTemplate, String object, Integer objectId, Comment comment) {
        int rowsUpdated = jdbcTemplate.update("insert into " + object
                + "_comments (eventid, authorid, text, timestamp) values (?,?,?,?)", objectId, comment.getAuthor()
                .getId(), comment.getBody(), comment.getTimestamp());
        if (rowsUpdated == 0) {
            throw new CommentExistsException();
        }

    }

    public static void updateComment(JdbcTemplate jdbcTemplate, String object, int commentId, Comment c) {
        int rowsUpdated = jdbcTemplate.update("update " + object + "_comments set text=?, timestamp=? where id=?",
                c.getBody(), c.getTimestamp(), c.getId());
        if (rowsUpdated == 0) {
            throw new NoSuchCommentException();
        }
    }

    public static void deleteComment(JdbcTemplate jdbcTemplate, String object, int objectId, int commentId) {
        int rowsUpdated = jdbcTemplate.update("delete from " + object + "_comments where eventId=? and id=?", objectId,
                commentId);
        if (rowsUpdated == 0) {
            throw new NoSuchCommentException();
        }
    }

    public static List<Comment> findCommentableObjectComments(JdbcTemplate jdbcTemplate, String SQL_STRING,
            int objectId, int pageSize, int pageNumber) {
        return jdbcTemplate.query(SQL_STRING, new Object[]{objectId, pageSize, pageNumber * pageSize}, commentRowMapper);
    }

    public static RowMapper<Comment> commentRowMapper = new RowMapper<Comment>() {
        public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
            int commentId = rs.getInt("id");
            String text = rs.getString("text");
            Timestamp persistedTimestamp = rs.getTimestamp("timestamp");
            DateTime timestamp = persistedTimestamp == null ? null : new DateTime(persistedTimestamp);
            return new Comment(commentId, text, timestamp, new User(rs.getInt("authorid"), rs.getString("firstName"),
                    rs.getString("lastName"), rs.getString("email"), rs.getString("nickname")));
        }
    };
}