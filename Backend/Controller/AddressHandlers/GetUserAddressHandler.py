import tornado.gen
from Controller.AddressHandlers.AbstractAddressHandler import AbstractAddressHandler
import json

class GetUserAddressHandler(AbstractAddressHandler):
    @tornado.web.authenticated
    @tornado.gen.coroutine
    def get(self):
        ownerEmail = self.get_argument('email')
        try:
            if ownerEmail:
                results = self.db.getAddressIDsOwnedBy(ownerEmail)
                self.write(json.dumps(results))
        except Exception as e:
            print e