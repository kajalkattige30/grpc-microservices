package com.kajal.service;

import com.google.protobuf.Descriptors;
import com.google.rpc.context.AttributeContext;
import com.kajal.Singer;
import com.kajal.Song;
import com.kajal.SongSingerServiceGrpc;
import com.kajal.TempDb;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service
public class SongSingerClientService {
    @GrpcClient("grpc-service")
    SongSingerServiceGrpc.SongSingerServiceBlockingStub synchronousClient;

    @GrpcClient("grpc-service")
    SongSingerServiceGrpc.SongSingerServiceStub asynchronousClient;

    public Map<Descriptors.FieldDescriptor, Object> getSinger(int singerId) {
        Singer singerRequest = Singer.newBuilder().setSingerId(singerId).build();
        Singer singerResponse = synchronousClient.getSinger(singerRequest);
        return singerResponse.getAllFields();
    }

    public List<Map<Descriptors.FieldDescriptor, Object>> getSongsBySinger(int singerId) throws InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        Singer singerRequest = Singer.newBuilder().setSingerId(singerId).build();
        final List<Map<Descriptors.FieldDescriptor, Object>> response = new ArrayList<>();
        asynchronousClient.getSongsBySinger(singerRequest, new StreamObserver<Song>() {
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
        TempDb.getSongsFromTempDb().forEach(responseObserver::onNext);
        responseObserver.onCompleted();
        boolean await = countDownLatch.await(1, TimeUnit.MINUTES);
        return await ? response : Collections.emptyMap();
    }

    public List<Map<Descriptors.FieldDescriptor, Object>> getSongsBySingerGender(String gender) throws InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final List<Map<Descriptors.FieldDescriptor, Object>> response = new ArrayList<>();
        StreamObserver<Song> responseObserver = asynchronousClient.getSongBySingerGender(new StreamObserver<Song>() {
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
        TempDb.getSingersFromTempDb()
                .stream()
                .filter(singer -> singer.getGender().equalsIgnoreCase(gender))
                .forEach(singer -> responseObserver.onNext(Song.newBuilder().setSingerId(singer.getSingerId()).build()));
                responseObserver.onCompleted();
        boolean await = countDownLatch.await(1, TimeUnit.MINUTES);
        return await ? response : Collections.emptyList();
    }

}
