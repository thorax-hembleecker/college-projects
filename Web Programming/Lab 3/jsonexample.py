import json

f = open("users.json")

data = json.load(f)

for i in data.keys():
    curr = data[i]
    print(curr["fname"], curr["lname"])
    
f.close()