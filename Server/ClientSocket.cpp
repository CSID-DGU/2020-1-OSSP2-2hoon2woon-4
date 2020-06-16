/**
 * 2020-04-27 Seungun-Park
 * Server.cpp
 */
#include "ClientSocket.h"
#include "Server.h"

#include <thread>
#include <WS2tcpip.h>
#include <stdio.h>
#include <sstream>
#include <iomanip>
#include <iostream>

ClientSocket::ClientSocket(int _fd, sockaddr_in _addr, Server& _server)
    : client_fd(_fd), addr(_addr), server(_server), runable(true)
{	
	if (mysql_init(&sqlconn) == NULL)
		printf("mysql_init() error!");

	sqlconnection = mysql_real_connect(&sqlconn, sqlhost, sqluser, sqlpw, sqldb, 3306, (const char*)NULL, 0);
	if (sqlconnection == NULL)
		printf("%d error: %s, %d\n", mysql_errno(&sqlconn), mysql_error(&sqlconn));
	else
	{
		if (mysql_select_db(&sqlconn, sqldb))
			printf("%d error: %s, %d\n", mysql_errno(&sqlconn), mysql_error(&sqlconn));
	}
}

ClientSocket::~ClientSocket() 
{
	mysql_close(sqlconnection);
	closesocket(client_fd);
}

bool ClientSocket::isRunable()
{
	return runable;
}

void ClientSocket::run()
{
	while (1) 
	{
		std::fill(buf, buf + BUF_LEN, '\0');
		readLen = recv(client_fd, buf, BUF_LEN, 0);
		if (readLen < 1)
		{
			break;
		}
		//printf_s("%s\n", buf);

		if (strcmp("login", buf) == 0) login();
		else if (strcmp("register", buf) == 0) regist();
	};
	runable = false;
}

char* ClientSocket::ip()
{
	inet_ntop(AF_INET, &(addr.sin_addr), ipaddr, INET_ADDRSTRLEN);
	return ipaddr;
}

bool ClientSocket::login()
{
	std::fill(buf, buf + BUF_LEN, '\0');
	readLen = recv(client_fd, buf, BUF_LEN, 0);
	if (readLen < 1) return false;
	userid = buf;
	send(client_fd, buf, readLen, 0);

	std::fill(buf, buf + BUF_LEN, '\0');
	readLen = recv(client_fd, buf, BUF_LEN, 0);
	if (readLen < 1) return false;	
	std::stringstream ss;
	for (char e : buf)
		ss << std::hex << std::setw(2) << std::setfill('0') << (((int)e)&0x000000FF);
	pw = ss.str();
	if (sqlconnection != NULL)
	{
		std::string query = "select * from tetris.users where userid='"+userid+"'";
		if(mysql_query(sqlconnection, query.c_str()))
			printf("%d error: %s, %d\n", mysql_errno(&sqlconn), mysql_error(&sqlconn));
		sql_result = mysql_store_result(sqlconnection);
		if (mysql_num_rows(sql_result) != 0)
		{
			MYSQL_ROW row = mysql_fetch_row(sql_result);
			if (strcmp(pw.c_str(), row[2]) == 0)
			{
				send(client_fd, "login success", 13, 0);
				printf("login : %s\n", row[1]);
				return true;

			}
			else
			{
				send(client_fd, "login failed", 12, 0);
				return false;
			}
		}
		else
		{
			send(client_fd, "login failed", 12, 0);
			return false;
		}
	}
	return false;
}

bool ClientSocket::regist()
{
	std::fill(buf, buf + BUF_LEN, '\0');
	readLen = recv(client_fd, buf, BUF_LEN, 0);
	if (readLen < 1) return false;
	userid = buf;
	send(client_fd, buf, readLen, 0);

	std::fill(buf, buf + BUF_LEN, '\0');
	readLen = recv(client_fd, buf, BUF_LEN, 0);
	if (readLen < 1) return false;	
	std::stringstream ss;
	for (char e : buf)
		ss << std::hex << std::setw(2) << std::setfill('0') << (((int)e)&0x000000FF);
	pw = ss.str();
	if (sqlconnection != NULL)
	{
		std::string query = "select * from tetris.users where userid='" + userid + "'";
		if (mysql_query(sqlconnection, query.c_str()))
			printf("%d error: %s, %d\n", mysql_errno(&sqlconn), mysql_error(&sqlconn));
		sql_result = mysql_store_result(sqlconnection);
		if (mysql_num_rows(sql_result) == 0)
		{
			std::string query = "insert into tetris.users(userid, password) VALUES ('" + userid + "','" + pw + "')";
			if (mysql_query(sqlconnection, query.c_str()))
				printf("%d error: %s, %d\n", mysql_errno(&sqlconn), mysql_error(&sqlconn));

			printf("register : %s\n", userid);
			send(client_fd, "register success", 16, 0);
		}
		else
		{
			send(client_fd, "register failed", 15, 0);
		}
	}
	return false;
}
