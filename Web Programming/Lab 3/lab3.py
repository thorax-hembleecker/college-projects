from flask import Flask, render_template, url_for
import json

app = Flask(__name__)

class User:
    def __init__(self, lname, email, uid, fname, friends, dob):
        self.lname = lname
        self.email = email
        self.uid = uid
        self.fname = fname
        self.friends = friends
        self.dob = dob

f = open("users.json")
data = json.load(f)
# data = data["4264464760154519683"]
users = []

for i in data.keys():
    curr = data[i]
    # print(curr["fname"], curr["lname"])
    u = User(curr["lname"], curr["email"], curr["uid"], curr["fname"], curr["friends"], curr["dob"])
    users.append(u)
    
f.close()

@app.route("/users/<int:uid>")
def index(uid):
    if str(uid) in data.keys():
        return render_template("user.html", user = data[str(uid)], users = data)
    return render_template("404.html"), 404
    # abort(404)
    # page_not_found()
    # return 404

@app.errorhandler(404)
def page_not_found(e):
    return render_template("404.html"), 404