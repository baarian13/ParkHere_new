import tornado.gen
from Controller.AddressHandlers.AbstractAddressHandler import AbstractAddressHandler

SUCCESS = '200'
FAILURE = '401'

class DeleteAddressHandler(AbstractAddressHandler):
    '''
    -Requests are posted here when a user is creating an account.
    -All arguments are submitted as strings.
    '''
    #@tornado.web.authenticated
    @tornado.gen.coroutine
    def post(self):
        result = SUCCESS
        try:
            ownerEmail = self.get_argument("email","")
            addressID = self.get_argument("addressID", "")
            self.db.deleteAddress(ownerEmail, addressID)
        except:
            result = FAILURE

        self.write(result)