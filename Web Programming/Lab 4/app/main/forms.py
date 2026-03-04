from flask_wtf import FlaskForm
from wtforms import StringField, PasswordField, SubmitField, IntegerField, BooleanField, FieldList
from wtforms.widgets import TextArea
from wtforms.validators import DataRequired, Length

class UserForm(FlaskForm):
    username = StringField("Username: ", validators = [DataRequired()])
    password = PasswordField("Password: ", validators = [DataRequired()])
    submit = SubmitField("Sign up!")

class LoginForm(FlaskForm):
    username = StringField("Username: ", validators = [DataRequired()])
    password = PasswordField("Password: ", validators = [DataRequired()])
    remember_me = BooleanField("Keep me logged in")
    submit = SubmitField("Log me in!")