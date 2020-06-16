#include "Server.h"
#include <stdio.h>
#include <stdlib.h>

int main(int argc, char **argv)
{
    Server server = Server();

	server.run();

    return 0;
}