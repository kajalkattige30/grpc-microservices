package com.kajal.service;

import com.google.protobuf.Descriptors;
import com.google.rpc.context.AttributeContext;
import com.kajal.Artist;
import com.kajal.Song;
import com.kajal.SongArtistServiceGrpc;
import com.kajal.TempDb;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service
public class SongArtistClientService {

    // create a synchronous client as a blocking stub to make call
    @GrpcClient("grpc-service")
    SongArtistServiceGrpc.SongArtistServiceBlockingStub synchronousClient;

    @GrpcClient("grpc-service")
    SongArtistServiceGrpc.SongArtistServiceStub asynchronousClient;

    public Map<Descriptors.FieldDescriptor, Object> getArtist(int artistId) {
        Artist artistRequest = Artist.newBuilder().setArtistId(artistId).build();
        Artist artistResponse = synchronousClient.getArtist(artistRequest); // making inter-service communication call to the server using synchronous client
        return artistResponse.getAllFields();
    }

    public List<Map<Descriptors.FieldDescriptor, Object>> getSongsByArtist(int artistId) throws InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        // creating a new artist object
        Artist artistRequest = Artist.newBuilder().setArtistId(artistId).build();
        final List<Map<Descriptors.FieldDescriptor, Object>> response = new ArrayList<>();
        asynchronousClient.getSongsByArtist(artistRequest, new StreamObserver<Song>() {
            @Override
            public void onNext(Song song) {
                response.add(song.getAllFields());
            }

            @Override
            public void onError(Throwable t) {
                countDownLatch.countDown();
            }

            @Override
            public void onCompleted() {
                countDownLatch.countDown();
            }
        });
        boolean await = countDownLatch.await(1, TimeUnit.MINUTES);
        return await ? response : Collections.emptyList();
    }

    public Map<String, Map<Descriptors.FieldDescriptor, Object>> getLatestSong() throws InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final Map<String, Map<Descriptors.FieldDescriptor, Object>> response = new HashMap<>();
        StreamObserver<Song> responseObserver = asynchronousClient.getLatestSong(new StreamObserver<Song>() {
            @Override
            public void onNext(Song song) {
                response.put("Latest Song", song.getAllFields());
            }

            @Override
            public void onError(Throwable t) {
                countDownLatch.countDown();
            }

            @Override
            public void onCompleted() {
                countDownLatch.countDown();
            }
        });
        // client is streaming multiple songs to the server
        TempDb.getSongsFromTempDb().forEach(responseObserver::onNext);
        responseObserver.onCompleted();
        boolean await = countDownLatch.await(1, TimeUnit.MINUTES);
        return await ? response : Collections.emptyMap();
    }

    public List<Map<Descriptors.FieldDescriptor, Object>> getSongsByArtistGender(String gender) throws InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final List<Map<Descriptors.FieldDescriptor, Object>> response = new ArrayList<>();
        StreamObserver<Song> responseObserver = asynchronousClient.getSongByArtistGender(new StreamObserver<Song>() {
            @Override
            public void onNext(Song song) {
                response.add(song.getAllFields());
            }

            @Override
            public void onError(Throwable t) {
                countDownLatch.countDown();
            }

            @Override
            public void onCompleted() {
                countDownLatch.countDown();
            }
        });
        TempDb.getArtistsFromTempDb()
                .stream()
                .filter(artist -> artist.getGender().equalsIgnoreCase(gender))
                .forEach(artist -> responseObserver.onNext(Song.newBuilder().setArtistId(artist.getArtistId()).build()));
                responseObserver.onCompleted();
        boolean await = countDownLatch.await(1, TimeUnit.MINUTES);
        return await ? response : Collections.emptyList();
    }

}
