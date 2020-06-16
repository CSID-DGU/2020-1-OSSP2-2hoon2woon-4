/**
 * 2020-04-27 Seungun-Park
 * Server.cpp
 */
#pragma once
#pragma comment(lib, "libmysql.lib")

#include <WinSock2.h>
#include <string>
#include <mysql.h>

#define BUF_LEN 32

class Server;

class ClientSocket
{
private:
	bool login();
	bool regist();

public:
    ClientSocket(int, sockaddr_in, Server&);
	~ClientSocket();
    void run();
	bool isRunable();
	char* ip();

private:
    int client_fd;
	bool runable;
    Server& server;
	sockaddr_in addr;

	char buf[BUF_LEN];
	int readLen;
	char ipaddr[22];

	int user;
	std::string userid;
	std::string pw;

	const char* sqlhost = "";
	const char* sqluser = "";
	const char* sqlpw = "";
	const char* sqldb = "";

	MYSQL* sqlconnection = NULL;
	MYSQL sqlconn;
	MYSQL_RES* sql_result;
	MYSQL_ROW sql_row;

};
