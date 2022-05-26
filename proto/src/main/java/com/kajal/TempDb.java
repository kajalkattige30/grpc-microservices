package com.kajal;

import java.util.ArrayList;
import java.util.List;

public class TempDb {
    public static List<Singer> getSingersFromTempDb() {
        return new ArrayList<Singer>() {
            {
                add(Singer.newBuilder().setSingerId(1).setSongId(1).setFirstName("Taylor").setLastName("Swift").setGender("Female").build());
                add(Singer.newBuilder().setSingerId(2).setFirstName("Justin").setLastName("Bieber").setGender("Male").build());
                add(Singer.newBuilder().setSingerId(3).setFirstName("Selena").setLastName("Gomez").setGender("Female").build());
                add(Singer.newBuilder().setSingerId(4).setFirstName("Ariana").setLastName("Grande").setGender("Female").build());
            }
        };
    }
    public static List<Song> getSongsFromTempDb() {
        return new ArrayList<>() {
            {
                add(Song.newBuilder().setSongId(1).setSingerId(1).setTitle("Willow").setYear(2020).setAlbum("evermore").build());
                add(Song.newBuilder().setSongId(2).setSingerId(1).setTitle("Blank space").setYear(2014).setAlbum("1989").build());
                add(Song.newBuilder().setSongId(3).setSingerId(2).setTitle("Peaches").setYear(2021).setAlbum("Justice").build());
                add(Song.newBuilder().setSongId(4).setSingerId(3).setTitle("Who says").setYear(2011).setAlbum("When the sun goes down").build());
                add(Song.newBuilder().setSongId(5).setSingerId(3).setTitle("Wolves").setYear(2017).setAlbum("Album").build());
                add(Song.newBuilder().setSongId(6).setSingerId(4).setTitle("Thank you, Next").setYear(2019).setAlbum("thank you, next").build());
            }
        };
    }
}
