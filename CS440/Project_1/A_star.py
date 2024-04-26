class A_star():
    def __init__(self, parent = None, position = None):
        self.parent = parent
        self.position = position

        #define each node g h f distance
        self.g = 0
        self.h = 0
        self.f = 0
    
    def __eq__(self, other):
        return self.position == other.position

def shortest(start, target):
    open_list = []   #use heap to initialize open list
    close_list = []
    
    start = A_star(None, start)   #starting node has g() distance of 0
    start.g = 0
    start.h = 0
    start.f = 0

    target.g = 0
    target.h = 0
    target.f = 0

    #add start cell first
    open_list.append(start)

    while len(open_list) > 0:
        current_index = 0


