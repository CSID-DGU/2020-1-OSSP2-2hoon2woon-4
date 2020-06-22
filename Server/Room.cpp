#include "Room.h"

Room::Room(int _bj, std::string _bjname, std::string _name)
{
	number = 1;
	clients[0] = _bj;
	names[0] = _bjname;
	name = _name.c_str();
	game = false;
}

int Room::getNumber()
{
	return number;
}

std::string Room::getBjname()
{
	return names[0];
}

std::string Room::getName()
{
	return name;
}
