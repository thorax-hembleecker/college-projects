from flask import session, render_template, redirect, url_for, flash, request

from . import main
from .. import db
from ..models import User
from .forms import *
from flask_login import login_required, current_user, login_user, logout_user
import random

@main.route("/")
def index():
    loggedin = "No"
    user = current_user
    if (current_user.is_authenticated):
        loggedin = "Yes"
    return render_template("index.html", loggedin=loggedin, user=user)

@main.route("/coolpage")
@login_required
def coolpage():
    loggedin = "No"
    user = current_user
    if (current_user.is_authenticated):
        loggedin = "Yes"
        return render_template("coolpage.html", loggedin=loggedin, user=user)
    return render_template("login.html", loggedin=loggedin, user=user)

@main.route("/users/<id>")
@login_required
def user(id):
    loggedin = "No"
    user = current_user
    if (current_user.is_authenticated):
        loggedin = "Yes"
    return render_template("user.html", loggedin=loggedin, user=user)

@main.route("/login.html", methods = ["GET", "POST"])
def login():
    loggedin = "No"
    user = current_user
    if (current_user.is_authenticated):
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

@main.route("/signup", methods = ["GET", "POST"])
def signup():
    loggedin = "No"
    user = current_user
    if (current_user.is_authenticated):
        loggedin = "Yes"
    
    form = UserForm()
    
    if (form.validate_on_submit()):
        username = form.username.data
        password = form.password.data
        created = createUser(username, password)
        if (created):
            flash(username + " has signed up.")
            user = User.query.filter_by(username = form.username.data).first()
            login_user(user, False)
        else:
            flash("User " + username + " already exists, you dingus.")
        return redirect(url_for("main.index"))
    return render_template("signup.html", form=form, loggedin=loggedin, user=user)

@main.route("/logout")
@login_required
def logout():
    logout_user()
    flash("You have been logged out.")
    return redirect(url_for("main.logout_page"))

@main.route("/loggedout")
def logout_page():
    loggedin = "No"
    user = current_user
    if (current_user.is_authenticated):
        loggedin = "Yes"
    return render_template("logout.html", loggedin=loggedin, user=user)

def createUser(username, password):
    u = User.query.filter_by(username=username).first()
    if (u == None):
        user_id = random.randint(0, 10000000000)
        user = User(id = user_id, username = username, password = password)
        db.session.add(user)
        db.session.commit()
        return True
    else:
        return False