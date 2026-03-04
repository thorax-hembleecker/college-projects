# Hopefully this works lol

from flask import Flask, request, session, render_template, redirect, url_for, flash
from flask_sqlalchemy import SQLAlchemy
from flask_session import Session
from config import config
from flask_login import LoginManager

login_manager = LoginManager()
login_manager.login_view = "main.login"

db = SQLAlchemy() # I believe this is where models is getting "from . import db"

def create_app(config_name):
    app = Flask(__name__)
    app.config.from_object(config[config_name])
    config[config_name].init_app(app)
    
    db.init_app(app)
    login_manager.init_app(app)
    
    from .main import main as main_blueprint # This is the part I'm not really sure about
    app.register_blueprint(main_blueprint)

    ### ADDED BY ME ###
    with app.app_context():
        db.create_all()
    ### ADDED BY ME ###
    
    return app