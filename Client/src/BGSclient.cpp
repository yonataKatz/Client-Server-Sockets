#include <iostream>
#include <thread>
#include "../include/ConnectionHandler.h"
#include "../include/userReader.h"
#include "../include/serverReader.h"
#include "atomic"



int main(int argc, char *argv[]) {
//    if (argc < 3) {
//        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
//        return -1;
//    }
    std::string host = argv[1];
    short port = atoi(argv[2]);

    ConnectionHandler connectionHandler(host, port);
    if (!connectionHandler.connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }

    atomic<bool> isLogin(true);
    atomic<bool> shouldTerminate(false);


    userReader ur (connectionHandler,shouldTerminate,isLogin);
    std::thread user (&userReader::run, &ur);
    serverReader sr (connectionHandler, isLogin,shouldTerminate);
    std::thread ser (&serverReader::run, &sr);

    ser.join();
    user.join();
}
