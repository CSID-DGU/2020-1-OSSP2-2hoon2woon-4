#ifndef __ROOM_H__
#define __ROOM_H__

#include <string>

class Room
{
public:
	int number;

public:
	Room(int, std::string);

	int getNumber();
	std::string getBjname();

	int clients[4] = {0, };
	std::string names[4] = {"", };
};

#endif
