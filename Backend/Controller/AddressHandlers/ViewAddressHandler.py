import tornado.gen
from Controller.AddressHandlers.AbstractAddressHandler import AbstractAddressHandler
import json

class ViewAddressHandler(AbstractAddressHandler):
    @tornado.web.authenticated
    @tornado.gen.coroutine
    def get(self):
        addressID = self.get_argument("addressID")
        #verify this works.
        if addressID:
            res = self.db.viewAddressInfo(addressID)
            results = { 'ownerEmail'			: res[0],
                        'address'        		: res[1],
                        'sportType'				: res[2],
                        'isCovered' 			: res[3],
                        'description'			: res[4],
                        'picturePath'			: res[5]}
            self.write(json.dumps(results))