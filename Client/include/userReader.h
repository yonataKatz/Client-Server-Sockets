

#ifndef ASSIGNMENT3_USERREADER_H
#define ASSIGNMENT3_USERREADER_H

#endif //ASSIGNMENT3_USERREADER_H

#include "thread"
#include <stdlib.h>
#include "../include/ConnectionHandler.h"
#include <string>
#include "../include/EncoderDecoder.h"
#include "atomic"

using namespace std;

class userReader {

private:
    ConnectionHandler& ch;
    atomic<bool> &shouldTerminate;
    atomic<bool> &isLogin;



public:
    //constructor
    userReader(ConnectionHandler& con, atomic<bool>& ter, atomic<bool>& login);

    //methods
    void run();

};