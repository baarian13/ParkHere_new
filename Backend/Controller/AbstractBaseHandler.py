'''
Created on Oct 17, 2016

@author: henrylevy
'''
import tornado.web

class AbstractBaseHandler(tornado.web.RequestHandler):
    def get_current_user(self):
        return self.get_secure_cookie("user")
    
    def get(self):
        print "test"
        if not self.current_user:
            self.redirect("/signin")
        print "test2"

class MainHandler(AbstractBaseHandler):
    pass