package com.kajal;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.ArrayList;
import java.util.List;

// annotate with grpc service
@GrpcService
public class SongArtistServerService extends SongArtistServiceGrpc.SongArtistServiceImplBase{
    @Override
    public void getArtist(Artist request, StreamObserver<Artist> responseObserver) {
        TempDb.getArtistsFromTempDb().
                stream().
                filter(artist -> artist.getArtistId()==request.getArtistId()).//filtering it with author id got from request
                findFirst().ifPresent(responseObserver::onNext); // send the response back to the client
        responseObserver.onCompleted();
    }

    @Override
    public void getSongsByArtist(Artist request, StreamObserver<Song> responseObserver) {
        TempDb.getSongsFromTempDb()
                .stream()
                .filter(song -> song.getArtistId() == request.getArtistId())
                .forEach(responseObserver::onNext); //streaming each song to the client
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
    public StreamObserver<Song> getSongByArtistGender(StreamObserver<Song> responseObserver) {
        return new StreamObserver<Song>() {
            List<Song> songList = new ArrayList<>();
            @Override
            public void onNext(Song song) {
                TempDb.getSongsFromTempDb()
                        .stream()
                        .filter(songsFromDb -> song.getArtistId() == songsFromDb.getArtistId())
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
