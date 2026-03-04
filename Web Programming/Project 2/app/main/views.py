from flask import session, render_template, redirect, url_for, flash, request

from . import main
from .. import db
from ..models import User, Story, Chapter
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

@main.route("/signup.html", methods = ["GET", "POST"])
def signup():
    loggedin = "No"
    user = current_user
    if (current_user.is_authenticated):
        loggedin = "Yes"
    
    form = UserForm()
    print("This page exists.")

    if (form.validate_on_submit()):
        username = form.username.data
        password = form.password.data

        created = createUser(username, password)

        if (created):
            flash(username + " has signed up!")
            print(username + " has signed up!")
            user = User.query.filter_by(username = form.username.data).first()
            login_user(user, False)
        else:
            flash("User " + username + " already exists.")
            print("User " + username + " already exists.")
        return redirect(url_for("main.index"))
    else:
        print("AAAAAAAAAAAAAAAAA\n")
        print(form.errors)
    return render_template("signup.html", loggedin=loggedin, user=user, form=form)

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

    return render_template("login.html", loggedin=loggedin, user=user, form=form)

@main.route("/logout")
@login_required
def logout():
    logout_user()
    flash("Logged out!")
    return redirect(url_for('main.index'))

@main.route("/write.html", methods = ["GET", "POST"])
@login_required
def write():
    loggedin = "No"
    user = current_user
    if (current_user.is_authenticated): # Should never not be true.
        loggedin = "Yes"

    form = WritingForm()

    if (form.validate_on_submit()):
        print("Form validated")
        author_id = current_user.username
        author = current_user
        title = form.title.data
        story = createStory(author_id, author, title)

        text = form.chapter.data
        chapter = createChapter(story.id, story, text)

        if (story is not None):
            flash(title + " created!")
            print(title + "created!")
        else:
            flash("You fool. You absolute buffoon.")
            print("You fool. You absolute buffoon.")

        if (chapter is not None):
            flash("Chapter created!")
            print("Chapter created!")
        else:
            flash("No chapter.")
            print("No chapter.")

        return redirect("/" + str(story.id) + "/chapter/1")
    else:
        print("I have made a great error.")
        print(form.title.data)
        print(form.chapter.data)
        print(form.is_submitted())
        print(form.validate())
        print(form.errors)

    return render_template("write.html", loggedin=loggedin, user=user, form=form)

@main.route("/users/<username>")
def user(username):
    loggedin = "No"
    user = current_user
    if (current_user.is_authenticated):
        loggedin = "Yes"

    author = User.query.filter_by(username=username).first()
    return render_template("user.html", loggedin=loggedin, user=user, author=author)

@main.route("/browse")
def browse():
    loggedin = "No"
    user = current_user
    if (current_user.is_authenticated):
        loggedin = "Yes"

    stories = Story.query.all()
    return render_template("browse.html", loggedin=loggedin, user=user, stories=stories)

@main.route("/<id>/chapter/<chapter_num>")
def story(id, chapter_num):
    loggedin = "No"
    user = current_user
    if (current_user.is_authenticated):
        loggedin = "Yes"

    story = Story.query.filter_by(id=id).first()
    return render_template("story.html", loggedin=loggedin, user=user, story=story, chapter_num=chapter_num)

@main.route("/<id>/new", methods = ["GET", "POST"])
@login_required
def newchapter(id):
    loggedin = "No"
    user = current_user
    if (current_user.is_authenticated):
        loggedin = "Yes"

    form = ChapterForm()

    story = Story.query.filter_by(id=id).first()
    chapter_num = len(story.chapters) + 1

    if (form.validate_on_submit()):
        print("Successfully added chapter!")
        text = form.chapter.data
        chapter = createChapter(story.id, story, text)

        if (chapter is not None):
            print("Yippee!")
        else:
            print("I have failed you.")

        return redirect("/" + str(story.id) + "/chapter/" + str(chapter_num))
    else:
        print("OH NO OH NO OH NO OH NO OH NO OH NO")
        print(form.errors)

    return render_template("newchapter.html", loggedin=loggedin, user=user, form=form, story=story)

@main.route("/deletechapter/<id>/<story_id>/chapter/<chapter_num>")
@login_required
def deletechapter(id, story_id, chapter_num):
    chapter = Chapter.query.filter_by(id=id).first()
    db.session().delete(chapter)
    db.session().commit()
    if (chapter_num == 1):
        return redirect("/" + story_id + "/chapter/" + str(int(chapter_num)))
    else:
        return redirect("/" + story_id + "/chapter/" + str(int(chapter_num) - 1))

@main.route("/delete/<id>")
@login_required
def delete(id):
    story = Story.query.filter_by(id=id).first()
    db.session().delete(story)
    db.session().commit()
    return redirect(url_for('main.user', username=current_user.username))

def createUser(username, password):
    u = User.query.filter_by(username=username).first()
    if (u == None):
        id = random.randint(0, 10000000000)
        user = User(id=id, username=username, password=password)
        db.session.add(user)
        db.session.commit()
        print("Yay!!!")
        return True
    else:
        print("WHAT")
        return False

def createStory(author_id, author, title):
    story = Story(author_id=author_id, author=author, title=title)
    db.session.add(story)
    db.session.commit()
    return story

def createChapter(story_id, story, text):
    chapter = Chapter(story_id=story_id, story=story, text=text)
    db.session.add(chapter)
    db.session.commit()
    return chapter