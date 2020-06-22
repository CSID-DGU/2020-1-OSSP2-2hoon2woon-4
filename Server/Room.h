#ifndef __ROOM_H__
#define __ROOM_H__

#include <string>

class Room
{
public:
	int number;
	bool game;
	std::string name;
public:
	Room(int, std::string, std::string);

	int getNumber();
	std::string getBjname();

	int clients[4] = {0, };
	std::string names[4] = {"", };

	int wait;

	int dead;
	int rank[4] = {0, };

	std::string getName();
};

#endif
