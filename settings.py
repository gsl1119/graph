import configparser, os


config = configparser.ConfigParser()
root_dir = os.path.dirname(os.path.abspath(__file__))  # 获取当前目录
config.read(root_dir + '/config.ini')
# 获取配置文件配置
host = config.get('mysql', 'host')
port = config.get('mysql', 'port')
user = config.get('mysql', 'user')
password = config.get('mysql', 'password')
db = config.get('mysql', 'db')

cache_type = config.get('redis', 'cache_type')
cache_redis_host = config.get('redis', 'cache_redis_host')
cache_redis_port = config.get('redis', 'cache_redis_port')


class Config:
    DEBUG: False
    TESTING: False
    SQLALCHEMY_DATABASE_URI = 'mysql+pymysql://' + user + ':' + password + '@' + host + ':' + port + '/' + db
    SQLALCHEMY_TRACK_MODIFICATIONS = False
    SQLALCHEMY_ECHO = True


# 生产环境配置
class DevelopmentConfig(Config):
    DEBUG = True
    ENV = 'development'


class ProductionConfig(Config):
    DATABASE_URT = ''


class TestingConfig(Config):
    TESTING = True


class CachingConfig(Config):
    config: {
        'CACHE_TYPE': cache_type,
        'CACHE_REDIS_HOST': cache_redis_host,
        'CACHE_REDIS_PORT': cache_redis_port
    }