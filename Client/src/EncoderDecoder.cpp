
#include "../include/EncoderDecoder.h"


//remember to delete from stack
//delete if not neccessery
EncoderDecoder::EncoderDecoder() :
     /*   chars(new char[1024]), */
        response(""), index(2) {}

EncoderDecoder::~EncoderDecoder() {}


char* EncoderDecoder::EncoderDecoder::encode(string str) {

    char* toReturn = new char [1024];
    //Register case
    if (str[0] == 'R') {
        short opCode = 1;
        shortToBytes(opCode, toReturn);
        index = 2;
        unsigned int strIndex = 9;
        while (strIndex < str.length()) {
            if (str[strIndex] != ' ')
                toReturn[index] = str[strIndex];
            else
                toReturn[index] = '\0';
            strIndex++;
            index++;
        }
        //finished string from cin - now adding another \0 for server decode
        toReturn[index] = '\0';
        index++;
        toReturn[index] = ';';
    }

    //Login Case
    if ((str[0] == 'L') &( str[3] == 'I')) {
        short opCode = 2;
        shortToBytes(opCode, toReturn);

        index = 2;
        unsigned int strIndex = 6;
        while (strIndex < str.length()) {
            if (str[strIndex] != ' ')
                toReturn[index] = str[strIndex];
            else
                toReturn[index] = '\0';
            strIndex++;
            index++;
        }
        toReturn[index] = ';';
    }

    //Logout Case
    if ((str[0] == 'L') &( str[3] == 'O') ){
        short opCode = 3;
        shortToBytes(opCode, toReturn);
        toReturn[2]=';';
    }

    //Follow | UnFollow
    if (str[0] == 'F') {
        short opCode = 4;
        shortToBytes(opCode, toReturn);
        if (str[7]=='0')
            toReturn[2] = '0';
        else
            toReturn[2]='1';
        unsigned int strIndex = 9;
        index = 3;
        while (strIndex < str.length()) {
            toReturn[index] = str[strIndex];
            index++;
            strIndex++;
        }
        toReturn[index] = '\0';
        index++;
        toReturn[index] = ';';
    }



    //Post
    if ((str[0] == 'P') & (str[1] == 'O')) {
        short opCode = 5;
        shortToBytes(opCode, toReturn);
        unsigned int strIndex = 5;
        index = 2;
        while (strIndex < str.length()) {
            toReturn[index] = str[strIndex];
            index++;
            strIndex++;
        }
        toReturn[index]='\0';
        index++;
        toReturn[index] = ';';
    }

    //PM
    if ((str[0] == 'P' )& (str[1] == 'M')) {
        short opCode = 6;
        shortToBytes(opCode, toReturn);
        unsigned int strIndex=3;
        index=2;
        while (strIndex < str.length()) {
            if (str[strIndex] != ' ')
                toReturn[index] = str[strIndex];
            else
                toReturn[index] = '\0';
            strIndex++;
            index++;
        }
        toReturn[index]='\0';
        index++;
        toReturn[index] = ';';
    }


    //LogStat Case
    if ((str[0] == 'L') &( str[3] == 'S')) {
        short opCode = 7;
        shortToBytes(opCode, toReturn);
        index=2;
        toReturn[index] = ';';
    }

    //Stat case
    if (str[0] == 'S') {
        short opCode = 8;
        shortToBytes(opCode, toReturn);
        index = 2;
        int userCounter = 0;
        int usersNamesLength = str.length() - 5;
        string strnew = str.substr(5,usersNamesLength);
        string curUserName = "";
        while (userCounter < usersNamesLength) {
            if (strnew[userCounter] == ' ')
                toReturn[index] = '|';
            else
                toReturn[index] = strnew[userCounter];
            index++;
            userCounter++;
        }
        toReturn[index] = '\0';
        index++;
        toReturn[index] = ';';
    }

    //Block case
    if (str[0] == 'B') {
        short opCode = 12;
        shortToBytes(opCode, toReturn);
        unsigned int userCounter = 6;
        index = 2;
        while (userCounter < str.length()) {
            toReturn[index] = str[userCounter];
            index++;
            userCounter++;
        }
        toReturn[index] = '\0';
        index++;
        toReturn[index] = ';';
    }
    //needs to be deleted
    char* toReturnUpdated = new char[index];
    for (int i=0; i<=index; i++)
        toReturnUpdated[i]=toReturn[i];
    return toReturnUpdated;
}


void EncoderDecoder::shortToBytes(short num, char *bytesArr) {
    bytesArr[0] = ((num >> 8) & 0xFF);
    bytesArr[1] = (num & 0xFF);
}

short EncoderDecoder::bytesToShort(char *bytesArr) {
    short result = (short) ((bytesArr[0] & 0xff) << 8);
    result += (short) (bytesArr[1] & 0xff);
    return result;
}



