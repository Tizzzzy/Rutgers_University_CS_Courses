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