from . import db
from werkzeug.security import generate_password_hash, check_password_hash
from flask_login import UserMixin
from sqlalchemy.dialects.postgresql import ARRAY

from . import login_manager

class User(UserMixin, db.Model):
    __tablename__ = "user"
    id = db.Column(db.Integer, primary_key=True)

    username = db.Column(db.String(64), unique=True, index=True) # index=True creates an index in the DB for this column, speeding up queries.
    password_hash = db.Column(db.String(128))
    stories = db.relationship("Story", back_populates="author")

    def __repr__(self): # String representation of the object, is it required?
        return "<user %r>" % self.username
    
    @property
    def password(self):
        raise AttributeError("password is not a readable attribute")
    
    @password.setter
    def password(self, password):
        self.password_hash = generate_password_hash(password)

    def verify_password(self, password):
        return check_password_hash(self.password_hash, password)
    
class Story(db.Model):
    __tablename__ = "story"
    id = db.Column(db.Integer, primary_key=True)

    author_id = db.Column(db.String(64), db.ForeignKey("user.username"))
    author = db.relationship("User", back_populates="stories")
    title = db.Column(db.String(200), index=True)
    # chapters = db.Column(ARRAY(db.Text))
    # chapters = db.Column(db.Text)
    chapters = db.relationship("Chapter", back_populates="story")

class Chapter(db.Model):
    __tablename__ = "chapter"
    id = db.Column(db.Integer, primary_key=True)
    story_id = db.Column(db.String(64), db.ForeignKey("story.id"))
    story = db.relationship("Story", back_populates="chapters")
    text = db.Column(db.Text)
    
@login_manager.user_loader
def load_user(user_id):
    return User.query.get(int(user_id))