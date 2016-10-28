'''
Created on Oct 17, 2016

@author: henrylevy
'''
import tornado.web
from Controller.AbstractBaseHandler import AbstractBaseHandler
from Model.SpotDatabaseManager import SQLSpotDatabaseManager

class AbstractSpotHandler(AbstractBaseHandler):
    def __init__(self, *args, **kwargs):
        super(AbstractSpotHandler, self).__init__(*args, **kwargs)
        self.db = SQLSpotDatabaseManager('parkhereapp.csgoykinzoiq.us-west-2.rds.amazonaws.com',
                                         'parkhere', 'password', 3306, 'parkhere')

    @tornado.web.authenticated # ensures that user has valid token/is signed in
    def get(self):
        pass