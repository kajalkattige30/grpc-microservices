package com.kajal.controller;

import com.google.protobuf.Descriptors;
import com.kajal.service.SongArtistClientService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
public class SongArtistController {
    SongArtistClientService songArtistClientService;

    @GetMapping("/artist/{artistId}")
    public Map<Descriptors.FieldDescriptor, Object> getArtist(@PathVariable String artistId){
        return songArtistClientService.getArtist(Integer.parseInt(artistId));
    }

    @GetMapping("song/{artistId}")
    public List<Map<Descriptors.FieldDescriptor, Object>> getSongsByArtist(@PathVariable String artistId) throws InterruptedException {
        return songArtistClientService.getSongsByArtist(Integer.parseInt(artistId));
    }

    @GetMapping("/song")
    public Map<String, Map<Descriptors.FieldDescriptor, Object>> getLatestSong() throws InterruptedException {
        return songArtistClientService.getLatestSong();
    }
    @GetMapping("song/artist/{gender}")
    public List<Map<Descriptors.FieldDescriptor, Object>> getSongsByArtistGender(@PathVariable String gender) throws InterruptedException {
        return songArtistClientService.getSongsByArtistGender(gender);
    }
}
