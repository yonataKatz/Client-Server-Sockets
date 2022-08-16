
#ifndef ASSIGNMENT3_SERVERREADER_H
#define ASSIGNMENT3_SERVERREADER_H

#endif //ASSIGNMENT3_SERVERREADER_H
#include "thread"
#include <stdlib.h>
#include "../include/ConnectionHandler.h"
#include <string>
#include <iostream>

using namespace std;

class serverReader{
private:
    ConnectionHandler &connectionHandler;
    atomic<bool>& login;
    atomic<bool>& shouldTerminate;


public:
    //constructor
    serverReader(ConnectionHandler& connectionHandler, atomic<bool>& log, atomic<bool>& ter);
    //destructor
    ~serverReader();

    //methods
    void run();

};