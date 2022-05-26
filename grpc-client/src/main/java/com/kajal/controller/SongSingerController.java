package com.kajal.controller;

import com.google.protobuf.Descriptors;
import com.kajal.service.SongSingerClientService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
public class SongSingerController {
    SongSingerClientService songSingerClientService;

    @GetMapping("/singer/{singerId}")
    public Map<Descriptors.FieldDescriptor, Object> getSinger(@PathVariable String singerId){
        return songSingerClientService.getSinger(Integer.parseInt(singerId));
    }

    @GetMapping("song/{singerId}")
    public List<Map<Descriptors.FieldDescriptor, Object>> getSongsBySinger(@PathVariable String singerId) throws InterruptedException {
        return songSingerClientService.getSongsBySinger(Integer.parseInt(singerId));
    }

    @GetMapping("/song")
    public Map<String, Map<Descriptors.FieldDescriptor, Object>> getLatestSong() throws InterruptedException {
        return songSingerClientService.getLatestSong();
    }
    @GetMapping("song/singer/{gender}")
    public List<Map<Descriptors.FieldDescriptor, Object>> getSongsBySingerGender(@PathVariable String gender) throws InterruptedException {
        return songSingerClientService.getSongsBySingerGender(gender);
    }
}
