# README !!!
# run this line in command prompt first before running the pymongo, because the data is too big
# > use admin
# > db.adminCommand({setParameter: 1, internalQueryExecMaxBlockingSortBytes:501514320})

import pymongo

client = pymongo.MongoClient()

mydb = client["db"]

mycol = mydb["Penna"]

total = 0
count = 0
lis = []
time = []
cur = list(mycol.find().sort("Timestamp", pymongo.ASCENDING))

for i in range(len(cur)):
    if i == len(cur)-1:
        total += cur[i]["totalvotes"]
        count += 1
        break
    elif cur[i]["Timestamp"] == cur[i+1]["Timestamp"]:
        total += cur[i]["totalvotes"]
        count += 1
    else:
        key = cur[i]["Timestamp"]
        total += cur[i]["totalvotes"]
        lis.append(total)
        time.append(key)
        total = 0
        count += 1

Increment = []
Increment.append(lis[0])
for x in range(1, len(lis)):
    diff = lis[x] - lis[x-1]
    Increment.append(diff)

max = max(Increment)
index = Increment.index(max)
# print(len(time), len(Increment))
# print(index)
print(time[index], " : ", max)
