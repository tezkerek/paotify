package repository;

import entities.Artist;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ArtistService {
    private static ArtistService instance = null;
    private final Connection connection;

    private ArtistService() throws SQLException {
        this.connection = DriverManager.getConnection("jdbc:sqlite:data.sqlite");
    }

    public static ArtistService getInstance() {
        if (instance == null) {
            try {
                instance = new ArtistService();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return instance;
    }

    public List<Artist> getArtists() {
        try {
            var statement = connection.prepareStatement("select * from `artists`");
            var resultSet = statement.executeQuery();
            return parseArtists(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Artist getArtistById(int id) {
        try {
            var statement = connection.prepareStatement("select * from `artists` where `id`=?");
            statement.setInt(1, id);
            var resultSet = statement.executeQuery();
            var parsedArtists = parseArtists(resultSet);
            return parsedArtists.isEmpty() ? null : parsedArtists.get(0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createArtist(Artist artist) {
        try {
            var statement = connection.prepareStatement("insert into `artists` (`name`) values (?)");
            statement.setString(1, artist.getName());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateArtist(int id, Artist newArtist) {
        try {
            var statement = connection.prepareStatement("update `artists` set `name`=? where `id`=?");
            statement.setString(1, newArtist.getName());
            statement.setInt(2, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteArtist(int id) {
        try {
            var statement = connection.prepareStatement("delete from `artists`where `id`=?");
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Artist> parseArtists(ResultSet resultSet) throws SQLException {
        var artists = new ArrayList<Artist>();
        while (resultSet.next()) {
            var artist = new Artist(resultSet.getInt("id"), resultSet.getString("name"));
            artists.add(artist);
        }

        return artists;
    }
}
