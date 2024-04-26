# v: visited & unblock
# u; unvisited
# b: block
import copy
import random
import math
import heapq

# from cell import cell

# maze size is 101*101, so width and height should be 101
width = 8
height = 8
# we want 50 mazes, so number should be 50
number = 1
maze = []
# array to store all of the mazes
mazes = []
count = 0

s_x = 0
s_y = 0

t_x = 0
t_y = 0

block_dic = {}


# block_num = 0


class Cell:
    def __init__(self, status, x, y, f, g, h, checked):
        self.status = status
        self.x = x
        self.y = y
        self.f = f
        self.g = g
        self.h = h
        self.checked = checked

    def __str__(self):
        return self.status


def init_maze(width, height):
    for i in range(0, height):
        row = []
        for j in range(0, width):
            # initialize all cell as unvisited
            row.append('u')
        maze.append(row)

    return maze


block = []
visited = []


# generate blocking cell (1 for blocked, 0 for unblocked)
# need to generate
def generate():
    global s_x
    global s_y
    global t_x
    global t_y

    mazes.append('maze')
    block.append('BLOCK maze {}: '.format(count))
    visited.append('VISITED maze {}: '.format(count))

    unblocked_count = 0

    # make maze
    for i in range(0, len(maze)):
        for j in range(0, len(maze[i])):
            p = random.uniform(0.0, 1.0)
            if p < 0.30:
                maze[i][j] = Cell('1', i, j, math.inf, math.inf, math.inf, False)
                block.append((i, j))
                visited.append((i, j))
            else:
                maze[i][j] = Cell('0', i, j, -1, -1, -1, False)
                unblocked_count = unblocked_count + 1
                visited.append((i, j))

    # place agent and position
    agent_found = False
    target_found = False

    # simple population of target and agent
    while agent_found is False or target_found is False:
        for a in range(0, len(maze)):
            for b in range(0, len(maze[a])):
                if maze[a][b].status == '0':
                    a_prob = random.uniform(0.0, 1.0)
                    t_prob = random.uniform(0.0, 1.5)
                    if a_prob < 1.0 / unblocked_count and agent_found is False:
                        maze[a][b].status = 'A'
                        agent_found = True
                        s_x = a
                        s_y = b
                    if t_prob < 1.0 / unblocked_count and target_found is False:
                        maze[a][b].status = 'T'
                        target_found = True
                        t_x = a
                        t_y = b
            if agent_found is True and target_found is True:
                break
    # while agent_found is False:
    #     s_x = random.randint(0, len(maze)-1)
    #     s_y = random.randint(0, len(maze[0])-1)
    #     if maze[s_x][s_y].status == '0':
    #         maze[s_x][s_y].status = 'A'
    #         agent_found = True

    # while target_found is False:
    #     t_x = random.randint(0, len(maze)-1)
    #     t_y = random.randint(0, len(maze[0])-1)
    #     if maze[t_x][t_y].status == '0':
    #         maze[t_x][t_y].status = 'T'
    #         target_found = True
    # set the nodes f and g to be used later

    mazes.append(maze)


def print_maze():
    for i in range(0, len(maze)):
        for j in range(0, len(maze[i])):
            print(maze[j][i], end=" ")
        print()
    print()


# calculate the h value for each neighbor cell of the current node: distance to the goal, fixed for normal a*
def cal_h(neighbor, goal_x, goal_y):
    for node in neighbor:
        x_dis = node.x
        y_dis = node.y
        node.h = abs(x_dis - goal_x) + abs(y_dis - goal_y)


# calculate the g value for each neighbor cell of the current node: distance for a neighboor node of current node to the Start State node, not fixed, accumulative
def cal_g(neighbor, current):
    for n in neighbor:
        x_dis = n.x
        y_dis = n.y
        n.g = abs(x_dis - current.x) + abs(y_dis - current.y) + current.g


# calculate the f value for cell
def cal_f(neighbor):
    for n in neighbor:
        n.f = n.g + n.h
    # the heap push function, push path[f of current node,current node x,current node y,current node g,parent node x,parent node y] to heap


def heap_push(neighbor, current, pathheap):
    path = []
    for node in neighbor:
        path = [node.f, node.x, node.y, node.g, node.h, current.x, current.y]
        heapq.heappush(pathheap, path)
    return pathheap


def read_block_dic(block_dic):
    blocks = []
    for key in block_dic:
        blocks.append(block_dic[key])
    return blocks

# # store neighbors into the heap
def open_heap(q, cell):
    heapq.heappush(q, (cell.f, cell.x, cell.y))


def repeated_a(start_x, start_y, target_x, target_y, maze_grid):
    block_num = 0
    # since this is our start node, f and g must be 0
    maze_grid[start_x][start_y].f = 0
    maze_grid[start_x][start_y].g = 0
    # create & initialize the current node
    current_node = maze_grid[start_x][start_y]

    # initialize the path_heap(open list)[[f of current node,current node x,current node y,current node g,parent node x,parent node y]]  start state parent is itself
    path_heap = [[current_node.f, current_node.x, current_node.y, current_node.g, current_node.h, current_node.x,
                  current_node.y]]
    heapq.heapify(path_heap)
    # create & initialize the closed list
    closed_list = {}

    while path_heap:
        # pop path in path_heap (f of current node,open node,parrent node) with minimum 'f' value
        path = heapq.heappop(path_heap)
        current_node.f = path[0]
        current_node.x = path[1]
        current_node.y = path[2]
        current_node.g = path[3]
        current_node.h = path[4]

        # put current node in checked node
        checked_node = current_node
        maze_grid[checked_node.x][checked_node.y].checked = True

        # if checked node is the same as target node, you are done(have problem #)
        # if checked_node.x == target_x and checked_node.y == target_y:
        # need to get path back
        # print("Done")
        # get neighbors (check cardinal directions, check if blocked, and check if already checked)
        neighbors = []

        # all the empty neighbors
        if checked_node.x + 1 < width and \
                maze_grid[checked_node.x + 1][checked_node.y].status == '0' and \
                maze_grid[checked_node.x + 1][checked_node.y].checked is False:
            maze_grid[checked_node.x + 1][checked_node.y].checked is True
            neighbors.append(maze_grid[checked_node.x + 1][checked_node.y])

        elif checked_node.x + 1 < width and \
                maze_grid[checked_node.x + 1][checked_node.y].status == '1' and \
                maze_grid[checked_node.x + 1][checked_node.y].checked is False:
            maze_grid[checked_node.x + 1][checked_node.y].checked is True
            block_dic[block_num] = [checked_node.x + 1, checked_node.y]
            block_num += 1

        if checked_node.x - 1 >= 0 and \
                maze_grid[checked_node.x - 1][checked_node.y].status == '0' and \
                maze_grid[checked_node.x - 1][checked_node.y].checked is False:
            maze_grid[checked_node.x - 1][checked_node.y].checked is True
            neighbors.append(maze_grid[checked_node.x - 1][checked_node.y])

        elif checked_node.x - 1 >= 0 and \
                maze_grid[checked_node.x - 1][checked_node.y].status == '1' and \
                maze_grid[checked_node.x - 1][checked_node.y].checked is False:
            maze_grid[checked_node.x - 1][checked_node.y].checked is True
            block_dic[block_num] = [checked_node.x - 1, checked_node.y]
            block_num += 1

        if checked_node.y + 1 < height and \
                maze_grid[checked_node.x][checked_node.y + 1].status == '0' and \
                maze_grid[checked_node.x][checked_node.y + 1].checked is False:
            maze_grid[checked_node.x][checked_node.y + 1].checked is True
            neighbors.append(maze_grid[checked_node.x][checked_node.y + 1])

        elif checked_node.y + 1 < height and \
                maze_grid[checked_node.x][checked_node.y + 1].status == '1' and \
                maze_grid[checked_node.x][checked_node.y + 1].checked is False:
            maze_grid[checked_node.x][checked_node.y + 1].checked is True
            block_dic[block_num] = [checked_node.x, checked_node.y + 1]
            block_num += 1

        if checked_node.y - 1 >= 0 and \
                maze_grid[checked_node.x][checked_node.y - 1].status == '0' and \
                maze_grid[checked_node.x][checked_node.y - 1].checked is False:
            maze_grid[checked_node.x][checked_node.y - 1].checked is True
            neighbors.append(maze_grid[checked_node.x][checked_node.y - 1])

        elif checked_node.y - 1 >= 0 and \
                maze_grid[checked_node.x][checked_node.y - 1].status == '1' and \
                maze_grid[checked_node.x][checked_node.y - 1].checked is False:
            maze_grid[checked_node.x][checked_node.y - 1].checked is True
            block_dic[block_num] = [checked_node.x, checked_node.y - 1]
            block_num += 1

        #   give neighbor h,g,f value
        cal_h(neighbors, target_x, target_y)
        cal_g(neighbors, checked_node)
        cal_f(neighbors)

        # push the neighbor to the path_heap (addheap)
        path_heap = heap_push(neighbors, checked_node, path_heap)

        for i in range(len(path_heap)):
            print('heap: ', heapq.heappop(path_heap))

    # test
    for n in neighbors:
        print('x {}, y{}, h{}, g{} f{}, status{}: '.format(n.x, n.y, n.h, n.g, n.f, n.status))

        # check if neighbor has been inspected or can be reached with a smaller cost from current node

    # test
    print('block: ', block_dic)

    return []


# print out the initial maze
init_maze(width, height)
print_maze()

# print out the blocked maze
while count < number:
    generate()
    print_maze()
    # print("MAZES", mazes)
    # print("Block", block)
    repeated_a(s_x, s_y, t_x, t_y, maze)
    count += 1

# test
# block = read_block_dic(block_dic)'
# print(heapq)
print(block)
print(s_x, s_y, t_x, t_y)