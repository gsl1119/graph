from flask import Flask

from apps.api import common_dp
from ext import db
from ext.bcrypt import bcrypt
from ext.jwt import jwt
from settings import DevelopmentConfig


def create_app():
    app = Flask(__name__)
    # 加载配置
    app.config.from_object(DevelopmentConfig)
    app.register_blueprint(common_dp)
    db.init_app(app)  # 将db对象与app进行了关联
    jwt.init_app(app)
    bcrypt.init_app(app)
    # 注册蓝图
    return app
