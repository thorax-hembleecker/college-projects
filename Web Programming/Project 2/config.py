import os

basedir = os.path.abspath(os.path.dirname(__file__))

class Config:

    @staticmethod
    def init_app(app):
        app.config["SESSION_PERMANENT"] = False
        app.config["SESSION_TYPE"] = "filesystem"
        app.config["SECRET_KEY"] = "hard to guess string"
        app.config["SQLALCHEMY_DATABASE_URI"] = "sqlite:///" + os.path.join(basedir, "data.sqlite")
        app.config["SQLALCHEMY_TRACK_MODIFICATIONS"] = False
        # app.config["TEMPLATES_AUTO_RELOAD"] = True
        # app.config["SEND_FILE_MAX_AGE_DEFAULT"] = 0

class DevelopmentConfig(Config):
    # Development initialization
    DEBUG = True

class TestingConfig(Config):
    # Testing initialization
    TESTING = True

class ProductionConfig(Config):
    # Production initialization
    DEBUG = False

config = {
    "development": DevelopmentConfig,
    "testing": TestingConfig,
    "production": ProductionConfig,
    "default": ProductionConfig
}