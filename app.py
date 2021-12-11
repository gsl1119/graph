from datetime import datetime, timedelta

from flask_migrate import Migrate, MigrateCommand  # 最新版的不在支持MigrateCommand
from flask_script import Manager
# Flask Script扩展提供向Flask插入外部脚本的功能，包括运行一个开发用的服务器，一个定制的Python
# shell，设置数据库的脚本，cronjobs 及其他运行在web应用之外的命令行任务；使得脚本和系统分开；
from apps import create_app
from ext import db
from flask_cors import CORS


app = create_app()


app.config["JWT_SECRET_KEY"] = "super-secret"  # Change this! 一定要选择加密方式
app.config["JWT_ACCESS_TOKEN_EXPIRES"] = timedelta(hours=2)

CORS(app, resources=r'/*')

manager = Manager(app=app)

Migrate(app=app, db=db)


manager.add_command('db', MigrateCommand)


@manager.command
def init():
    print('初始化')


if __name__ == '__main__':
    manager.run()
