package repository;

import entities.Album;
import entities.Genre;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AlbumService {
    private static AlbumService instance = null;
    private final Connection connection;

    private AlbumService() throws SQLException {
        this.connection = DriverManager.getConnection("jdbc:sqlite:data.sqlite");
    }

    public static AlbumService getInstance() {
        if (instance == null) {
            try {
                instance = new AlbumService();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return instance;
    }

    public List<Album> getAlbums() {
        try {
            var statement = connection.prepareStatement("select * from `albums`");
            var resultSet = statement.executeQuery();
            return parseAlbums(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createAlbum(Album album) {
        try {
            var statement = connection.prepareStatement("insert into `albums` (`title`, `release_date`, `genre`, `artist_id`) values (?, ?, ?, ?)");
            statement.setString(1, album.getTitle());
            statement.setString(2, album.getReleaseDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
            statement.setString(3, album.getGenre().name());
            statement.setInt(4, album.getArtist().getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateAlbum(int id, Album newAlbum) {
        try {
            var statement = connection.prepareStatement("update `albums` set `title`=?, `release_date`=?, `genre`=?, `artist_id`=? where `id`=?");
            statement.setString(1, newAlbum.getTitle());
            statement.setString(2, newAlbum.getReleaseDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
            statement.setString(3, newAlbum.getGenre().name());
            statement.setInt(4, newAlbum.getArtist().getId());
            statement.setInt(5, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteAlbum(int id) {
        try {
            var statement = connection.prepareStatement("delete from `albums`where `id`=?");
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Album> parseAlbums(ResultSet resultSet) throws SQLException {
        var albums = new ArrayList<Album>();
        while (resultSet.next()) {
            var artist = ArtistService.getInstance().getArtistById(resultSet.getInt("artist_id"));
            var releaseDate = LocalDate.parse(resultSet.getString("release_date"));
            var genre = Genre.valueOf(resultSet.getString("genre"));

            var album = new Album(resultSet.getInt("id"), resultSet.getString("title"), releaseDate, genre, artist);
            albums.add(album);
        }

        return albums;
    }
}
