from datetime import timedelta


# Flask Script扩展提供向Flask插入外部脚本的功能，包括运行一个开发用的服务器，一个定制的Python
# shell，设置数据库的脚本，cronjobs 及其他运行在web应用之外的命令行任务；使得脚本和系统分开；
from flask_migrate import MigrateCommand

from apps import create_app
from ext import manager

app = create_app()


app.config["JWT_SECRET_KEY"] = "super-secret"  # Change this! 一定要选择加密方式
app.config["JWT_ACCESS_TOKEN_EXPIRES"] = timedelta(hours=2)


manager.add_command('db', MigrateCommand)


@manager.command
def init():
    print('初始化')


if __name__ == '__main__':
    manager.run()
