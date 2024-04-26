#!/usr/bin/python

"""
This file creates a mininet network with one router and 4 hosts in two
subnets. The topology is as follows.

h1      h2
  \   /
    R1
  /   \
h3      h4

h1 and h3 are in the 10.0.0.0/24 subnet, while h2 and h4 are in the
192.168.0.0/24 subnet.

The connectivity among network interfaces looks like that:

r1-eth1 -- h1-eth0 
r1-eth2 -- h2-eth0 
r1-eth3 -- h3-eth0
r1-eth4 -- h4-eth0

where each row is a link, and each column in each row is the name of a
network interface (usefully labeled with the machine on which the
interface resides).

"""

from mininet.topo import Topo
from mininet.net import Mininet
from mininet.node import Node
from mininet.log import setLogLevel, info
from mininet.cli import CLI

class LinuxRouter( Node ):
    "A Node with IP forwarding enabled."

    def config( self, **params ):
        super( LinuxRouter, self).config( **params )
        # Enable forwarding on the router
        self.cmd( 'sysctl net.ipv4.ip_forward=1' )
        self.cmd( 'sysctl net.ipv4.conf.all.proxy_arp=1' )

    def terminate( self ):
        self.cmd( 'sysctl net.ipv4.ip_forward=0' )
        self.cmd( 'sysctl net.ipv4.conf.all.proxy_arp=0' )
        super( LinuxRouter, self ).terminate()

class NetworkTopo ( Topo ):
    """ A linux router connecting 4 hosts in two IP subnets. """
    def build( self, **_opts ):
        r1 = self.addNode( 'r1', cls=LinuxRouter, ip=None )
        h1 = self.addHost( 'h1', ip=None )
        h2 = self.addHost( 'h2', ip=None )
        h3 = self.addHost( 'h3', ip=None)
        h4 = self.addHost( 'h4', ip=None )
        self.addLink( h1, r1, intfName2='r1-eth1' )
        self.addLink( h2, r1, intfName2='r1-eth2' )
        self.addLink( h3, r1, intfName2='r1-eth3' )
        self.addLink( h4, r1, intfName2='r1-eth4' )
    
def run():
     """ Test the router and interface configurations. """
     topo = NetworkTopo()
     net = Mininet( topo=topo )
     net.start()
     info( '*** Routing Table on Router:\n' )
     info( net[ 'r1' ].cmd( 'route' ) )
     CLI( net )
     net.stop()

if __name__ == "__main__":
    setLogLevel( 'info' )
    run()
    
