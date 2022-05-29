# gRPC-microservices

gRPC is a modern communication framework which was introduced in 2015. The protocol buffers in the proto file act as a form of contract for communication. The proto file is a schema for the data that is being sent across the microservices. It helps define things like the fields that are required, fields that are optional as well as the object types for these fields. The proto file also defines the procedures that are expected to be exposed, essentially defining what procedures are callable remotely from other microservices. gRPC also provides great performance benefits with HTTP 2.0 as protocol buffers are serialized and sent as binaries across the wire. In traditional REST API, where the de facto message type is JSON, they are not compressed or flat by any means considering that they are key-value pairs that certainly are not the most space efficient. With the protocol buffers, messages are sent as binaries which are significantly smaller than the normal JSON messages. This makes gRPC fast and efficient.

This work implements grpc using a spring boot application. It implements 4 types of grpc - unary, server streaming, client streaming and bidirectional streaming on a database file containing lists of songs and artists.

There are three modules created in this project:

1. Proto: contains all the proto files(Schemas)
2. grpc-server : This is the server module which contains the server application and its implementation. Client microservice makes remote procedure calls to this microservice.

3. grpc-client: This is the client module which contains a controller file where in some APIs are exposed. These APIs make use of auto-generated services which internally do the inter-service communication and invoke methods in the server microservice.

Steps to run the project:

Start the client and server applications.

Run the following commands in a terminal:

curl http://localhost:8080/artist/1

curl http://localhost:8080/song/1

curl http://localhost:8080/song

curl http://localhost:8080/song/artist/male







