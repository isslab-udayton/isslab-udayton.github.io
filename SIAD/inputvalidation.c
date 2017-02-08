#include <stdio.h>
#include <stdlib.h>
#include <string.h>
int main(int argc, char *argv[])
{
	char servername[256];
	int port;
	printf("Client program\n");
	if(argc!=3) {
		printf("Usage: %s <server> <port>\n", argv[0]);
		exit(0);
	}
	strcpy(servername, argv[1]);
	printf("Server:");
   	printf(servername);
	
	return 0;
}
