'''
Created on Oct 28, 2016

@author: henrylevy
'''
from DataObjects.Spot import Spot

'''
Created on Oct 17, 2016

@author: henrylevy
'''
import tornado.gen
from Controller.SpotHandlers.AbstractSpotHandler import AbstractSpotHandler

class PostSpotHandler(AbstractSpotHandler):
    '''
    -Requests are posted here when a user is creating an account.
    -All arguments are submitted as strings.
    '''
    @tornado.web.authenticated
    @tornado.gen.coroutine
    def post(self):
        '''
        -Each argument is specified in URL format.
        -If an argument is not specified  then the creation will fail.
        -writes success if everything writes failure if user could not be authenticated
        -if success token is set, if not, token is not set and client must try again
        args:
            address->str  must be valide so that latitude and longitude can be found
            price->str (2 decimal places-will round down)
            spotType->str see spot class for details
            isCovered-> str 1 for true, 0 for false
            cancelationPolicy->str see spot class
            start->str (YYYY-MM-DD HH:00:00) (24 hour time)
            end->str (YYYY-MM-DD HH:00:00)
            isRecurring-> str 1 for true, 0 for false
            description-> str
        '''
        args = {'ownerEmail'        : self.get_secure_cookie("user"),
                'address'           : self.get_argument("address", ""),
                'spotType'          : int(self.get_argument("spotType", "")),
                'isBooked'          : False,
                'isCovered'         : bool(int(self.get_argument("isCovered", ""))),
                'cancelationPolicy' : int(self.get_argument("cancelationPolicy", "")),
                'price'             : float(self.get_argument("price", "")),
                'start'             : self.get_argument("start", ""),
                'end'               : self.get_argument("end", ""),
                'description'       : self.get_argument("description", ""),
                'isRecurring'       : bool(int(self.get_argument("isRecurring", "")))}
        spot = Spot(**args)
        try:
            self.db.insertIntoTable(spot)
            self.write('200')
        except:
            self.write('401')
