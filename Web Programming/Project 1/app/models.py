# Hopefully this works lol

from . import db
from werkzeug.security import generate_password_hash, check_password_hash
from flask_login import UserMixin

from . import login_manager

class User(UserMixin, db.Model): # User in DB example had "UserMixin" as well, find out what that does
    __tablename__ = "user"
    id = db.Column(db.Integer, primary_key=True)
    
    username = db.Column(db.String(64), unique=True, index=True) # What does "index" mean here?
    displayname = db.Column(db.String(64), unique=True, index=True) # What does "index" mean here?
    password_hash = db.Column(db.String(128))
    bio = db.Column(db.Text)
    pages = db.relationship("Page", back_populates="creator") # backref is deprecated, so use back_populates

    def __repr__(self): # This is a string representation of the object.
        return "<user %r>" % self.username
    
    @property
    def password(self):
        raise AttributeError("password is not a readable attribute")
        
    @password.setter
    def password(self, password):
        self.password_hash = generate_password_hash(password)
        
    def verify_password(self, password):
        return check_password_hash(self.password_hash, password)

class Page(db.Model):
    __tablename__ = "page"
    url = db.Column(db.String(64), unique=True, index=True, primary_key=True) # Again look at "index"
    title = db.Column(db.String(256))
    body = db.Column(db.Text)
    references = db.Column(db.Text)
    creator_id = db.Column(db.String(64), db.ForeignKey("user.username"))
    creator = db.relationship("User", back_populates="pages")

    def __repr__(self):
        return "Page " + self.title + " created by " + self.creator + " at " + self.url

@login_manager.user_loader
def load_user(user_id):
    return User.query.get(int(user_id))