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
        self.db = SQLSpotDatabaseManager('parkhere.cghr1zvgeuqd.us-west-1.rds.amazonaws.com',
                                         'zhichenw', 'zhichenw', 3306, 'ParkHere')

    @tornado.web.authenticated # ensures that user has valid token/is signed in
    def get(self):
        pass