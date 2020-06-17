CC=g++
CFLAGS=-std=c++17
LDFLAGS=-lmysqlclient -pthread
OBJS=Room.o Server.o Client.o main.o
TARGET=Server

$(TARGET): $(OBJS)
	$(CC) -o $@ $(OBJS) $(LDFLAGS)

main.o: Client.h Server.h Room.h main.cpp
	$(CC) -c -o main.o main.cpp $(CFLAGS)

Client.o: Client.h Server.h Room.h Client.cpp
	$(CC) -c -o Client.o Client.cpp $(CFLAGS)

Server.o: Client.h Server.h Room.h Server.cpp
	$(CC) -c -o Server.o Server.cpp $(CFLAGS)

Room.o: Client.h Server.h Room.h Room.cpp
	$(CC) -c -o Room.o Room.cpp $(CFLAGS)

clean:
	rm -f *.o
	rm -f $(TARGET)

