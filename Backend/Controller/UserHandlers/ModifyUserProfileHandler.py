'''
Created on Oct 17, 2016

@author: henrylevy
'''
import tornado.web
import tornado.gen
from Controller.UserHandlers.AbstractUserHandler import AbstractUserHandler

class ModifyUserProfileHandler(AbstractUserHandler):
    @tornado.web.authenticated # ensures that user has valid token/is signed in
    @tornado.gen.coroutine
    def get(self):
        pass