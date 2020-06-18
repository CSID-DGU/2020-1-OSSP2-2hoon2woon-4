#ifndef __CLIENT_H__
#define __CLIENT_H__

#include <unistd.h>
#include <arpa/inet.h>
#include <sys/types.h>
#include <sys/socket.h>
#include "Room.h"

#include <memory>
#include <mysql/mysql.h>
#include <string>

#define BUF_LEN 32

#define print_time() fprintf(log, "%04d-%02d-%02d %02d:%02d:%02d\n", curr_tm.tm_year + 1900, curr_tm.tm_mon + 1, curr_tm.tm_mday, curr_tm.tm_hour, curr_tm.tm_min, curr_tm.tm_sec);

class Server;
class Room;

class Client
{
private:
	bool login();
	bool regist();
	bool rank();
	bool exit();
	bool rankupdate();

public:
	Client(int, sockaddr_in, Server&);
	~Client();
	void run();
	bool isRunnable();
	char* ip();
	void game();
	void gamestart();
	std::shared_ptr<Room> room;

private:
	int client_fd;
	bool runnable;
	Server& server;
	struct sockaddr_in addr;

	char buf[BUF_LEN];
	int readLen;
	char ipaddr[22];

	int user;
	std::string userid;
	std::string pw;

	const char* sqlhost;
	const char* sqluser;
	const char* sqlpw;
	const char* sqldb;

	MYSQL* sqlconnection = NULL;
	MYSQL sqlconn;
	MYSQL_RES* sql_result;
	MYSQL_ROW sql_row;

	FILE* log;

	struct tm curr_tm;
	time_t curr_time;
};

#endif
