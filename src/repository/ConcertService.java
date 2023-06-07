package repository;

import entities.Concert;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ConcertService {
    private static ConcertService instance = null;
    private final Connection connection;

    private ConcertService() throws SQLException {
        this.connection = DriverManager.getConnection("jdbc:sqlite:data.sqlite");
    }

    public static ConcertService getInstance() {
        if (instance == null) {
            try {
                instance = new ConcertService();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return instance;
    }

    public List<Concert> getConcerts() {
        try {
            var statement = connection.prepareStatement("select * from `concerts`");
            var resultSet = statement.executeQuery();
            return parseConcerts(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Concert getConcertById(int id) {
        try {
            var statement = connection.prepareStatement("select * from `concerts` where `id`=?");
            statement.setInt(1, id);
            var resultSet = statement.executeQuery();
            var parsedConcerts = parseConcerts(resultSet);
            return parsedConcerts.isEmpty() ? null : parsedConcerts.get(0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createConcert(Concert concert) {
        try {
            var statement = connection.prepareStatement(
                "insert into `concerts` (`location`, `date`, `artist_id`) values (?, ?, ?)");
            statement.setString(1, concert.location());
            statement.setString(2, concert.date().format(DateTimeFormatter.ISO_LOCAL_DATE));
            statement.setInt(3, concert.artist().getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateConcert(int id, Concert newConcert) {
        try {
            var statement = connection.prepareStatement(
                "update `concerts` set `location`=?, `date`=?, `artist_id`=? where `id`=?");
            statement.setString(1, newConcert.location());
            statement.setString(2, newConcert.date().format(DateTimeFormatter.ISO_LOCAL_DATE));
            statement.setInt(3, newConcert.artist().getId());
            statement.setInt(4, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteConcert(int id) {
        try {
            var statement = connection.prepareStatement("delete from `concerts`where `id`=?");
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Concert> parseConcerts(ResultSet resultSet) throws SQLException {
        var concerts = new ArrayList<Concert>();
        while (resultSet.next()) {
            var concert = new Concert(
                resultSet.getInt("id"),
                resultSet.getString("location"),
                LocalDate.parse(resultSet.getString("date")),
                ArtistService.getInstance().getArtistById(resultSet.getInt("artist_id"))
            );
            concerts.add(concert);
        }

        return concerts;
    }
}
