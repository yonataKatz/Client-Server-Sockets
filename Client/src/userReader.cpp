#include "../include/userReader.h"

userReader::userReader(ConnectionHandler& con , atomic<bool>& ter, atomic<bool>& login): ch(con), shouldTerminate(ter), isLogin(login)
{}

void userReader::run()
{
    EncoderDecoder encdec;
    while (!shouldTerminate) {
        const short bufsize = 1024;
        if (isLogin)
        {
           char buf[bufsize];
            std::cin.getline(buf, bufsize);
            std::string line(buf);
            if (line=="LOGOUT") {
                isLogin= false;
            }
            char *bytes = encdec.encode(line);
            int counter = 0;
            while (bytes[counter] != ';')
                counter++;
            if (!ch.sendBytes(bytes, counter + 1)) {
                std::cout << "Disconnected. Exiting...\n" << std::endl;
                break;
            }
        }
    }
}
