package entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Album {
    private final String title;
    private final LocalDate releaseDate;
    private final Genre genre;
    private final List<Track> tracks = new ArrayList<>();
    private final List<Review> reviews = new ArrayList<>();
    private final Artist artist;

    public Album(String title, LocalDate releaseDate, Genre genre, Artist artist) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.genre = genre;
        this.artist = artist;
    }

    public Album(String title, LocalDate releaseDate, Genre genre) {
        this(title, releaseDate, genre, null);
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public Genre getGenre() {
        return genre;
    }

    public List<Track> getTracks() {
        return Collections.unmodifiableList(tracks);
    }

    public void addTrack(Track track) {
        tracks.add(track);
    }

    public List<Review> getReviews() {
        return Collections.unmodifiableList(reviews);
    }

    public Optional<Float> getRating() {
        if (reviews.isEmpty()) {
            return Optional.empty();
        }

        float ratingSum = reviews.stream().reduce(0, (sum, review) -> sum + review.rating(), Integer::sum);
        return Optional.of(ratingSum / reviews.size());
    }

    public void addReview(Review review) {
        reviews.add(review);
    }

    public Artist getArtist() {
        return artist;
    }

    public String toString() {
        return String.format("%s (%d)", title, releaseDate.getYear());
    }
}
