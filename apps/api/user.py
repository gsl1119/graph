from . import common_dp
import json
from flask import request

from ext import jwt

from flask_jwt_extended import (create_access_token, get_jwt_identity, jwt_required)

from apps.models import Student
from ext import db
from ext import bcrypt


@common_dp.route('/login', methods=['GET', 'POST'])
def login():
    if request.method == "POST":
        data = json.loads(request.data)
        username = data['userName']
        password = data['userPass']
        try:
            result = db.session.query(Student).filter(Student.user_name == username).all()
            if len(result) == 0:
                return {
                    "code": 4000,
                    "msg": "账号或密码错误",
                }
            for row in result:
                real_pwd = row.password
                if real_pwd == password:
                    access_token = create_access_token(identity=12323)
                    return {
                        "code": 2000,
                        "msg": "登录成功",
                        "token": access_token,
                        "nickname": username
                    }
                else:
                    return {
                        "code": 4000,
                        "msg": "账号或密码错误",
                    }
        except:
            return {
                "code": 4000,
                "msg": "账号或密码错误",
            }
# flask响应return时老是报相同的错：TypeError: The view function did not return a valid response. The function either returned
# None or ended without a return statement.