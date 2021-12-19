from flask import Flask

from apps.view import init_view
from ext import db, init_ext, manager, cache
from settings import DevelopmentConfig, CachingConfig


def create_app():
    app = Flask(__name__)
    # 加载配置
    app.config.from_object(DevelopmentConfig)
    init_view(app)
    db.init_app(app)  # 将db对象与app进行了关联
    manager.__init__(app=app)
    cache.init_app(app=app, config=CachingConfig.config)
    init_ext(app)
    # 注册蓝图
    return app
