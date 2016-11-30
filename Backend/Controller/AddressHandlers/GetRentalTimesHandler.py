import tornado.gen
from Controller.AddressHandlers.AbstractAddressHandler import AbstractAddressHandler

class GetRentalTimesHandler(AbstractAddressHandler):
    @tornado.web.authenticated
    @tornado.gen.coroutine
    def get(self):
        addressID = self.get_argument('addressID')
        try:
            if addressID:
                count = self.db.getCountForAddress(addressID)
                self.write(str(count))
        except Exception as e:
            print e