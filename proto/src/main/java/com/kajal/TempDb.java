package com.kajal;

import java.util.ArrayList;
import java.util.List;

public class TempDb {
    public static List<Artist> getArtistsFromTempDb() {
        return new ArrayList<Artist>() {
            {
                add(Artist.newBuilder().setArtistId(1).setSongId(1).setFirstName("Taylor").setLastName("Swift").setGender("Female").build());
                add(Artist.newBuilder().setArtistId(2).setFirstName("Justin").setLastName("Bieber").setGender("Male").build());
                add(Artist.newBuilder().setArtistId(3).setFirstName("Selena").setLastName("Gomez").setGender("Female").build());
                add(Artist.newBuilder().setArtistId(4).setFirstName("Ariana").setLastName("Grande").setGender("Female").build());
            }
        };
    }
    public static List<Song> getSongsFromTempDb() {
        return new ArrayList<>() {
            {
                add(Song.newBuilder().setSongId(1).setArtistId(1).setSongName("Willow").setYear(2020).setAlbum("evermore").build());
                add(Song.newBuilder().setSongId(2).setArtistId(1).setSongName("Blank space").setYear(2014).setAlbum("1989").build());
                add(Song.newBuilder().setSongId(3).setArtistId(2).setSongName("Peaches").setYear(2021).setAlbum("Justice").build());
                add(Song.newBuilder().setSongId(4).setArtistId(3).setSongName("Who says").setYear(2011).setAlbum("When the sun goes down").build());
                add(Song.newBuilder().setSongId(5).setArtistId(3).setSongName("Wolves").setYear(2017).setAlbum("Album").build());
                add(Song.newBuilder().setSongId(6).setArtistId(4).setSongName("Thank you, Next").setYear(2019).setAlbum("thank you, next").build());
            }
        };
    }
}
