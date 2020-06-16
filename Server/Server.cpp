/**
 * 2020-04-26 Seungun-Park
 * Server.cpp
 */
#include "Server.h"
#include "ClientSocket.h"

#include <stdexcept>
#include <stdio.h>
#include <stdlib.h>
#include <string>
#include <string.h>
#include <errno.h>
#include <thread>
#include <iostream>
#include <WS2tcpip.h>

Server::Server()
{
	wVersionRequested = MAKEWORD(1, 1);
    err = WSAStartup(wVersionRequested, &wsaData);
	
	if (err != 0) {
		std::cout << "WSAStartup error " << WSAGetLastError() << std::endl;
        WSACleanup();
		exit(0);
    }
    port = atoi(reinterpret_cast<const char*>(getenv("PORT")));
    server_fd = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);
    if(server_fd == INVALID_SOCKET) throw std::runtime_error(std::string(strerror(errno)));
    /*{
        int option;
        setsockopt(server_fd, SOL_SOCKET, SO_REUSEADDR, reinterpret_cast<const void*>(&option), sizeof(option));
    }*/

    sockaddr_in socketAddress;

    std::fill(reinterpret_cast<char*>(&socketAddress),
              reinterpret_cast<char*>(&socketAddress) + sizeof(socketAddress),
              0);
    socketAddress.sin_family = AF_INET;
    socketAddress.sin_addr.s_addr = htonl(INADDR_ANY);
    socketAddress.sin_port = htons(port);

    {
        int result = bind(server_fd, reinterpret_cast<const sockaddr*>(&socketAddress), sizeof(socketAddress));
        if(result == SOCKET_ERROR) throw std::runtime_error(std::string(strerror(errno)));
    }

    {
        backlog = 1;
        int result = listen(server_fd, backlog);
        if(result == -1) throw std::runtime_error(std::string(strerror(errno)));
    }

	printf("Server Start\n");
}

Server::~Server()
{
	closesocket(server_fd);
    WSACleanup();
}

void Server::run()
{
    while(1)
    {
        sockaddr_in clientAddress;
        int clientAddressLength = sizeof(clientAddress);

        int client_fd = accept(server_fd,
                               reinterpret_cast<sockaddr*>(&clientAddress), 
                               &clientAddressLength);


        if(client_fd == -1) continue;

        auto clientSocket = std::make_shared<ClientSocket>(client_fd, clientAddress, *this);

        clientSockets.push_back(clientSocket);

		threads.push_back(std::thread(func, clientSockets.back()));
		
		for (std::vector<std::shared_ptr<ClientSocket>>::iterator iter = clientSockets.begin();
			 iter != clientSockets.end();)
		{
			if (!((*iter)->isRunable()))
			{
				iter = clientSockets.erase(iter);
				continue;
			}
			++iter;
		}
    }
}

void func(std::shared_ptr<ClientSocket> client)
{
	printf("connect client : %s\n", client->ip());
	client->run();
	printf("disconnect client : %s\n", client->ip());
}