from flask_wtf import FlaskForm
from wtforms import StringField, PasswordField, SubmitField, IntegerField, BooleanField, FieldList
from wtforms.widgets import TextArea
from wtforms.validators import DataRequired, Length

class UserForm(FlaskForm):
    username = StringField("Username: ", validators = [DataRequired()])
    password = PasswordField("Password: ", validators = [DataRequired()])
    displayname = StringField("Display name: ", validators = [DataRequired()])
    bio = StringField("Write a short bio: ", widget=TextArea(), validators = [DataRequired()])
    submit = SubmitField("Sign up!")

class PageForm(FlaskForm):
    title = StringField("What title would you like to give your page? ", validators = [DataRequired()])
    body = StringField("Type the contents of your page here: ", widget=TextArea(), validators = [DataRequired()])
    references = StringField("Type a list of sources for your page, if applicable. Separate sources by commas.")
    url = StringField("What URL would you like to assign your page? E.g. \"Wikipedia\" for .../wiki/Wikipedia. ", validators = [DataRequired()])
    submit = SubmitField("Save page!")

class LoginForm(FlaskForm):
    username = StringField("Username: ", validators = [DataRequired()])
    password = PasswordField("Password: ", validators = [DataRequired()])
    remember_me = BooleanField("Keep me logged in")
    submit = SubmitField("Log me in!")