#
#CFLAGS:=-c -Wall -Weffc++ -g -std=c++11 -Iinclude
#LDFLAGS:=-lboost_system -lboost_thread -pthread
#
#all: BGSclient
#	g++ -o bin/BGSclient bin/BGSclient.o bin/ConnectionHandler.o bin/echoClient.o bin/userReader.o bin/serverReader.o  $(LDFLAGS)
#
#BGSclient: bin/BGSclient.o bin/ConnectionHandler.o bin/echoClient.o bin/userReader.o bin/serverReader.o
#	@echo 'Building target: main'
#	@echo 'Invoking: C++ Linker'
#	g++ -o bin/BGSclient bin/ConnectionHandler.o bin/userReader.o bin/serverReader.o bin/BGSclient.o $(LDFLAGS)
#	@echo 'Finished building target: main'
#	@echo ' '
#
#bin/BGSclient.o: src/BGSclient.cpp
#	g++ $(CFLAGS) -o bin/BGSclient.o src/BGSclient.cpp
#
#bin/ConnectionHandler.o: src/ConnectionHandler.cpp
#	g++ $(CFLAGS) -o bin/ConnectionHandler.o src/ConnectionHandler.cpp
#
#bin/echoClient.o: src/echoClient.cpp
#	g++ $(CFLAGS) -o bin/echoClient.o src/echoClient.cpp
#
#bin/userReader.o: src/userReader.cpp
#	g++ $(CFLAGS) -o bin/userReader.o src/userReader.cpp
#bin/serverReader.o: src/serverReader.cpp
#	g++ $(CFLAGS) -o bin/serverReader.o src/serverReader.cpp
#
#.PHONY: clean
#clean:
#	rm -f bin/*
#

CFLAGS:=-c -Wall -Weffc++ -g -std=c++11 -Iinclude
LDFLAGS:=-lboost_system

all: BGSclient
BGSclient: bin/ConnectionHandler.o bin/userReader.o bin/serverReader.o bin/BGSclient.o bin/EncoderDecoder.o

	@echo 'Building target: BGSclient'
	@echo 'Invoking: c++ Linker'

	g++ -pthread -o bin/BGSclient bin/BGSclient.o bin/serverReader.o bin/userReader.o bin/ConnectionHandler.o bin/EncoderDecoder.o $(LDFLAGS)

	@echo 'Finished building target: BGSclient'
	@echo ' '

bin/userReader.o: src/userReader.cpp
	g++ $(CFLAGS) -o bin/userReader.o src/userReader.cpp

bin/ConnectionHandler.o: src/ConnectionHandler.cpp
	g++ $(CFLAGS) -o bin/ConnectionHandler.o src/ConnectionHandler.cpp

bin/serverReader.o: src/serverReader.cpp
	g++ $(CFLAGS) -o bin/serverReader.o src/serverReader.cpp

bin/BGSclient.o: src/BGSclient.cpp
	g++ $(CFLAGS) -o bin/BGSclient.o src/BGSclient.cpp

bin/EncoderDecoder.o: src/EncoderDecoder.cpp
	g++ $(CFLAGS) -o bin/EncoderDecoder.o src/EncoderDecoder.cpp

.PHONY: clean
clean:
	rm -f bin/*