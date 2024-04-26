import threading
import time
import random

import socket

def server():
    try:
        ss = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        print("[S]: Server socket created")
    except socket.error as err:
        print('socket open error: {}\n'.format(err))
        exit()

    server_binding = ('', 50007)
    ss.bind(server_binding)
    ss.listen(1)
    host = socket.gethostname()
    print("[S]: Server host name is {}".format(host))
    localhost_ip = (socket.gethostbyname(host))
    print("[S]: Server IP address is {}".format(localhost_ip))
    csockid, addr = ss.accept()
    print ("[S]: Got a connection request from a client at {}".format(addr))

    # send a intro message to the client.  
    msg = "Welcome to CS 352!"
    msg = msg[::-1]
    csockid.send(msg.encode('utf-8'))

    # Open a file to write
    output = open('out-proj.txt', 'w')

    # Receive data from client
    data_from_client=csockid.recv(100) # receive HELLO
        
    # Send reversed data to client
    data_from_client = data_from_client[::-1]
    print("[S]: Data received from client {}".format(data_from_client.decode('utf-8')))
    output_to_file = data_from_client.decode('utf-8')
    csockid.send(output_to_file.encode())
    print ("[S]: Data sent to client {}".format(data_from_client.decode('utf-8')))
    
    # Read lines sent from client
    data_from_client = csockid.recv(200)
    while data_from_client:
        data_from_client = data_from_client[::-1]
        output.writelines(data_from_client.decode('utf-8')+"\n") # write to output file
        data_from_client = csockid.recv(200)
        
    # Close output file
    output.close()

    # Close the server socket
    ss.close()
    exit()

if __name__ == "__main__":
    t1 = threading.Thread(name='server', target=server)
    t1.start()