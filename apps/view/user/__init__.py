from flask import Blueprint, Flask

common_bp = Blueprint('common', __name__)


from . import routes  # 此处是为了加载routes.py 否则 routes.py 将无法被执行


def register_user_views(app: Flask):
    app.register_blueprint(common_bp)
