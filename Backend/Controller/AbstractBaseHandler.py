'''
Created on Oct 17, 2016

@author: henrylevy
'''
import tornado.web

class AbstractBaseHandler(tornado.web.RequestHandler):
    def get_current_user(self):
        return self.get_secure_cookie("user")
    
    @property
    def current_user(self):
        return self.get_current_user()

class MainHandler(AbstractBaseHandler):
    pass