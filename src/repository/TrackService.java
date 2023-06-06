package repository;

import entities.Track;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class TrackService {
    private static TrackService instance = null;
    private final Connection connection;

    private TrackService() throws SQLException {
        this.connection = DriverManager.getConnection("jdbc:sqlite:data.sqlite");
    }

    public static TrackService getInstance() {
        if (instance == null) {
            try {
                instance = new TrackService();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return instance;
    }

    public List<Track> getAllTracks() {
        try {
            var statement = connection.prepareStatement("select * from `tracks`");
            var resultSet = statement.executeQuery();
            return parseTracks(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Track> getTracksByAlbum(int albumId) {
        try {
            var statement = connection.prepareStatement("select * from `tracks` where `album_id`=?");
            statement.setInt(1, albumId);
            var resultSet = statement.executeQuery();
            return parseTracks(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createTrack(Track track) {
        try {
            var statement = connection.prepareStatement(
                "insert into `tracks` (`number`, `title`, `duration`, `lyrics`, `album_id`) values (?, ?, ?, ?, ?)");
            statement.setInt(1, track.getNumber());
            statement.setString(2, track.getTitle());
            statement.setLong(3, track.getDuration().toSeconds());
            statement.setString(4, track.getLyrics());
            statement.setInt(5, track.getAlbum().getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateTrack(int id, Track newTrack) {
        try {
            var statement = connection.prepareStatement(
                "update `tracks` set `number`=?, `title`=?, `duration`=?, `lyrics`=?, `album_id`=? where `id`=?");
            statement.setInt(1, newTrack.getNumber());
            statement.setString(2, newTrack.getTitle());
            statement.setLong(3, newTrack.getDuration().toSeconds());
            statement.setString(4, newTrack.getLyrics());
            statement.setInt(5, newTrack.getAlbum().getId());
            statement.setInt(6, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteTrack(int id) {
        try {
            var statement = connection.prepareStatement("delete from `tracks` where `id`=?");
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Track> parseTracks(ResultSet resultSet) throws SQLException {
        var tracks = new ArrayList<Track>();
        while (resultSet.next()) {
            var album = AlbumService.getInstance().getAlbumById(resultSet.getInt("album_id"));

            var track = new Track(
                resultSet.getInt("id"),
                resultSet.getInt("number"),
                resultSet.getString("title"),
                Duration.ofSeconds(resultSet.getInt("duration")),
                album
            );
            tracks.add(track);
        }

        return tracks;
    }
}
