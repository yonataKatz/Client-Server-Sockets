

#ifndef BOOST_ECHO_CLIENT_ENCODERDECODER_H
#define BOOST_ECHO_CLIENT_ENCODERDECODER_H
#endif //BOOST_ECHO_CLIENT_ENCODERDECODER_H
using namespace std;
#include "string"


class EncoderDecoder{

private:
   // char* chars;
    string response;
    int index;


public:
   //constructor
   EncoderDecoder();

   //destructor
   virtual ~EncoderDecoder();

   //methods
   char* encode(string str);

   void shortToBytes(short sh, char* arr);

    short bytesToShort (char* byteArr);

};
