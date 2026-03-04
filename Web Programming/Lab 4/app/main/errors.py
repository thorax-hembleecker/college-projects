from flask import render_template
from . import main

from flask_login import current_user

@main.app_errorhandler(404)
def page_not_found(e):
    loggedin = "No"
    user = current_user
    if (current_user.is_authenticated):
        loggedin = "Yes"
    return render_template("404.html", loggedin=loggedin, user=user), 404

@main.app_errorhandler(500)
def internal_server_error(e):
    loggedin = "No"
    user = current_user
    if (current_user.is_authenticated):
        loggedin = "Yes"
    return render_template("500.html", loggedin=loggedin, user=user), 500