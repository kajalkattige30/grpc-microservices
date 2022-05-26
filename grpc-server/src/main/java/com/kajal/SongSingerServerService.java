package com.kajal;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.ArrayList;
import java.util.List;

@GrpcService
public class SongSingerServerService extends SongSingerServiceGrpc.SongSingerServiceImplBase{
    @Override
    public void getSinger(Singer request, StreamObserver<Singer> responseObserver) {
        TempDb.getSingersFromTempDb().
                stream().
                filter(singer -> singer.getSingerId()==request.getSingerId()).
                findFirst().ifPresent(responseObserver::onNext);
        responseObserver.onCompleted();
    }

    @Override
    public void getSongsBySinger(Singer request, StreamObserver<Song> responseObserver) {
        TempDb.getSongsFromTempDb()
                .stream()
                .filter(song -> song.getSingerId() == request.getSingerId())
                .forEach(responseObserver::onNext);
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<Song> getLatestSong(StreamObserver<Song> responseObserver) {
        return new StreamObserver<Song>() {
            Song LatestSong = null;
            int SongYear = 0;
            @Override
            public void onNext(Song song) {
                if(song.getYear() > SongYear) {
                    SongYear = song.getYear();
                    LatestSong = song;
                }
            }

            @Override
            public void onError(Throwable throwable) {
                responseObserver.onError(throwable);
            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(LatestSong);
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public StreamObserver<Song> getSongBySingerGender(StreamObserver<Song> responseObserver) {
        return new StreamObserver<Song>() {
            List<Song> songList = new ArrayList<>();
            @Override
            public void onNext(Song song) {
                TempDb.getSongsFromTempDb()
                        .stream()
                        .filter(songsFromDb -> song.getSingerId() == songsFromDb.getSingerId())
                        .forEach(songList::add);
            }

            @Override
            public void onError(Throwable t) {
                responseObserver.onError(t);
            }

            @Override
            public void onCompleted() {
                songList.forEach(responseObserver::onNext);
                responseObserver.onCompleted();
            }
        };
    }
}
