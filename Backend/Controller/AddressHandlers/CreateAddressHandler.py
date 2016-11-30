import tornado.gen
from Controller.AddressHandlers.AbstractAddressHandler import AbstractAddressHandler
from DataObjects.Address import Address

class CreateAddressHandler(AbstractAddressHandler):
    # @tornado.web.authenticated
    @tornado.gen.coroutine
    def post(self):
        '''
        -Each argument is specified in URL format.
        -If an argument is not specified  then the creation will fail.
        -writes success if everything writes failure if user could not be authenticated
        -if success token is set, if not, token is not set and client must try again
        args:
            address->str  must be valid so that latitude and longitude can be found
            price->str (2 decimal places-will round down)
            spotType->str see address class for details
            isCovered-> str 1 for true, 0 for false
            description-> str
        '''
        print self.get_secure_cookie("user")
        args = {'ownerEmail'        : self.get_argument("email",""),
                'address'           : self.get_argument("address", ""),
                'spotType'          : int(self.get_argument("spotType", "")),
                'isCovered'         : bool(int(self.get_argument("isCovered", ""))),
                'description'       : self.get_argument("description", "")}
        address = Address(**args)
        try:
            self.db.insertIntoTable(address)
            picture = self.get_argument("picture","")
            if picture:
                try:
                    self.db.submitPicture(picture, self.get_argument("email",""), self.get_argument("address",""))
                except Exception as e:
                    print 'picture exception'
                    self.write('206')
        except Exception as e:
            self.write('401')