This source code writen by C++17.
 It is recommended to compile with g++ on Linux systems.



## How to build and run

### `make`

If you want to compile without additional configuration, use the 'make' command in 'Server' directory.

### `./Server`

Use the './Server' command in 'Server' directory to run server program.

### `Ctrl+C`

Use the 'Ctrl+C' to exit the program.

### `nohup ./Server &`

Use nohup to run the server as a background program.


## Environment Variable

### `PORT`

'PORT' is your server's port number. Additionally, if you want access from another computer, you need to add this port to your server system's inbound rules.

### `SQL_HOST`

'SQL_HOST' is IP address of your MySQL system. If the MySQL are running on the same system with server, set it to 'localhost'.

### `SQL_USER`

'SQL_USER' is username of MySQL.

### `SQL_PW`

'SQL_PW' is password of user.

### `SQL_DB`

'SQL_DB' is database name. If you will use 'tetris.sql' file, set it to 'tetris'.

This program should use 'tetris' as database name.


## log Directory

The server program creates log files with filename as the ip address.
