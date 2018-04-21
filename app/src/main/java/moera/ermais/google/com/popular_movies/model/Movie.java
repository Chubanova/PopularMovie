package moera.ermais.google.com.popular_movies.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import moera.ermais.google.com.popular_movies.R;


public class Movie implements Parcelable {
    private String title;
    private int voteCount;
    private boolean hasVideo;
    private double voteAverage;
    private double popularity;
    private String poster;
    private byte[] posterByte;
    private String originalLanguage;
    private String originalTitle;
    private String backdrop;
    private boolean adult;
    private String overview;
    private String releaseDate;
    private int id;

    public Movie() {
    }

    public Movie(String title, int voteCount, double voteAverage, double popularity, String poster, String releaseDate, String overview) {
        this.title = title;
        this.voteCount = voteCount;
        this.voteAverage = voteAverage;
        this.popularity = popularity;
        this.poster = poster;
        this.posterByte = null;
        this.releaseDate = releaseDate;
        this.overview = overview;
    }

    public Movie(String title, int voteCount, double voteAverage, double popularity, byte[] poster, String releaseDate, String overview, int id) {
        this.title = title;
        this.voteCount = voteCount;
        this.voteAverage = voteAverage;
        this.popularity = popularity;
        this.poster = null;
        this.posterByte = poster;
        this.releaseDate = releaseDate;
        this.overview = overview;
        this.id = id;
    }

    //
    private Movie(Parcel in) {
        id = in.readInt();
        title = in.readString();
        voteCount = in.readInt();
        hasVideo = in.readByte() != 0;
        voteAverage = in.readDouble();
        popularity = in.readDouble();
        poster = in.readString();
        originalLanguage = in.readString();
        originalTitle = in.readString();
        backdrop = in.readString();
        adult = in.readByte() != 0;
        overview = in.readString();
        releaseDate = in.readString();
        int length = in.readInt();
        if (length != -1) {
            posterByte = new byte[length];
            in.readByteArray(posterByte);
        }
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public boolean isHasVideo() {
        return hasVideo;
    }

    public void setHasVideo(boolean hasVideo) {
        this.hasVideo = hasVideo;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public String getPoster() {
        return poster;
    }

    public byte[] getPosterByte() {
        return posterByte;
    }

    public void setPoster(Context context, String poster) {
        this.poster = context.getResources().getString(R.string.picture_link) + poster;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public void setBackdrop(String backdrop) {
        this.backdrop = backdrop;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeInt(voteCount);
        parcel.writeByte((byte) (hasVideo ? 1 : 0));
        parcel.writeDouble(voteAverage);
        parcel.writeDouble(popularity);
        parcel.writeString(poster);
        parcel.writeString(originalLanguage);
        parcel.writeString(originalTitle);
        parcel.writeString(backdrop);
        parcel.writeByte((byte) (adult ? 1 : 0));
        parcel.writeString(overview);
        parcel.writeString(releaseDate);
        parcel.writeInt(posterByte == null ? -1 : posterByte.length);
        parcel.writeByteArray(posterByte);
    }

    @Override
    public String toString() {
        return title + " : " + overview;
    }


}
