'''
Created on Oct 17, 2016

@author: henrylevy
'''
import tornado.web
from Controller.UserHandlers.AbstractUserHandler import AbstractUserHandler

class RateUserHandler(AbstractUserHandler):
    @tornado.web.authenticated # ensures that user has valid token/is signed in
    def get(self):
        pass