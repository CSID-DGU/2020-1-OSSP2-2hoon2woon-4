#include "Client.h"
#include "Server.h"

#include <thread>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sstream>
#include <iomanip>
#include <iostream>

#include <errno.h>
#include <string>

#include <ctime>

/**
 * @author Seungun-Park
 * init sql
 * */
Client::Client(int _fd, struct sockaddr_in _addr, Server& _server)
	: client_fd(_fd), addr(_addr), server(_server), runnable(true)
{
	char logp[26] = "log/";
	strcat(logp, ip());
	log = fopen(logp, "a");

	curr_time = time(nullptr);
	localtime_r(&curr_time, &curr_tm);

	print_time();

	sqlhost = reinterpret_cast<const char*>(getenv("SQL_HOST"));
	sqluser = reinterpret_cast<const char*>(getenv("SQL_USER"));
	sqlpw = reinterpret_cast<const char*>(getenv("SQL_PW"));
	sqldb = reinterpret_cast<const char*>(getenv("SQL_DB"));

	if(mysql_init(&sqlconn) == NULL)
		std::cout << "mysql_init() error!" << std::endl;

	sqlconnection = mysql_real_connect(&sqlconn, sqlhost, sqluser, sqlpw, sqldb, 3306, (const char*)NULL, 0);
	if(sqlconnection == NULL)
		fprintf(log, "%d error: %s, %d\n", mysql_errno(&sqlconn), mysql_error(&sqlconn));
	else
	{
		if(mysql_select_db(&sqlconn, sqldb))
			fprintf(log,"%d error: %s, %d\n", mysql_errno(&sqlconn), mysql_error(&sqlconn));
	}
}

/**
 * @author Seungun-Park
 * close sql connection
 * close client socket
 * */
Client::~Client()
{
	mysql_close(sqlconnection);
	close(client_fd);
}

bool Client::isRunnable()
{
	return runnable;
}

/**
 * @author Seungun-Park
 * run process
 * */
void Client::run()
{
	while(1)
	{
		std::fill(buf, buf + BUF_LEN, '\0');
		readLen = read(client_fd, buf, BUF_LEN);
		fprintf(log, "%s", buf);
		if(readLen < 1) break;
		buf[readLen - 1] = '\0';
		if(strcmp("login", buf) == 0) login();
		else if(strcmp("register", buf) == 0) regist();
		else if(strcmp("join", buf) == 0)
		{
			if(room)
				exit();

			server.join(*this, client_fd, userid);

		}
		else if(strcmp("createroom", buf) == 0)
		{
			if(room)
				exit();
			server.createRoom(*this, client_fd, userid);
		}
		else if(strcmp("exit", buf) == 0) exit();
		else if(strcmp("game start", buf) == 0) gamestart();
		else if(strcmp("game", buf) == 0) game();
	}
	
	//save log file
	fclose(log);
	//end process
	runnable = false;
}

bool Client::exit()
{
	if(!room)
	{
		write(client_fd, "exit failed", 11);
		return false;
	}
	for(int i = 0; i < 4; i++)
	{
		if(room->clients[i] == client_fd)
		{
			room->clients[i] = 0;
			room->names[i] = "";
			for(int j = i; j < 3; j++)
			{
				room->clients[j] = room->clients[j+1];
				room->names[j] = room->names[j+1];
			}
			break;
		}
	}
	(room->number)--;
	server.exit();

	room.reset();
	return true;
}


/**
 * @author Seungun-Park
 * return ip address
 * */
char* Client::ip()
{
	inet_ntop(AF_INET, &(addr.sin_addr), ipaddr, INET_ADDRSTRLEN);
	return ipaddr;
}

/**
 * @author Seungun-Park
 * login function
 * */
bool Client::login()
{
	printf("login\n");
	//receive id
	std::fill(buf, buf + BUF_LEN, '\0');
	readLen = read(client_fd, buf, BUF_LEN);
	if(readLen < 1)
	{
		write(client_fd, "error", 5);
		return false;
	}
	userid = buf;
	write(client_fd, buf, readLen);

	//receive pw
	std::fill(buf, buf + BUF_LEN, '\0');
	readLen = read(client_fd, buf, BUF_LEN);
	if(readLen < 1)
	{
		write(client_fd, "error", 5);
	       	return false;
	}
	
	//encrypt pw
	//login authentication
	std::stringstream ss;
	for(char e : buf)
		ss << std::hex << std::setw(2) << std::setfill('0') << (((int)e)&0x000000FF);
	pw = ss.str();
	if(sqlconnection != NULL)
	{
		std::string query = "select * from tetris.users where userid='" + userid + "'";
		if(mysql_query(sqlconnection, query.c_str()))
			fprintf(log, "%d error: %s, %d\n", mysql_errno(&sqlconn), mysql_error(&sqlconn));
		sql_result = mysql_store_result(sqlconnection);
		
		//exist id
		if(mysql_num_rows(sql_result) != 0)
		{
			sql_row = mysql_fetch_row(sql_result);
			if(strcmp(pw.c_str(), sql_row[2]) == 0)
			{
				write(client_fd, "login success", 13);
				fprintf(log, "login : %s\n", sql_row[1]);
				return true;
			}
			else
			{
				write(client_fd, "wrong password", 14);
				fprintf(log, "login failed : %s\n", sql_row[1]);
				return false;
			}
		}
		else //not exist id
		{
			write(client_fd, "wrong userid", 12);
			fprintf(log, "login failed : %s\n", userid.c_str());
			return false;
		}

	}
	else
	{
		write(client_fd, "sql error", 8);
	}
	return false;
}

bool Client::regist()
{
	//receive id
	std::fill(buf, buf + BUF_LEN, '\0');
	readLen = read(client_fd, buf, BUF_LEN);
	if(readLen < 1)
	{
		write(client_fd, "error", 5);
		return false;
	}
	userid = buf;
	write(client_fd, buf, readLen);

	//receive pw
	std::fill(buf, buf + BUF_LEN, '\0');
	readLen = read(client_fd, buf, BUF_LEN);
	if(readLen < 1)
	{
		write(client_fd, "error", 5);
	       	return false;
	}
	
	//encrypt pw
	//login authentication
	std::stringstream ss;
	for(char e : buf)
		ss << std::hex << std::setw(2) << std::setfill('0') << (((int)e)&0x000000FF);
	pw = ss.str();
	if(sqlconnection != NULL)
	{
		std::string query = "select * from tetris.users where userid='" + userid + "'";
		if(mysql_query(sqlconnection, query.c_str()))
			fprintf(log, "%d error: %s, %d", mysql_errno(&sqlconn), mysql_error(&sqlconn));
		sql_result = mysql_store_result(sqlconnection);
		if(mysql_num_rows(sql_result) == 0)
		{
			std::string query = "insert into tetris.users(userid, password) VALUES ('"
					    + userid + "', '" + pw + "')";
			if(mysql_query(sqlconnection, query.c_str()))
				fprintf(log, "query error");

			printf("register : %s\n", userid.c_str());
			write(client_fd, "register success", 16);
			return true;
		}
		else
		{
			write(client_fd, "register failed", 15);
			return false;
		}
	}
	else
	{
		write(client_fd, "sql error", 8);
	}
	return false;
}

bool Client::rank()
{

}

void Client::game()
{
	while(1)
	{
		read(client_fd, buf, BUF_LEN);
		for(int i = 0; i < (*room).getNumber(); i++)
		{
			if((*room).clients[i] != client_fd)
				write((*room).clients[i], buf, BUF_LEN);
		}
	}
}

void Client::gamestart()
{
	if(room->clients[0] == client_fd)
	{
		for(int i = 0; i < room->number; i++)
		{
			write(room->clients[i], "gamestart", 9);
		}
	}
}
