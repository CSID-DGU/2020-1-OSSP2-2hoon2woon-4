#include "Room.h"

Room::Room(int _bj, std::string _bjname)
{
	number = 1;
	clients[0] = _bj;
	names[0] = _bjname;
}

int Room::getNumber()
{
	return number;
}

std::string Room::getBjname()
{
	return names[0];
}
