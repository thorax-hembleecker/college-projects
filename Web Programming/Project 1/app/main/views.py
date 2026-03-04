# from flask import Flask, render_template, url_for

# app = Flask(__name__)


from flask import session, render_template, redirect, url_for, flash, request

from . import main
from .. import db
from ..models import User, Page
from .forms import *
from flask_login import login_required, current_user, login_user, logout_user
import random

@main.route("/index.html")
def index():
    loggedin = "No"
    user = current_user
    if (current_user.is_authenticated):
        loggedin = "Yes"
    return render_template("index.html", loggedin=loggedin, user=user)

@main.route("/users/<id>")
def user(id):
    loggedin = "No"
    user = current_user
    if (current_user.is_authenticated):
        loggedin = "Yes"
    return render_template("user.html", userPage=User.query.filter_by(username=id).first(), loggedin=loggedin, user=user) # Not sure if "first" is needed given that usernames are unique.

@main.route("/wiki/<id>")
def page(id):
    loggedin = "No"
    user = current_user
    if (current_user.is_authenticated):
        loggedin = "Yes"
    return render_template("page.html", page=Page.query.filter_by(url=id).first(), loggedin=loggedin, user=user)


@main.route("/pages.html", methods = ["GET", "POST"])
def pages():
    loggedin = "No"
    user = current_user
    if (current_user.is_authenticated): # Should always be the case.
        loggedin = "Yes"
    
    return render_template("pages.html", pages=Page.query.all(), loggedin=loggedin, user=user)

@main.route("/users.html", methods = ["GET", "POST"])
def users():
    loggedin = "No"
    user = current_user
    if (current_user.is_authenticated): # Should always be the case.
        loggedin = "Yes"
    
    return render_template("users.html", users=User.query.all(), loggedin=loggedin, user=user)

@main.route("/login.html", methods = ["GET", "POST"])
def login():
    loggedin = "No"
    user = current_user
    if (current_user.is_authenticated): # This should never be the case, but if something weird happens it's here.
        loggedin = "Yes"
    
    form = LoginForm()

    if (form.validate_on_submit()):
        user = User.query.filter_by(username = form.username.data).first()
        if user is not None and user.verify_password(form.password.data):
            login_user(user, form.remember_me.data)
            next = request.args.get("next")
            if next is None or not next.startswith("/"):
                next = url_for("main.index")
            return redirect(next)
        flash("Invalid login.")
    return render_template("login.html", form=form, loggedin=loggedin, user=user)

@main.route("/logout")
@login_required
def logout():
    logout_user()
    flash("You have been logged out.")
    return redirect(url_for("main.index"))

@main.route("/newuser.html", methods = ["GET", "POST"])
def newUser():
    loggedin = "No"
    user = current_user
    if (current_user.is_authenticated): # Again, this should never be the case, but...
        loggedin = "Yes"

    form = UserForm()

    if (form.validate_on_submit()):
        username = form.username.data
        displayname = form.displayname.data
        bio = form.bio.data
        password = form.password.data
        created = createUser(username, displayname, password, bio)
        if (created):
            flash(username + " has signed up!")
            user = User.query.filter_by(username = form.username.data).first()
            login_user(user, False)
        else:
            flash("User " + username + " already exists. Try a different username.")
        return redirect(url_for("main.index"))
    return render_template("newuser.html", form=form, loggedin=loggedin, user=user)

@main.route("/page-editor.html", methods = ["GET", "POST"])
@login_required
def newPage():
    loggedin = "No"
    user = current_user
    if (current_user.is_authenticated): # Should always be the case.
        loggedin = "Yes"

    form = PageForm()

    if (form.validate_on_submit()):
        url = form.url.data
        title = form.title.data
        body = form.body.data
        references = form.references.data
        creator_id = current_user.username
        creator = current_user
        created = createPage(url, title, body, references, creator_id, creator)
        if (created):
            flash("Page " + title + " has been created!")
        else:
            flash("A page already exists at this URL. Please choose another.")
        return redirect("/wiki/" + url)
    return render_template("page-editor.html", form=form, loggedin=loggedin, user=user)

def createUser(username, displayname, password, bio): # Hashing happens via @password.setter in models.py
    u = User.query.filter_by(username=username).first()
    if(u == None):
        user_id = random.randint(0, 10000000000)
        user = User(id = user_id, username = username, displayname = displayname, password = password, bio = bio)
        db.session.add(user)
        db.session.commit()
        return True
    else:
        return False
    
def createPage(url, title, body, references, creator_id, creator):
    p = Page.query.filter_by(url=url).first()
    if (p == None):
        page = Page(url = url, title = title, body = body, references = references, creator_id = creator_id, creator = creator)
        # creator = User.query.filter_by(username=creator_id).first()
        # page.creator = creator
        db.session.add(page)
        db.session.commit()
        return True
    else:
        return False