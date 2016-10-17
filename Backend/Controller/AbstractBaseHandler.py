'''
Created on Oct 17, 2016

@author: henrylevy
'''
import tornado
from Controller.SinginHandler import SigninHandler

class AbstractBaseHandler(tornado.web.RequestHandler):
    def get_current_user(self):
        return self.get_secure_cookie("user")
    
    def get(self):
        if not self.current_user:
            self.redirect("/signin")

class MainHandler(AbstractBaseHandler):
    pass

settings = {
    "cookie_secret": "__TODO:_GENERATE_RANDOM_VALUE_HERE__",
    "login_url": "/signin",
}
application = tornado.web.Application([
    (r"/", MainHandler),
    (r"/signin", SigninHandler),
], **settings)