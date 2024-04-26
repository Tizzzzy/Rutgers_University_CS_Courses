import threading
import time
import random
import socket
import argparse

# Settings
# Maximum chunk size used for transmission.
# This value must be larger than the chunk size used in the
# sender. Currently, we set this to 100.
MAX_CHUNK_SIZE = 100
# dummy SEQ number used for receiver's ack packets
__SEQ_UNUSED = 235347
# Loss emulation default settings.
# Lose each N'th incoming packet
pkt_counter_eN = 0
ack_counter_eN = 0
pkt_eN_N = 3
ack_eN_N = 4
# Lose two packets separated by one successful transmission
# every M packets
pkt_counter_aeM = 0
ack_counter_aeM = 0
pkt_aeM_M = 4
ack_aeM_M = 5
# IID loss with probability 1/N
pkt_iid_N = 3
ack_iid_N = 4
# Default loss types
pkt_losstype = 'everyN'
ack_losstype = 'everyN'

# Message class: we use this class to structure our protocol
# message. The fields in our protocol are:
# seq no: the starting sequence number of application bytes
# on this packet
# ack no: the cumulative ACK number of application bytes
# being acknowledged in this ACK
# len: the number of application bytes being transmitted on
# this packet
# msg: the actual application payload on this packet
# The methods `serialize` and `deserialize` allow the
# conversion of a protocol object to bytes transmissible
# through a sendto() system call and the bytes from a
# recvfrom() system call into a protocol structure.
class Msg:
    def __init__(self, seq, ack, msg):
        self.seq = int(seq)
        self.ack = int(ack)
        self.msg = str(msg)
        self.len = len(self.msg)

    def serialize(self):
        ser_repr = (str(self.seq) + ' | ' + str(self.ack) + ' | ' +
                    str(self.len) + ' | ' + self.msg)
        return ser_repr.encode('utf-8')

    def __str__(self):
        repr  = "Seq: " + str(self.seq) + '   '
        repr += "ACK: " + str(self.ack) + '   '
        repr += "Len: " + str(self.len) + '   '
        repr += "Msg: " + self.msg.strip()
        return repr

    @staticmethod
    def deserialize(ser_bytes_msg):
        ser_msg = ser_bytes_msg.decode('utf-8')
        parts = ser_msg.split('|')
        if len(parts) >= 4:
            return Msg(int(parts[0]),
                       int(parts[1]),
                       '|'.join(parts[3:])[1:])
        else:
            print("Error in deserializing into Msg object.")
            exit(-1)

## Helper methods.
### Argument parsing
def parse_args():
    parser = argparse.ArgumentParser()
    parser.add_argument('--pktloss',
                        help = "Emulated loss behavior on packets"
                               " (default every n packets)",
                        default = 'everyn',
                        choices = ['noloss', 'everyn',
                                   'alteveryn', 'iid'])
    parser.add_argument('--ackloss',
                        help = "Emulated loss behavior on ACKs"
                               " (default noloss)",
                        default = 'noloss',
                        choices = ['noloss', 'everyn',
                                   'alteveryn', 'iid'])
    parser.add_argument('--pktlossN',
                        type = int,
                        default = pkt_eN_N,
                        help = "n for pkt loss behaviors "
                               "(only if loss specified)")
    parser.add_argument('--acklossN',
                        type = int,
                        default = ack_eN_N,
                        help = "n for ack loss behaviors "
                               "(only if loss specified)")
    parser.add_argument('--ooo_enabled',
                        action = "store_true",
                        dest = "ooo_enabled",
                        help = "enable out of order data buffering (default false)")
    parser.add_argument('--port',
                        type = int,
                        help = "receiver local port to bind to (default 50007)",
                        default = 50007)
    parser.add_argument('--outfile',
                        type = str,
                        help = "name of output file (default test-output.txt)",
                        default = "test-output.txt")
    args = parser.parse_args()
    return vars(args)

def set_loss_params(args):
    global pkt_losstype, ack_losstype
    global pkt_eN_N, pkt_aeM_M, pkt_iid_N
    global ack_eN_N, ack_aeM_M, ack_iid_N
    # Packet loss parameters
    pkt_losstype = args['pktloss']
    pkt_lossparam = args['pktlossN']
    if pkt_losstype == 'everyn':
        pkt_eN_N = pkt_lossparam
    elif pkt_losstype == 'alteveryn':
        if (pkt_lossparam < 4):
            print ("[R] Error: pktlossN must be >= 4 for alteveryn")
            exit(-1)
        pkt_aeM_M = pkt_lossparam
    elif pkt_losstype == 'iid':
        pkt_iid_N = pkt_lossparam
    else:
        assert(pkt_losstype == 'noloss')
    # ACK loss parameters
    ack_losstype = args['ackloss']
    ack_lossparam = args['acklossN']
    if ack_losstype == 'everyn':
        ack_eN_N = ack_lossparam
    elif ack_losstype == 'alteveryn':
        if (ack_lossparam < 4):
            print ("[R] Error: acklossN must be >= 4 for alteveryn")
            exit(-1)
        ack_aeM_M = ack_lossparam
    elif ack_losstype == 'iid':
        ack_iid_N = ack_lossparam
    else:
        assert(ack_losstype == 'noloss')

### Construct a default ACK message given an original
### message `msg`.
def construct_ack(msg):
    ack_num = msg.seq + msg.len
    return Msg(__SEQ_UNUSED, ack_num, '')

### Loss emulation methods
def noLoss():
    return False

def pkt_everyN():
    global pkt_counter_eN
    pkt_counter_eN = (pkt_counter_eN + 1) % pkt_eN_N
    return pkt_counter_eN == (pkt_eN_N - 1)

def ack_everyN():
    global ack_counter_eN
    ack_counter_eN = (ack_counter_eN + 1) % ack_eN_N
    return ack_counter_eN == (ack_eN_N - 1)

def pkt_alternateEveryM():
    global pkt_counter_aeM
    pkt_counter_aeM = (pkt_counter_aeM + 1) % pkt_aeM_M
    return pkt_counter_aeM in [pkt_aeM_M - 1,
                               pkt_aeM_M - 3]

def ack_alternateEveryM():
    global ack_counter_aeM
    ack_counter_aeM = (ack_counter_aeM + 1) % ack_aeM_M
    return ack_counter_aeM in [ack_aeM_M - 1,
                               ack_aeM_M - 3]

def pkt_iid():
    return random.randint(1, pkt_iid_N) == 1

def ack_iid():
    return random.randint(1, ack_iid_N) == 1

pkt_loss_funs = {'noloss': noLoss,
                 'everyn': pkt_everyN,
                 'alteveryn': pkt_alternateEveryM,
                 'iid': pkt_iid}

ack_loss_funs = {'noloss': noLoss,
                 'everyn': ack_everyN,
                 'alteveryn': ack_alternateEveryM,
                 'iid': ack_iid}

def pkt_loss_verdict():
    return pkt_loss_funs[pkt_losstype]()

def ack_loss_verdict():
    return ack_loss_funs[ack_losstype]()

### Methods that use loss emulation while receiving packets
### or even when sending ACKs. The receiver uses these
### methods in place of the usual `recvfrom()` and `sendto()`
### to emulate the loss of packets received or ACKs sent.
def lossy_recvfrom(ss, nbytes):
    data = None
    sender = None
    while data is None:
        loss = pkt_loss_verdict()
        if not loss:
            (data, sender) = ss.recvfrom(nbytes)
        else: # receive anyway, but discard.
            ss.recvfrom(nbytes)
    return data, sender

def lossy_sendto(ss, msg, sender_addr):
    loss = ack_loss_verdict()
    if not loss:
        ss.sendto(msg.serialize(), sender_addr)
        print ("Transmitted {}".format(str(msg)))
        
### Init socket
def init_socket(local_receiver_port):
    try:
        ss = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        print("[R]: Receiver socket created")
    except socket.error as err:
        print('socket open error: {}\n'.format(err))
        exit()
    ss.bind(('', local_receiver_port))
    return ss

### Put filedata
def put_filedata(filename, filedata):
    print ("[R] Writing results into {}".format(filename))
    f = open(filename, 'w')
    f.write(filedata)
    f.close()

############################################
# Main receive loop
def receiver(ss, ooo_enabled):
    def get_msg_ack():
        data_from_sender, sender_addr = lossy_recvfrom(
            ss, MAX_CHUNK_SIZE)
        msg = Msg.deserialize(data_from_sender)
        print ("Received    {}".format(str(msg)))
        ack_msg = construct_ack(msg)
        return msg, ack_msg, sender_addr

    # Retrieve and ACK the first message. Obtain the message
    # length which is transmitted in this message.
    msg, ack_msg, sender_addr = get_msg_ack()
    lossy_sendto(ss, ack_msg, sender_addr) # ACK
    try:
        total_bytes = int(msg.msg)
    except:
        print ("Error: File length invalid! quitting.")
        ss.close()
        exit(-1)

    # Receive and ACK the subsequent (data) packets
    output = ''   # final result of the download
    ooo_data = {} # out of order data buffer, only active if
                  # ooo_enabled is set.
    last_seq_expected = msg.seq + len(msg.msg) + total_bytes - 1
    last_acked = msg.seq + msg.len

    while last_acked <= last_seq_expected:
        msg, ack_msg, sender_addr = get_msg_ack()
        last_seq_in_msg = msg.seq + msg.len - 1
        if last_acked == msg.seq:
            # Most common case: fresh in-order data
            # add to existing data buffer
            output += msg.msg
            new_ack_num = ack_msg.ack
            # Uncommon case 1: possible that previously
            # out-of-order data is now in order. Update
            # last_acked to the value that corresponds to
            # the "latest" in-order data, given this last
            # packet that appears to fill a hole in the
            # sequence space.
            if ooo_enabled:
                while new_ack_num in ooo_data:
                    new_data = ooo_data[new_ack_num]
                    output += new_data
                    ooo_data.pop(new_ack_num)
                    new_ack_num += len(new_data)
                    print ("[R] Plugged a hole in seq space"
                           " up to seq {}".format(
                               new_ack_num))
            # cumulative ACK (last_acked) must reflect
            # latest piece of data that is in order
            last_acked = new_ack_num
        elif last_acked < msg.seq:
            # Uncommon case 2: fresh data that creates a
            # hole in the sequence space.
            print ("[R] Fresh data creating seq space hole")
            if not ooo_enabled:
                output += msg.msg
            else:
                print ("[R] Sending dup ACK")
                ooo_data[msg.seq] = msg.msg
        elif last_acked > last_seq_in_msg:
            # Retransmitted data that receiver has already
            # seen. Previously sent ACKs may have been
            # dropped. last_acked remains unchanged.
            print ("[R] Spurious retransmission of data"
                   " already at the receiver")
            print ("[R] Sending dup ACK")
        else:
            # partially fresh and partially retransmitted
            # data. We're not handling this case.
            print ("[R] Error: Receiver cannot handle mix of"
                   " fresh and retransmitted data.")
            exit(-1)
        if ooo_enabled: # Use cumulative ACKs
            ack_msg.ack = last_acked
        else: # last_acked merely reflects latest data
            # received. This is OK for a "lossy" receiver,
            # but not a reliable one.
            last_acked = ack_msg.ack
        # Send the ACK
        if last_acked <= last_seq_expected:
            lossy_sendto(ss, ack_msg, sender_addr)
        else:
            # if the last ACK is dropped, handling at the
            # sender requires separate exchanges and
            # timeouts similar to TCP FIN_WAITs. Simplify
            # our life, don't drop the very last ACK.
            ss.sendto(ack_msg.serialize(),
                      sender_addr)

    return output

if __name__ == "__main__":
    args = parse_args()
    set_loss_params(args)
    ss = init_socket(args['port'])
    output = receiver(ss, args['ooo_enabled'])
    ss.close()
    put_filedata(args['outfile'], output)
    print("[R] Receiver finished downloading file data.")
