/**
 * 2020-04-26 Seungun-Park
 * Server.h
 */
#pragma once
#pragma comment(lib, "ws2_32.lib");

#include <WinSock2.h>
#include <thread>
#include <memory>
#include <vector>

#define MAX_THREAD_POOL 128

class ClientSocket;

class Server
{
public:
    Server(); 
	~Server();

    void run();

private:
    SOCKET server_fd;
    int port;
    int backlog;
	
	WORD		wVersionRequested;
    WSADATA		wsaData;
    SOCKADDR_IN servAddr, cliAddr; //Socket address information
    int			err;

    std::vector<std::shared_ptr<ClientSocket>> clientSockets;
	std::vector<std::thread> threads;
};

void func(std::shared_ptr<ClientSocket>);
