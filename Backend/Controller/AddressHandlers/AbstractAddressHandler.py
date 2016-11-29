import tornado.web
from Controller.AbstractBaseHandler import AbstractBaseHandler
from Model.UserDatabaseManager import SQLUserDatabaseManager

class AbstractAddressHandler(AbstractBaseHandler):

    def __init__(self, *args, **kwargs):
        super(AbstractAddressHandler, self).__init__(*args, **kwargs)
        self.db = SQLAddressDatabaseManager('parkhere.cghr1zvgeuqd.us-west-1.rds.amazonaws.com',
                                         'zhichenw', 'zhichenw', 3306, 'ParkHere')

    @tornado.web.authenticated # ensures that user has valid token/is signed in
    def get(self):
        pass