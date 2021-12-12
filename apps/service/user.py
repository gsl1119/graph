from apps.models import Student
from ext import db


class User:
    @staticmethod
    def sql_login(username):
        result = db.session.query(Student).filter(Student.user_name == username).all()
        return result
