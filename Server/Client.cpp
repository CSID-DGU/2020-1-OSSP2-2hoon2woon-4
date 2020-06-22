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
	exit();
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
		fprintf(log, "%s\n", buf);
		
		if(readLen < 1) break;

		if(strcmp("ready", buf) == 0) ready();
		else{
		printf("%s\n", buf);
		}

		if(strcmp("login", buf) == 0) login();
		else if(strcmp("logout", buf) == 0) logout();
		else if(strcmp("register", buf) == 0) regist();
		else if(strcmp("rank", buf) == 0) rank();
		else if(strcmp("rankupdate", buf) == 0) rankupdate();
		else if(strcmp("enterroom", buf) == 0)
		{
			if(room)
				exit();
			server.join(*this, client_fd, userid);

		}
		else if(strcmp("createroom", buf) == 0)
		{
			if(room)
				exit();
			server.createRoom(client_fd, *this, userid);
		}
		else if(strcmp("exit", buf) == 0) exit();
		else if(strcmp("gamestart", buf) == 0) gamestart();
		else if(strcmp("game", buf) == 0) game();
		else if(strcmp("getroominfo", buf) == 0) server.getroominfo(client_fd);
		else if(strcmp("game", buf) == 0) game();
		//else write(client_fd, buf, BUF_LEN);
	}
	
	//save log file
	//end process
	runnable = false;
	fclose(log);
	exit();
}

bool Client::logout()
{
	exit();
	write(client_fd, "success", 7);
}

bool Client::exit()
{
	if(!room)
	{
		write(client_fd, "fail", 4);
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
	write(client_fd, "success", 7);
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
	write(client_fd, buf, BUF_LEN);
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
	printf("login : %s\n", userid.c_str());

	char pwbuf[32];
	//receive pw
	std::fill(pwbuf, pwbuf + 32, '\0');
	readLen = read(client_fd, pwbuf, 32);
	if(readLen < 1)
	{
		write(client_fd, "error", 5);
	       	return false;
	}
	
	//encrypt pw
	//login authentication
	std::stringstream ss;
	for(char e : pwbuf)
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
				fprintf(log, "login : %s\n", userid.c_str());
				return true;
			}
			else
			{
				write(client_fd, "wrong password", 14);
				fprintf(log, "login failed : %s\n", userid.c_str());
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
	write(client_fd, buf, BUF_LEN);
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

	char pwbuf[32];
	//receive pw
	std::fill(pwbuf, pwbuf + 32, '\0');
	readLen = read(client_fd, pwbuf, 32);
	if(readLen < 1)
	{
		write(client_fd, "error", 5);
	       	return false;
	}
	
	//encrypt pw
	//login authentication
	std::stringstream ss;
	for(char e : pwbuf)
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
	if(sqlconnection != NULL)
	{
		std::string query = "select score, userid from tetris.users order by score desc";
		if(mysql_query(sqlconnection, query.c_str()))
			fprintf(log, "%d error: %s, %d\n", mysql_errno(&sqlconn), mysql_error(&sqlconn));
		sql_result = mysql_store_result(sqlconnection);
		int i = 0;
		std::string ranking = "";
		for(i = 0; i < mysql_num_rows(sql_result); i++)
		{
			sql_row = mysql_fetch_row(sql_result);
			ranking += (std::to_string(i+1) + "." + sql_row[0] + ":" + sql_row[1] + "/");
		}
		write(client_fd, ranking.c_str(), 256);
	}
	else
	{
		write(client_fd, " ", 1);
	}
}

bool Client::rankupdate()
{
	std::fill(buf, buf + BUF_LEN, '\0');
	readLen = read(client_fd, buf, BUF_LEN);
	if(sqlconnection != NULL)
	{
		int score = atoi(buf);
		std::string query = "select score from tetris.users where userid='" + userid + "'";
		if(mysql_query(sqlconnection, query.c_str()))
			fprintf(log, "%d error %s, %d\n", mysql_errno(&sqlconn), mysql_error(&sqlconn));
		sql_result = mysql_store_result(sqlconnection);
		if(mysql_num_rows(sql_result) != 0)
		{
			sql_row = mysql_fetch_row(sql_result);
			if(score > atoi(sql_row[0]))
			{
				std::string query = "update tetris.users set score=" + std::to_string(score) + " where userid='" + userid +"'";
				if(mysql_query(sqlconnection, query.c_str()))
					fprintf(log, "%d error %s, %d\n", mysql_errno(&sqlconn), mysql_error(&sqlconn));
			}
		}
		fprintf(log, "%s\n", query.c_str());
	}
	write(client_fd, " ", 1);
}

void Client::game()
{
	if(room->game)
	{	
		write(client_fd, "start", 5);
		(room->wait)++;
		while(1)
		{
			if(room->wait == room->getNumber())
			{
			std::fill(buf, buf + BUF_LEN, '\0');
			readLen = read(client_fd, buf, BUF_LEN);
			if(readLen < 1) break;
			//printf("%s : %s\n\n", userid.c_str(), buf);
			if(strcmp(buf, "die") == 0)
			{
				room->rank[(room->dead)++] = client_fd;	
				if(room->dead == room->getNumber())
				{
					std::fill(buf, buf + BUF_LEN, '\0');
					strcpy(buf, "gameend");
					for(int i = room->dead - 1; i >= 0; i--)
					{
						strcat(buf, ":");
						for(int j = 0; j < room->getNumber(); j++)
						{
							if(room->clients[j] == room->rank[i])
							{
								strcat(buf, room->names[j].c_str());
								break;
							}
						}
					}
				}
				else
				{
					std::fill(buf, buf + BUF_LEN, '\0');
					strcpy(buf, userid.c_str());
					strcat(buf, ":dead");
				}
			}
			else if(strcmp(buf, "dead") == 0)
			{
				std::fill(buf, buf +BUF_LEN, '\0');
				strcpy(buf, userid.c_str());
				strcat(buf, ":dead");				
			}
			for(int i = 0; i < (*room).getNumber(); i++)
			{
				if((*room).clients[i] != client_fd)
					write((*room).clients[i], buf, BUF_LEN);
			}
			}
		}
	}
	else
	{
		write(client_fd, "fail", 4);
	}
}

bool Client::gamestart()
{
	if(room->clients[0] == client_fd)
	{
		room->game = true;
		write(client_fd, "success", 7);
		return true;
	}
	else
	{
		write(client_fd, "fail", 4);
		return false;
	}
}

bool Client::ready()
{
	if(room)
	{
		if(room->game)
		{
			write(client_fd, "gamestart", 9);
		}
		else
		{
			char buf[256];
			std::fill(buf, buf + 256, '\0');
			for(int i = 0; i < room->getNumber(); i++)
			{
				strcat(buf, room->names[i].c_str());
				strcat(buf, ",");
			}
			write(client_fd, buf, 256);
		}
		return true;
	}
	else
	{
		write(client_fd, "notinroom", 9);
		return false;
	}
}
