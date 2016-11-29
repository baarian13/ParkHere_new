import tornado.gen
from Controller.SpotHandlers.AbstractSpotHandler import AbstractSpotHandler
import json


class ViewRentalsHandler(AbstractSpotHandler):
    @tornado.web.authenticated
    @tornado.gen.coroutine
    def get(self):
        renterEmail = self.get_argument("email")
        if renterEmail:
            results = [{'id'       : res[0],
                        'address'  : res[1]}
                       for res in self.db.getSpotsRentedBy(renterEmail)]
            self.write(json.dumps(results))
