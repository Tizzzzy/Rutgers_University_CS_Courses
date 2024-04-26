import threading
import time
import random

import socket

def client():
    try:
        cs = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        print("[C]: Client socket created")
    except socket.error as err:
        print('socket open error: {} \n'.format(err))
        exit()
        
    # Define the port on which you want to connect to the server
    port = 50007
    localhost_addr = socket.gethostbyname(socket.gethostname())

    # connect to the server on local machine
    server_binding = (localhost_addr, port)
    cs.connect(server_binding)

    # Receive data from the server
    data_from_server=cs.recv(100)
    print("[C]: Data received from server: {}".format(data_from_server.decode('utf-8')))
    
    # Send data to the server
    msg = "HELLO"
    cs.send(msg.encode('utf-8'))
    print("[C]: Data sent to server: {}".format(msg))

    time.sleep(0.5)
    # Read lines from input file
    count = 0
    input_file = open('in-proj.txt', 'r')
    Lines = input_file.read().splitlines()

    # Send lines from input file to server
    for line in Lines:
        count += 1
        cs.send(line.encode('utf-8'))
        print("Line{}: {}".format(count, line))
        time.sleep(0.5)

    # Receive data from the server
    data_from_server=cs.recv(100)
    print("[C]: Data received from server: {}".format(data_from_server.decode('utf-8')))

    # close the client socket
    cs.close()
    exit()

if __name__ == "__main__":
    time.sleep(random.random() * 5)
    t2 = threading.Thread(name='client', target=client)
    t2.start()

    time.sleep(5)
    print("Done.")