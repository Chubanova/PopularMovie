package moera.ermais.google.com.popular_movies.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Машенька on 07.04.2018.
 */
//"author": "garethmb",
//        "content": "The long anticipated “Justice League” has finally arrived finally combining the biggest stars of the DC universe into one film. The proposed film has faced many obstacles on the way to the big screen ranging from script issues, massive reshoots and a change of Director for said reshoots due to a family tragedy that Director Zack Snyder suffered.\r\n\r\nThe film follows Batman (Ben Affleck) and Wonder Woman (Gal Godot), as they look to assemble a team of other gifted individuals to help fight off a pending invasion.  \r\n\r\nThe death of Superman has left a void on the Earth, and this has paved the way for an ancient evil to return as he attempts to conquer the planet after he collects the three needed artifacts that his plan requires.\r\n\r\nIn a race against time, Batman and Wonder Woman recruit Aquaman (Jason Momoa), The Flash (Ezra Miller), and Cyborg (Ray Fisher), to battle to save the planet. Naturally they battle amongst themselves as well as their massing enemies but ultimately decide on a dangerous plan that can tip the odds in their favor and save the day.\r\n\r\nThe action in the film is good but it often plays out like a video game. With so many blatantly obvious CGI backgrounds, the movie looked like a video game. There were numerous scenes that looked like they were lifted from Injustice and Injustice 2 that I mused to myself that someone must have used their Power Up for the shot.\r\n\r\nAffleck and Gadot are good and work well with one another, but there are some serious casting issues with the film. The biggest for me was Ezra Miller as The Flash. I did not like his effeminate, nerdy, socially awkward, and neurotic and at times cowardly take on the character. This is not the Barry Allen I grew up reading in comics or the one that has been portrayed twice in a much better fashion on television. His comic relief status grew old fast and his character really offered little to the film.\r\n\r\nIt has been well-documented that Joss Whedon not only handled the rewrites for the film but took over directing duties to complete the film. You can see elements of his humor scattered throughout and the film does move along at a steady pace without dragging.\r\n\r\nThe biggest issue is that so many of the characters are just stiff and one-dimensional. They really are not overly interesting so it is hard to really connect with them and the tasks they are facing. Unlike Marvel who have excelled with dysfunctional groups who fight amongst themselves as well as the forces of evil, this group seems to be going through the paces rather than being fully engaged with the task at hand and each other.\r\n\r\nIn the end “Justice League” is better than I expected, and the two bonus scenes in the credits show some interesting potential down the road.  As it is, it is flawed entertainment that requires audiences to overlook a lot of issues.\r\n\r\n3 stars out of 5",
//        "id": "5a0c5d3e9251414d94012bfd",
//        "url": "https://www.themoviedb.org/review/5a0c5d3e9251414d94012bfd"
public class Review implements Parcelable {
    private  String author;
    private String content;
    private String id;
    private String url;

    public Review() {
    }

    protected Review(Parcel in) {
        author = in.readString();
        content = in.readString();
        id = in.readString();
        url = in.readString();
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(author);
        parcel.writeString(content);
        parcel.writeString(id);
        parcel.writeString(url);
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
