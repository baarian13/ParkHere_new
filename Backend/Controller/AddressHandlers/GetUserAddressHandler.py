import tornado.gen
from Controller.AddressHandlers.AbstractAddressHandler import AbstractAddressHandler
import json

class ViewAddressHandler(AbstractAddressHandler):
    @tornado.web.authenticated
    @tornado.gen.coroutine
    def get(self):
        ownerEmail = self.get_argument('email')
        if ownerEmail:
            results = self.db.getAddressIDsOwnedBy(ownerEmail)
            self.write(json.dumps(results))