from flask_wtf import FlaskForm
from wtforms import StringField, PasswordField, SubmitField, IntegerField, BooleanField, FieldList
from wtforms.widgets import TextArea
from wtforms.validators import DataRequired, Length

class UserForm(FlaskForm):
    username = StringField("Username ", validators=[DataRequired()])
    password = PasswordField("Password ", validators=[DataRequired()])
    submit = SubmitField("Sign up!")

class LoginForm(FlaskForm):
    username = StringField("Username ", validators=[DataRequired()])
    password = PasswordField("Password ", validators=[DataRequired()])
    remember_me = BooleanField("Keep me logged in.")
    submit = SubmitField("Log in!")

class WritingForm(FlaskForm):
    title = StringField("Title ", validators=[DataRequired()])
    chapter = StringField("Begin writing here!", widget=TextArea(), validators=[DataRequired()])
    publish = SubmitField("Publish.")

class ChapterForm(FlaskForm):
    chapter = StringField("Begin writing here!", widget=TextArea(), validators=[DataRequired()])
    publish = SubmitField("Publish.")