package com.karhacter.movies_webapp.payloads;

public class SaveWatchedMovieRequest {
    private Long userId;
    private Long movieId;
    private int watchProgress;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public int getWatchProgress() {
        return watchProgress;
    }

    public void setWatchProgress(int watchProgress) {
        this.watchProgress = watchProgress;
    }
}
