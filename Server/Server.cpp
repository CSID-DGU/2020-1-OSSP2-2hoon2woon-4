#include "Server.h"
#include "Client.h"

#include <stdio.h>
#include <stdlib.h>
#include <iostream>
#include <errno.h>
#include <string>
#include <string.h>
#include <stdexcept>

/**
 * @author Seungun-Park
 * Create a server socket.
 * Start listen
 * */
Server::Server()
{
	port = atoi(reinterpret_cast<const char*>(getenv("PORT")));
	server_fd = socket(PF_INET, SOCK_STREAM, 0);
	if(server_fd == -1) throw std::runtime_error(std::string(strerror(errno)));
	
	std::fill(reinterpret_cast<char*>(&server_address),
		  reinterpret_cast<char*>(&server_address) + sizeof(server_address),
		  0);
	
	server_address.sin_family = AF_INET;
	server_address.sin_addr.s_addr = htonl(INADDR_ANY);
	server_address.sin_port = htons(port);

	{
		int result = bind(server_fd,
				  reinterpret_cast<const sockaddr*>(&server_address),
				  sizeof(server_address));
		if(result == -1) throw std::runtime_error(std::string(strerror(errno)));
	}
	backlog = 8;
	{
		int result = listen(server_fd, backlog);
		if(result == -1) throw std::runtime_error(std::string(strerror(errno)));
	}

	std::cout << "Server Start!" << std::endl;
}

/**
 * @author Seungun-Park
 * close server socket;
 * */
Server::~Server()
{
	close(server_fd);
}

/**
 * @author Seungun-Park
 * accept client.
 * if connect client, push client socket into vector and start thread
 * */
void Server::run()
{
	while(1)
	{
		struct sockaddr_in client_address;
		socklen_t client_address_size = sizeof(client_address);

		int client_fd = accept(server_fd,
				       reinterpret_cast<sockaddr*>(&client_address),
				       &client_address_size);

		if(client_fd == -1) continue;
		
		auto client = std::make_shared<Client>(client_fd, client_address, *this);

		clients.push_back(client);

		threads.push_back(std::thread(func, clients.back()));

		for(std::vector<std::shared_ptr<Client>>::iterator iter = clients.begin();
		    iter != clients.end();)
		{
			if(!((*iter)->isRunnable()))
			{
				iter = clients.erase(iter);
				continue;
			}
			++iter;
		}
		
	}
}

void func(std::shared_ptr<Client> client)
{
	printf("Connect : %s\n", client->ip());
	client->run();
	printf("Disconnect : %s\n", client->ip());
}

bool Server::createRoom(int fd, Client& c, std::string bjname)
{
	write(fd, "create", 6);
	std::fill(buf, buf + buflen, '\0');

	read(fd, buf, buflen);
	std::string roomname = buf;
	bool duplicate = true;
	std::fill(buf, buf + buflen, '\0');
	if(roomname.length() < 1)
	{
		strcpy(buf, "fail");
	}
	for(std::vector<std::shared_ptr<Room>>::iterator iter = rooms.begin();
	    iter != rooms.end(); iter++)
	{
		if((*iter)->getName().compare(roomname) == 0)
		{
			std::fill(buf, buf + buflen, '\0');
			strcpy(buf, "fail");
			duplicate = false;
			break;
		}
	}
	if(duplicate)
	{
		auto room = std::make_shared<Room>(fd, bjname, roomname);
		rooms.push_back(room);
		c.room = room;
		printf("%s\n", room->getName().c_str());
		strcpy(buf, c.room->getName().c_str());
	}
	write(fd, buf, buflen);
	printf("send room name : %s\n", buf);
	return true;
}

bool Server::join(Client& c, int fd, std::string name)
{
	write(fd, "enter", 5);
	std::fill(buf, buf + buflen, '\0');
	read(fd, buf, buflen);
	std::string roomname = buf;

	for(std::vector<std::shared_ptr<Room>>::iterator iter = rooms.begin();
	    iter != rooms.end(); iter++)
	{
		if((*iter)->getName().compare(roomname) == 0)
		{
			if((*iter)->number < 4)
			{
				((*iter)->clients)[(*iter)->number] = fd;
				((*iter)->names)[(*iter)->number] = name;
				c.room = (*iter);
				write(fd, std::to_string((*iter)->number).c_str(), 1);
				((*iter)->number)++;
				return true;
			}
			else
			{
				write(fd, "fail", 4);
				return false;
			}
		}
	}
}

bool Server::exit()
{
	for(std::vector<std::shared_ptr<Client>>::iterator iter = clients.begin();
		    iter != clients.end();)
		{
			if(!((*iter)->isRunnable()))
			{
				iter = clients.erase(iter);
				continue;
			}
			++iter;
		}
	for(std::vector<std::shared_ptr<Room>>::iterator iter = rooms.begin();
		    iter != rooms.end();)
		{
			if((*iter)->number == 0)
			{
				iter = rooms.erase(iter);
				continue;
			}
			++iter;
		}
}

bool Server::getroominfo(int fd)
{
	std::string roominfo = "noroominfo";
	if(rooms.size()>0){
	roominfo = "";
	for(std::vector<std::shared_ptr<Room>>::iterator iter = rooms.begin();
	    iter != rooms.end(); iter++)
	{
		printf("1.%s\n", (*iter)->getName().c_str());
		roominfo += ((*iter)->getName() + "," + std::to_string((*iter)->getNumber()) + "\n");
	}}
	char roombuf[1024];
	std::fill(roombuf, roombuf + 1024, '\0');
	strcpy(roombuf, roominfo.c_str());
	printf("%s\n", roombuf);
	write(fd, roombuf, 1024);
}
