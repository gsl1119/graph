# ORM => 映射表
from ext import db


# 创建表映射类
class Student(db.Model):
    __tablename__ = 'user'
    id = db.Column(db.Integer, primary_key=True)
    user_name = db.Column(db.String(50), nullable=False)
    password = db.Column(db.String(50), nullable=False)

    def __init__(self, user_name, password):
        self.user_name = user_name
        self.password = password

