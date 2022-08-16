#include "../include/serverReader.h"
#include "../include/EncoderDecoder.h"


serverReader::serverReader(ConnectionHandler &connectionHandler, atomic<bool>& log ,atomic<bool>& ter ): connectionHandler(connectionHandler), login(log), shouldTerminate(ter) {}

serverReader::~serverReader() {}

void serverReader::run() {
    //while client is login
    while (!shouldTerminate) {
        std::string answer;
        EncoderDecoder endec;

        //decoding and make sure it went good
        if (!connectionHandler.getLine(answer)) {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            break;
        }
        if (answer.size() > 0)
            std::cout << answer << std::endl;
        if (answer == "Error 3")
            login = true;
        if (answer.substr(0,5) == "ACK 3")
            shouldTerminate = true;
        answer = "";
    }

}