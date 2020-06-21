#ifndef __SERVER_H__
#define __SERVER_H__

#include <thread>
#include <memory>
#include <vector>

#include <unistd.h>
#include <arpa/inet.h>
#include <sys/types.h>
#include <sys/socket.h>

#define MAX_THREAD_POOL 128

class Client;
class Room;

class Server
{
public:
	Server();
	~Server();

	void run();

	bool createRoom(int, Client&, std::string);
	bool join(Client&, int, std::string);
	bool exit();
	bool getroominfo(int);

private:
	int number = 0;
	int server_fd;
	int port;
	int backlog;

	int buflen = 256;
	char buf[256];

	struct sockaddr_in server_address;

	std::vector<std::shared_ptr<Client>> clients;
	std::vector<std::thread> threads;
	std::vector<std::shared_ptr<Room>> rooms;
};

void func(std::shared_ptr<Client>);

#endif
