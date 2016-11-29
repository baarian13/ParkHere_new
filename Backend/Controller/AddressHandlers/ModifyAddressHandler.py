import tornado.web
import tornado.gen
from Controller.AddressHandlers.AbstractAddressHandler import AbstractAddressHandler

SUCCESS = '200'
FAILURE = '401'
PARTIAL = '206'

class ModifyAddressHandler(AbstractAddressHandler):
    @tornado.web.authenticated # ensures that user has valid token/is signed in
    @tornado.gen.coroutine
    def get(self):
        pass

    #Must send address and owner email in order to update picture
    #@tornado.web.authenticated # ensures that user has valid token/is signed in
    @tornado.gen.coroutine
    def post(self):
        result = SUCCESS
        ownerEmail = self.get_argument("ownerEmail","")
        address = self.get_argument("address","")
        addressID = self.get_argument("addressID","")
        spotType = self.get_argument("spotType","")
        isCovered = self.get_argument("isCovered","")
        description = self.get_argument("description","")
        if spotType:
            try:
                self.db.updateSpotType(addressID, spotType)
            except:
                result = PARTIAL
        if isCovered:
            try: 
                self.db.updateIsCovered(addressID, isCovered)
            except: 
                result = PARTIAL
        if description:
            try:
                self.db.updateDescription(addressID, description)
            except:
                result = PARTIAL
        try:
            picture = self.get_argument("picture","")
            if picture and address and ownerEmail:
                try:
                    self.db.submitPicture(picture, ownerEmail, address)
                except Exception as e:
                    print 'picture exception'
                    self.write('206')
        except Exception as e:
            self.write('401')
        self.write(result)
