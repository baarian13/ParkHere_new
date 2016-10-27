'''
Created on Oct 17, 2016

@author: henrylevy
'''
import tornado.web
from Controller.AbstractBaseHandler import AbstractBaseHandler
from Model.UserDatabaseManager import SQLUserDatabaseManager

class AbstractUserHandler(AbstractBaseHandler):

    def __init__(self):
        self.db = SQLUserDatabaseManager('parkhereapp.csgoykinzoiq.us-west-2.rds.amazonaws.com',
                                         'parkhere', 'password', 3306)

    @tornado.web.authenticated # ensures that user has valid token/is signed in
    def get(self):
        pass