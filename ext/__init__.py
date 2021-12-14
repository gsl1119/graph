# 创建一个映射对象 db
from flask_sqlalchemy import SQLAlchemy
from flask_bcrypt import Bcrypt
from flask_jwt_extended import JWTManager
from flask_cors import CORS
from flask_migrate import Migrate  # 最新版的不在支持MigrateCommand
from flask_script import Manager

db = SQLAlchemy()
manager = Manager()


def init_ext(app):
    cors = CORS(app, resources=r'/*')
    migrate = Migrate(app=app, db=db)
    jwt = JWTManager(app)
    bcrypt = Bcrypt(app)
    return cors, migrate, jwt, bcrypt
